package com.tianxin.activity;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tencent.opensource.model.byprice;
import com.tencent.opensource.model.mpayurl;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.itemdecoration.RecycleViewDivider;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 支付H5
 */
public class Activity_qiaoapi extends BasActivity2 {
    @BindView(R.id.sendcontext)
    TextView sendcontext;
    private int money;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.mtitle)
    TextView titletext;
    @BindView(R.id.money)
    TextView moneytext;
//    @BindView(R.id.smartRefreshLayout)
//    SmartRefreshLayout smartRefreshLayout;
    byprice byprices = null;
    private String title;
    private int id;
    private AlertDialog alertDialog;
    private mpayurl mpayurl;

    /**
     * 打开支付列表
     *
     * @param context
     */
    public static void starAction(Activity context, String msg, int id, int money, int type) {
        Intent intent = new Intent();
        intent.putExtra(Constants.money, money);
        intent.putExtra(Constants.TYPE, type);
        intent.putExtra(Constants.TITLE, msg);
        intent.putExtra(Constants.id, id);
        intent.setClass(context, Activity_qiaoapi.class);
        context.startActivityForResult(intent, Config.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_qiaoapi;
    }

    @Override
    public void iniview() {
        money = getIntent().getIntExtra(Constants.money, 0);
        TYPE = getIntent().getIntExtra(Constants.TYPE, 1);
        id = getIntent().getIntExtra(Constants.id, 0);
        title = getIntent().getStringExtra(Constants.TITLE);

        titletext.setText(title);
        moneytext.setText(String.format(getString(R.string.t11) + "", money));
        sendcontext.setText(String.format(getString(R.string.t112) + "", money));

   /*     smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100*//*,false*//*);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000*//*,false*//*);//传入false表示加载失败
                loadMoreData();
            }
        });*/

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new RecycleViewDivider(context, 1));
        recyclerView.setAdapter(radapter = new Radapter(context, list, Radapter.chat_item11, new Callback() {
            @Override
            public void onFall() {

            }

            @Override
            public void OnClickListener(int position) {
                for (Object o : list) {
                    byprice byprice = (com.tencent.opensource.model.byprice) o;
                    byprice.setCheckbox(false);
                }
                byprices = (com.tencent.opensource.model.byprice) list.get(position);
                byprices.setCheckbox(true);
                radapter.notifyDataSetChanged();
            }
        }));
    }

    @Override
    public void initData() {
        dialogshow(getString(R.string.ladmsg));
        datamodule.playlist(money, new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                List<byprice> byprice = (List<com.tencent.opensource.model.byprice>) object;
                list.clear();
                list.addAll(byprice);
                radapter.notifyDataSetChanged();
                dialogLoadings();
            }

            @Override
            public void onFail() {
                dialogLoadings();
            }

            @Override
            public void isNetworkAvailable() {
                dialogLoadings();
            }

        });
    }

    @Override
    @OnClick({R.id.sendcontext})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sendcontext:
                if (byprices == null) {
                    Toashow.toastMessage(getString(R.string.toast1));
                    return;
                }

                //发起支付请求
                dialogshow(getString(R.string.t113));
                datamodule.playmoney(id, money, byprices.getId(), TYPE, new Paymnets() {
                    @Override
                    public void isNetworkAvailable() {
                        dialogLoadings();

                    }

                    @Override
                    public void onSuccess(Object Data) {
                        mpayurl = (com.tencent.opensource.model.mpayurl) Data;
                        //APP调起浏览器打开H5支付链接
                        dialogLoadings();

                        //内部浏览器打开
                        DyWebActivity.starAction(context,mpayurl.getPayurl());

                        //转到系统默认浏览器打开
                        //update.updatedown(activity, mpayurl.getPayurl());

                    }

                    @Override
                    public void onFail() {
                        dialogLoadings();
                    }
                });
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    /**
     * 弹窗提示
     */
    public void myAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.pay_00))
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(getString(R.string.pay_01))
                .setPositiveButton(R.string.pay_02, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mpayurl == null) {
                            Toashow.toastMessage(getString(R.string.pay_03) + "");
                            dialog.dismiss();
                            return;
                        }
                        querselect();

                    }
                })
                .setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        //alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * 查询支付结果
     */
    public void querselect() {
        dialogshow(getString(R.string.toast07));
        datamodule.querselect(mpayurl.getOrderid(), new Paymnets() {
            @Override
            public void onSuccess() {
                dialogLoadings();
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                finish();
            }

            @Override
            public void onFail() {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                dialogLoadings();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.requestCode) {
            myAlertDialog();
        }
    }
}