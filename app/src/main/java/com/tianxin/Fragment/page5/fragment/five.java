package com.tianxin.Fragment.page5.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;

import butterknife.BindView;

/**
 * H漫画
 */
public class five extends BasFragment {
    @BindView(R.id.pullToRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.pullListView)
    ListView pullListView;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_five, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }


}
