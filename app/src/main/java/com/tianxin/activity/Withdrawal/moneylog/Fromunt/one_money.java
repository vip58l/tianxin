package com.tianxin.activity.Withdrawal.moneylog.Fromunt;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.moneyLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class one_money extends BasFragment {
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layout_froment, null);
    }

    @Override
    public void iniview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(radapter = new Radapter(context, list2, Radapter.item_moneymsg));
        recyclerView.addItemDecoration(myDecoration);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                initData();
            }
        });
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.moneylog(totalPage, 1, paymnets);
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
        if (refreshlayout != null) {
            refreshlayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onRefresh() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<moneyLog> moneyLogList = (List<moneyLog>) object;
            if (moneyLogList.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    ToastUtil.toastLongMessage(getString(R.string.eorrtext));
                }
            } else {
                list2.addAll(moneyLogList);
                radapter.notifyDataSetChanged();
            }
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastLongMessage(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            one_money.super.paymnets.ToKen(msg);
        }
    };
}
