package com.tianxin.activity.Image;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.blankj.utilcode.util.StringUtils.getString;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.activity_svip;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_item_img;
import com.tianxin.dialog.dialog_item_vip;
import com.tianxin.dialog.dialog_item_vip2;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.itembackTopbr;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.tupianzj;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片浏览展示
 */
public class activity_img_cover extends BasActivity2 {
    String TAG = "activity_img_cover";
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    LinearLayoutManager manager1;
    GridLayoutManager manager2;
    boolean isboole = false;

    public static void starsetAction(Context context) {
        context.startActivity(new Intent(context, activity_img_cover.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_img_cover;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg268));
        itemback.setrighttext(getString(R.string.tv_msg271));
        itemback.righttext.setTextColor(getResources().getColor(R.color.colorAccent));
        manager1 = new LinearLayoutManager(context);
        manager2 = new GridLayoutManager(context, 2);
        radapter = new Radapter(context, list, Radapter.activity_img_cover, callback);
        recyclerview.setLayoutManager(manager2);
        recyclerview.setAdapter(radapter);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    Toashow.show(getString(R.string.eorrfali2));
                    return;
                }
                loadMoreData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    Toashow.show(getString(R.string.eorrfali2));
                    return;
                }
                toinitData();
            }
        });
        refreshlayout.autoRefresh();
    }

    @Override
    public void initData() {

    }


    public void toinitData() {
        Glide.get(context).clearMemory();
        totalPage++;
        datamodule.activity_img_cover(totalPage, paymnets);
    }

    public void loadMoreData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        list.clear();
        radapter.notifyDataSetChanged();
        totalPage = 0;
        toinitData();
    }

    @Override
    @OnClick({R.id.tv3title})
    public void OnClick(View v) {
        isboole = !isboole;
        recyclerview.setLayoutManager(isboole ? manager1 : manager2);
        itemback.setrighttext(isboole ? getString(R.string.tv_msg270) : getString(R.string.tv_msg271));
    }

    @Override
    public void OnEorr() {
        if (refreshlayout != null) {
            refreshlayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onClick(Object object) {
            List<tupianzj> data = (List<tupianzj>) object;
            list.addAll(data);
            radapter.notifyDataSetChanged();
        }


        @Override
        public void onSuccess(String msg) {
            totalPage--;
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            totalPage--;
            activity_img_cover.super.paymnets.ToKen(msg);
        }

    };

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            if (userInfo.getVip() == 0) {
                showdialog();
                return;
            }

            tupianzj tupianzj = (com.tencent.opensource.model.tupianzj) list.get(position);
            if (tupianzj != null) {
                activity_img_page.starAction(context, gson.toJson(tupianzj));

            }
        }

        @Override
        public void LongClickListener(int position) {

        }
    };

    /**
     * 弹出购买VIP内容  今日聊天次数已达上限 提示购买VIP或充值金币
     */
    public void showdialog() {
        dialog_msg_svip.dialogmsgsvip(context, getString(R.string.tm96), getString(R.string.tv_msg228), getString(R.string.tv_msg153), messevip);
        //dialogmyshow2(context, paymnets1);

    }

    /**
     * VIP提示点击跳转
     */
    private Paymnets messevip = new Paymnets() {
        @Override
        public void onSuccess() {
            activity_svip.starsetAction(context);   //跳转到购买VIP会员
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context); //转到赚钱任务页
        }
    };

    /**
     * 图片浏览提示升级VIP会员
     */
    public static void dialogmyshow1(Context context, Paymnets paymnets) {
        dialog_item_vip.dialogitemvip(context, 20, paymnets);
    }

    /**
     * 图片浏览提示升级VIP会员
     */
    public static void dialogmyshow2(Context context, Paymnets paymnets) {
        dialog_item_img.dialogitemvip(context, 20, paymnets);
    }

    /**
     * 视频浏览提示升级VIP会员
     */
    public static void dialogmyshow3(Context context, Paymnets paymnets) {
        dialog_item_vip2.dialogitemvip(context, paymnets, null);
    }

    /**
     * 升级VIP会员
     */
    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void onFail() {

        }

        @Override
        public void onClick() {
            activity_svip.starsetAction(context);   //跳转到购买VIP会员
        }
    };

}