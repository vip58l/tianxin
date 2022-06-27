package com.tianxin.Fragment.page1.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Banner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class one1 extends BasFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Banner header;

    public static Fragment show(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        one1 one1 = new one1();
        one1.setArguments(bundle);
        return one1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE);
        if (arguments != null) {
            if (isVisibleToUser && list2.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.autoRefresh();
                    }
                }, 100);
            }
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home_one, null);
    }

    @Override
    public void iniview() {
        if (true) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            radapter = new Radapter(context, list2, Radapter.getOne1, type, paymnets);
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            radapter = new Radapter(context, list2, Radapter.getOne2, type, paymnets);
        }
        recyclerView.setAdapter(radapter);
        recyclerView.addItemDecoration(new MyDecoration(context, LinearLayout.HORIZONTAL));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                totalPage = 0;
                list2.clear();
                radapter.notifyDataSetChanged();
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                loadMoreData();
            }
        });
    }

    @Override
    public void initData() {
        if (type == Constants.TENCENT) {
            //添加HeaderView和FooterView
            if (header == null) {
                header = new Banner(context);
            }


        }
        if (!userInfo.isPermission()) {
            ActivityLocation.checkpermissions(getActivity());
        }
        GPSAMapLocation();
    }


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
        showDialogFrag();
    if (smartRefreshLayout!=null){
        smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
    }

    }

    @Override
    public void onRefresh() {

    }

    public void loadMoreData() {
        totalPage++;
        datamodule.one2list(totalPage, mapLocation, type, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        public void onFail() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        public void onError() {
            try {
                totalPage--;
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        public void ToKen(String msg) {
            totalPage--;
            one1.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(Object object) {
            List<com.tencent.opensource.model.member> members = (List<member>) object;
            list2.addAll(members);
            radapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void onItemClick(int position) {

        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsto.setPgs(1);
        GPSAMapLocation();
    }

    /**
     * 初始化定位
     */
    public void GPSAMapLocation() {
        if (mapLocation == null) {
            /**
             * 初始化定位
             */
            lbsamap.getmyLocation(callback);
        } else {
            //刷新定位地址
            radapter.setaMapLocation(mapLocation);
        }

    }
}
