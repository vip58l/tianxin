package com.tianxin.activity.Withdrawal;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianxin.activity.Withdrawal.moneylog.activity_moneylog;
import com.tianxin.activity.shareqrcode.activity_getspreadUrl;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_item_view_bain;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.reward;
import com.tianxin.Module.api.tasklist;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.shareqrcode.activity_Share;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 赚钱任务
 */
public class References extends BasActivity2 {
    String TAG = References.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.goldcoin)
    TextView goldcoin;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.msg_content)
    TextView msg_content; @BindView(R.id.chartfacegv)
    TextView chartfacegv;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    public static final int a0 = 0;
    public static final int a1 = 1;
    public static final int a2 = 2;
    public static final int a3 = 3;
    public static final int a4 = 4;
    public static final int a5 = 5;

    public static void starAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, References.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_references;
    }

    @Override
    public void initData() {
        getdata();
    }

    @OnClick({R.id.linearLayout7, R.id.linearLayout5, R.id.chartfacegv, R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.linearLayout7:
                //我的余额
                datamodule.reward(a3, paymnets);
                break;
            case R.id.linearLayout5:
                //我的钱包
                activity_balance.setAction(context);
                break;
            case R.id.tv3title:
                //现金明细
                activity_moneylog.setAction(context);
                break;
            case R.id.chartfacegv:
                //金币转换
                datamodule.goldcoinconversion(paymnets2);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg127));
        itemback.setrighttext(getString(R.string.tv_ms_msg));
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        radapter = new Radapter(context, list, Radapter.references);
        radapter.setPaymnets(paymnets);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(radapter);

        Drawable drawable1=getResources().getDrawable(R.mipmap.signature_refresh_icon_n);
        drawable1.setBounds(0,0,60,60);
        chartfacegv .setCompoundDrawables(null,drawable1,null,null);
    }

    /**
     * 读取操作说明列表
     */
    public void getdata() {
        datamodule.tasklist(a1, paymnets);
        datamodule.reward(a3, paymnets);
    }

    /**
     * 签到请求
     */
    public void qiandaoadd() {
        datamodule.qiandaoadd(a2, paymnets);
    }

    /**
     * 请求是否已绑定数居
     */
    public void bindlog() {
        datamodule.bindlog(a4, paymnets);
    }

    /**
     * 请求监听回调
     */
    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void status(int result) {
            //item适配器回调
            Object o = list.get(result);
            tasklist t = (tasklist) o;
            switch (t.getType()) {
                case a0:
                    //推荐分享
                    //打开链接
                    // tostartActivity(t);
                    //startActivity(new Intent(context, activity_scan_qrcode.class));
                    //startActivity(new Intent(context, activity_shareqrcode.class));

                    activity_getspreadUrl.starAction(context);
                    break;
                case a1:
                    //查看好友
                    startActivity(new Intent(context, activity_Share.class));
                    break;
                case a2:
                    //签到
                    qiandaoadd();
                    break;
                case a3:
                    //申请提现
                    Withdrawals.setAction(activity);
                    break;
                case a4:
                    //绑定好友
                    dialog_item_view_bain.myshow(context, paymnets);
                    break;

            }
        }

        @Override
        public void payonItemClick(String date, int TYPE) {
            try {
                switch (TYPE) {
                    case a1:
                        //赚钱任务
                        list.clear();
                        list.addAll(JsonUitl.stringToList(date, tasklist.class));
                        radapter.notifyDataSetChanged();
                        bindlog(); //查询帮定详情
                        break;
                    case a2:
                        //签到刷新
                        Mesresult r = gson.fromJson(date, Mesresult.class);
                        Toashow.show(r.getMsg());
                        if (r.getStatus().equals("1")) {
                            refresh();
                        }
                        break;
                    case a3:
                        //刷新金币
                        reward reward = gson.fromJson(date, reward.class);
                        goldcoin.setText(reward.getBalance());
                        money.setText(reward.getMoney());
                        break;
                    case a4:
                        mesresult = gson.fromJson(date, Mesresult.class);
                        if (mesresult.getStatus().equals("1")) {
                            for (Object o : list) {
                                tasklist m = (com.tianxin.Module.api.tasklist) o;
                                if (m.getType() == a4) {
                                    m.setBind(1);
                                }

                            }

                        }
                        radapter.notifyDataSetChanged();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));

        }

        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    list.clear();
                    radapter.notifyDataSetChanged();
                    getdata();

                }
            });
        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastShortMessage(getResources().getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void ToKen(String msg) {
            References.super.paymnets.ToKen(msg);
        }
    };

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
            //刷新金币余额
            datamodule.reward(a3, paymnets);
        }

        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }
    };

    /**
     * 刷新通知
     */
    public void refresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Object o : list) {
                    tasklist tasklist = (tasklist) o;
                    if (tasklist.getType() == a2) {
                        tasklist.setQiandao(1);
                    }
                }
                radapter.notifyDataSetChanged();
                datamodule.reward(a3, paymnets);
            }
        });
    }

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(false);
            list.clear();
            radapter.notifyDataSetChanged();
            getdata();
        }
    };

    /**
     * 打开浏览器
     *
     * @param t
     */
    private void tostartActivity(tasklist t) {
        if (!TextUtils.isEmpty(t.getPath())) {
            Intent intent = new Intent();
            intent.setClass(context, DyWebActivity.class);
            intent.putExtra(Constants.VIDEOURL, t.getPath());
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getdata();
    }
}