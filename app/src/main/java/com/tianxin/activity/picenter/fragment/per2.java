package com.tianxin.activity.picenter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.adapter.Radapter;
import com.tianxin.R;

import butterknife.BindView;

/**
 * 动态
 */
public class per2 extends BasFragment {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.per2_item_view, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        radapter = new Radapter(context, list2, Radapter.per3);
        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerview.setAdapter(radapter);
    }

    @Override
    public void initData() {
        OnEorr();
    }

    @Override
    public void OnEorr() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setVisibility(list2.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {

    }


    public static per2 perview() {
        Bundle args = new Bundle();
        per2 per2 = new per2();
        per2.setArguments(args);
        return per2;
    }
}
