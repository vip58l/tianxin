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
import com.tencent.opensource.model.item;
import com.tianxin.Module.api.order;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.adapter.setAdapter;
import com.tianxin.listener.Paymnets;
import com.tianxin.pullableview.PullToRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 充值明细
 */
public class Rechargeorder extends BasFragment {
    String TAG = Rechargeorder.class.getSimpleName();
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
        refresh_view.setOnRefreshListener(pullToRefreshLayout);
        listView.setAdapter(setAdapter = new setAdapter(context, list));
    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.getorderidlist(totalPage, type, paymnets);
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
        Rechargeorder fragment = new Rechargeorder();
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
    private PullToRefreshLayout.OnRefreshListener pullToRefreshLayout=new  PullToRefreshLayout.OnRefreshListener (){
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


    };

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
        }

        @Override
        public void onSuccess(Object object) {
            List<order> data = (List<order>) object;
            for (order datum : data) {
                item item = new item();
                item.type = setAdapter.Recharge3;
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
            Rechargeorder.super.paymnets.ToKen(msg);
        }
    };

}
