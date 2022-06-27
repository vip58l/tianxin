package com.tianxin.Fragment.fragment.browsefragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 谁看了我
 * 我看了谁
 */
public class Frnetbrowse extends BasFragment {
    String TAG = Frnetbrowse.class.getSimpleName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    boolean isVisibleToUser;

    public static Fragment gets(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        Fragment fragment = new Frnetbrowse();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt(Constants.TYPE);
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        isVisibleToUser = menuVisible;
        if (menuVisible) {
            if (isVisibleToUser && list2.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.autoRefresh();
                    }
                }, 200);
            }
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fmessage_view_item, null);
    }

    @Override
    public void iniview() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list2, Radapter.followlist_item, callback));
        recyclerview.addItemDecoration(new MyDecoration(context, 0));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadMoreData();
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

    }

    public void toinitData() {
        totalPage++;
        datamodule.view_user(totalPage, type, paymnets);
    }

    public void loadMoreData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(R.string.eorrfali2);
            return;
        }
        totalPage = 0;
        list2.clear();
        radapter.notifyDataSetChanged();
        toinitData();
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                loadMoreData();
                break;
        }
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() == 0 ? View.GONE : View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {

    }

    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            OnEorr();
        }

        @Override
        public void onFail() {
            totalPage--;
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<followlist> toList = (List<followlist>) object;
            list2.addAll(toList);
            radapter.setDelshow(type == 1 ? false : true);
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                Toashow.show(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);

        }
    };

    public Callback callback = new Callback() {
        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void OnClickListener(int position) {
            if (userInfo.getVip() == 0) {
                dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip), getString(R.string.tv_msg228), getString(R.string.tv_msg153), paymnets);
            } else {
                tpstartActivity(position);
            }
        }
    };

    private void tpstartActivity(int position) {
        followlist followlist = (com.tencent.opensource.model.followlist) list2.get(position);
        member member = followlist.getMember();
        if (member == null) {
            return;
        }
        String userid = type == 1 ? followlist.getTouserid() : followlist.getUserid();
        Intent intent = new Intent(context, activity_picenter.class);
        intent.putExtra(Constants.USERID, userid);
        startActivity(intent);
    }

}
