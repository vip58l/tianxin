/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/23 0023
 */


package com.tianxin.activity.Withdrawal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.setAdapter;
import com.tianxin.Util.Constants;
import com.tencent.opensource.model.orderdetailed;
import com.tencent.opensource.model.item;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.pullableview.PullToRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_Recharge extends BasFragment {
    String TAG = activity_Recharge.class.getSimpleName();
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;
    @BindView(R.id.pullGridview)
    ListView listView;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_detailedlist, null);
    }

    @Override
    public void iniview() {
        refresh_view.setOnRefreshListener(new MyListener());
        listView.setAdapter(setAdapter = new setAdapter(context, list));
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.querdetailedlist(totalPage, type, paymnets);
    }

    @Override
    public void OnEorr() {
        if (refresh_view != null) {
            refresh_view.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    public static Fragment pagetype(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        activity_Recharge fragment = new activity_Recharge();
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                initData();
                break;

        }

    }

    /**
     * 下拉或上拉获取数据
     */
    class MyListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            totalPage = 0;
            list.clear();
            setAdapter.notifyDataSetChanged();
            initData();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            initData();

        }


    }

    @Override
    public void onRefresh() {

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
            ;
        }

        @Override
        public void onSuccess(Object object) {
            List<orderdetailed> data = (List<orderdetailed>) object;
            for (orderdetailed datum : data) {
                item item = new item();
                item.type = type == 1 ? com.tianxin.adapter.setAdapter.Recharge : com.tianxin.adapter.setAdapter.Recharge2;
                item.object = datum;
                list.add(item);
            }
            setAdapter.notifyDataSetChanged();
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
        public void ToKen(String msg) {
            activity_Recharge.super.paymnets.ToKen(msg);
        }
    };

}
