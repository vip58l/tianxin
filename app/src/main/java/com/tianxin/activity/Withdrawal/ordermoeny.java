package com.tianxin.activity.Withdrawal;

import android.view.View;

import com.tianxin.adapter.setAdapter;
import com.tencent.opensource.model.item;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.order_moeny;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.pullableview.MyListener;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tianxin.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现订单记录
 */
public class ordermoeny extends BasActivity2 {
    @BindView(R.id.itemback)
    itembackTopbr itembackTopbr;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @BindView(R.id.listview)
    PullableListView listview;
    List<item> list = new ArrayList<>();
    private final String TAG = ordermoeny.class.getSimpleName();

    @Override
    protected int getview() {
        return R.layout.activity_ordermoeny;
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
        if (refresh_view != null) {
            refresh_view.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void iniview() {
        itembackTopbr.settitle(getString(R.string.tv_msg60));
        refresh_view.setOnRefreshListener(myListener);
        listview.setAdapter(adappter = new setAdapter(context, list));
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.ordermoeny(totalPage, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<order_moeny> orderMoenyList = (List<order_moeny>) object;
            if (orderMoenyList.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    Toashow.show(getResources().getString(R.string.eorrtext));
                }
                OnEorr();
                return;
            }
            for (order_moeny or : orderMoenyList) {
                item item = new item();
                item.object = or;
                item.type = com.tianxin.adapter.setAdapter.ordermoeny;
                list.add(item);
            }
            adappter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            ordermoeny.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                Toashow.show(msg);
            }
            OnEorr();
        }
    };

    private MyListener myListener = new MyListener() {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            list.clear();
            totalPage = 0;
            adappter.notifyDataSetChanged();
            initData();

        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            initData();
        }
    };

}