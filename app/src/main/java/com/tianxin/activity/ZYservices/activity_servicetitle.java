package com.tianxin.activity.ZYservices;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.itemdecoration.RecycleViewDivider;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.servicetitle;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加更多
 */
public class activity_servicetitle extends BasActivity2 implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "activity_servicetitle";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getview() {
        return R.layout.activity_servicetitle;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg68));
        itemback.righttext.setText(R.string.tv_msg69);
        itemback.setHaidtopBackgroundColor(getResources().getColor(R.color.home3));
        //itemback.righttext.setTextColor(getResources().getColor(R.color.homeback));
        itemback.righttext.setVisibility(View.VISIBLE);
        itemback.contertext.setTextSize(16);
        radapter = new Radapter(this, list, Radapter.activity_servicetitle);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                Object o = list.get(result);
                mystartActivity(o);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL, 5, getResources().getColor(R.color.home3)));
        recyclerview.setAdapter(radapter);
    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(BuildConfig.HTTP_API + "/getservicetitle", new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<servicetitle> servicetitles = JsonUitl.stringToList(date, servicetitle.class);
                    list.addAll(servicetitles);
                    radapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                OnEorr();
            }

            @Override
            public void fall(int code) {
                OnEorr();
            }
        });

    }

    @OnClick({R.id.eorr, R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                initData();
                break;
            case R.id.tv3title:
                staralertDialog();
                break;
        }
    }

    @Override
    public void OnEorr() {
        swipeRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        list.clear();

        initData();
    }

    public void mystartActivity(Object o) {
        Intent intent = new Intent(activity_servicetitle.this, activity_serviceaccount.class);
        intent.putExtra(Constants.ACCOUNT, o.toString());
        startActivity(intent);
    }

    /**
     * 弹窗提示消息
     */
    public void staralertDialog() {
//        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_item_upmsg, null);
//        TextView tv = inflate.findViewById(R.id.upmsg);
//        tv.setText(Html.fromHtml(getString(R.string.dialog_setMessage2)));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alertDialog_title);
        alertDialog.setMessage(R.string.dialog_setMessage2);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        /*alertDialog.setNegativeButton(R.string.AlertDialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/
        //监听事件
        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "对话框显示了");
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "对话框消失了");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        dialog.show();


    }
}