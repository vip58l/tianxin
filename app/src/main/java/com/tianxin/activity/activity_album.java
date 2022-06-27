package com.tianxin.activity;

import static com.blankj.utilcode.util.StringUtils.getString;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;

import com.tianxin.activity.picenter.activity_picbage;
import com.tianxin.adapter.Radapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.imglist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 他的相册
 */
public class activity_album extends BasActivity2{
    String TAG = "activity_album";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @Override
    protected int getview() {
        return R.layout.activity_album;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.album2));
        getuserid = getIntent().getStringExtra(Constants.USERID);
        radapter = new Radapter(context, list, Radapter.activity_album);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                staractivitygo(result);
            }
        });
        recyclerview.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerview.setAdapter(radapter);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                initData();
            }
        });
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.getimglist(getuserid, paymnets, totalPage);

    }

    @Override
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

    private void staractivitygo(int position) {
        Intent intent = new Intent();
        intent.setClass(this, activity_picbage.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra("perimg", (Serializable) list);
        startActivity(intent);
    }

    public void loadMoreData() {
        totalPage = 0;
        list.clear();
        radapter.notifyDataSetChanged();
        initData();
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
        public void onSuccess(Object object) {
            List<imglist> data = (List<imglist>) object;
            if (data.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    Toashow.show(getString(R.string.eorrtext));
                }
            }
            if (data.size() > 0) {
                list.addAll(data);
                radapter.notifyDataSetChanged();
            }
            OnEorr();
        }


    };
}