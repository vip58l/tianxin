package com.tianxin.activity.shareqrcode;

import static com.blankj.utilcode.util.StringUtils.getString;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Tiktokholder.sTiktokAdapter;
import com.tianxin.Module.api.memberparent;
import com.tianxin.widget.EndLessOnScrollListener;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 邀请好友列表
 */
public class activity_Share extends BasActivity2 implements SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = activity_Share.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private final List<memberparent> list = new ArrayList<>();
    private sTiktokAdapter sTiktokAdapter;

    public static void starAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_Share.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_share;
    }

    @Override
    public void iniview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(sTiktokAdapter = new sTiktokAdapter(context, list));
        recyclerview.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData();
            }
        });
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.memberparent(totalPage, paymnets);
    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                onRefresh();
                break;
        }

    }

    @Override
    public void onRefresh() {
        totalPage = 0;
        list.clear();
        initData();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void OnEorr() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
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
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<memberparent> data = (List<memberparent>) object;
            if (data.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrtext));
                }
                OnEorr();
                return;
            }
            list.addAll(data);
            itemback.settitle(String.format("邀请好友(%s)", list.get(0).getCount()));
            sTiktokAdapter.notifyDataSetChanged();
        }

        @Override
        public void ToKen(String msg) {
            activity_Share.super.paymnets.ToKen(msg);
        }
    };
}