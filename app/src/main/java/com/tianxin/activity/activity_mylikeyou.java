package com.tianxin.activity;

import static com.blankj.utilcode.util.StringUtils.getString;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tianxin.Module.McallBack;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_msg_svip;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的粉丝
 */
public class activity_mylikeyou extends BasActivity2 implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.itembackTopbr)
    itembackTopbr itembackTopbr;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    boolean isVisibleToUser;

    public static void setAction(Context context){
        context.startActivity(new Intent(context, activity_mylikeyou.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_mylikeyou;
    }

    @Override
    public void iniview() {
        itembackTopbr.settitle(getString(R.string.tv_msg67));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerview.setLayoutManager(gridLayoutManager);
        radapter = new Radapter(context, list, Radapter.Randomgreet);
        radapter.setPaymnets(paymnets);
        recyclerview.setAdapter(radapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(500/*,false*/);//传入false表示刷新失败
                activity_mylikeyou.this.onRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                toinitData();
            }
        });
    }

    @Override
    public void initData() {
        if (!isVisibleToUser) {
            isVisibleToUser = true;
            toinitData();
        }
    }

    public void toinitData() {
        totalPage++;
        datamodule.mylikeyout(totalPage, paymnets);
    }

    @Override
    public void onRefresh() {
        list.clear();
        totalPage = 0;
        toinitData();

    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                initData();
                break;
        }

    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void status(int result) {
            if (!Config.isNetworkAvailable()) {
                ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                return;
            }
            Object o = list.get(result);
            member member = (member) o;
            if (member == null) {
                return;
            }

            if (BuildConfig.TYPE == 1) {
                if (userInfo.getVip() == 0) {
                    dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip), getString(R.string.tv_msg228), getString(R.string.tv_msg153), paymnets);
                    return;
                }
            }

            Intent intent = new Intent();
            intent.setClass(context, activity_picenter.class);
            intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
            startActivity(intent);
        }

        @Override
        public void onSuccess(Object object) {
            List<followlist> followlistList = (List<followlist>) object;
            for (followlist followlist : followlistList) {
                member member = followlist.getMember();
                if (member != null) {
                    list.add(member);
                }
            }
            radapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void ToKen(String msg) {
            activity_mylikeyou.super.paymnets.ToKen(msg);
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);

        }
    };
}