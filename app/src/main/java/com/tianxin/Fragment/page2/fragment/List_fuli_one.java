package com.tianxin.Fragment.page2.fragment;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.adapter.setAdapter;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.activity.video.live.LiveVideo;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;

import butterknife.BindView;

public class List_fuli_one extends BasFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.pullGridview)
    ListView pullGridview;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private View rootView;

    @Override
    public View getview(LayoutInflater inflater) {
        return   rootView = inflater.inflate(R.layout.fragment_fls,null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        pullGridview.setOnItemClickListener(this);
        pullGridview.setAdapter(setAdapter = new setAdapter(context, list));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }

            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(100/*,false*/);//传入false表示加载失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }

            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            if (data != null) {
                int position = data.getIntExtra("position", 0);
                pullGridview.setSelection(position);
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), LiveVideo.class);
        intent.putExtra("list", (Serializable) list);
        intent.putExtra("position", position);
        getActivity().startActivityForResult(intent, Config.sussess);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (rootView != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
    }

}
