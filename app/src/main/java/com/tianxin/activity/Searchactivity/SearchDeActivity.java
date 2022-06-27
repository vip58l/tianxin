package com.tianxin.activity.Searchactivity;

import android.content.Intent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tianxin.activity.video.videoijkplayer1;
import com.tianxin.adapter.setAdapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tencent.opensource.model.item;
import com.tianxin.Util.StatusBarUtil;

import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.pullableview.PullToRefreshLayout;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//查找结果
public class SearchDeActivity extends BasActivity2 implements AdapterView.OnItemClickListener, PullToRefreshLayout.OnRefreshListener {
    private final List<item> list = new ArrayList<>();
    private final String TAG = SearchDeActivity.class.getSimpleName();
    private String selectkey;
    @BindView(R.id.mhde_tv_title)
    TextView mhde_tv_title;
    @BindView(R.id.pullListView)
    ListView pullListView;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refresh_view;

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(activity, getResources().getColor(R.color.c_main));
        return R.layout.activity_search_list;
    }

    @Override
    public void iniview() {
        refresh_view.setOnRefreshListener(this);
        adappter = new setAdapter(context, list);
        pullListView.setAdapter(adappter);
        pullListView.setOnItemClickListener(this);
        String[] titles = getIntent().getStringArrayExtra(Constants.TITLE);
        int position = getIntent().getIntExtra(Constants.POSITION, 0);
        selectkey = titles[position];
    }

    @Override
    public void initData() {
        getdatehttp();
    }

    @Override
    public void OnEorr() {
        if (refresh_view != null) {
            refresh_view.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick({R.id.se_img_back, R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.se_img_back:
                finish();
                break;
            case R.id.eorr:
                sonRefresh();
                break;
        }
    }

    public void getdatehttp() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(this, getString(R.string.isNetworkAvailable));
            finish();
            return;
        }
        totalPage++;
        datamodule.selectvideolist(selectkey, totalPage, paymnets);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, videoijkplayer1.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.TOTALPAGE, totalPage);
        intent.putExtra(Constants.TYPE, 0);
        intent.putExtra(Constants.dadelist, (Serializable) list);
        startActivity(intent);
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        sonRefresh();
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        getdatehttp();
    }

    private void sonRefresh() {
        totalPage = 0;
        list.clear();
        adappter.notifyDataSetChanged();
        refresh_view.setVisibility(View.VISIBLE);
        getdatehttp();
    }

    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void ToKen(String msg) {
            SearchDeActivity.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            Toashow.show(msg);
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> data = (List<videolist>) object;
            for (videolist datum : data) {
                item item = new item();
                item.type = setAdapter.booklist;
                item.object = datum;
                list.add(item);
            }
            adappter.notifyDataSetChanged();
            mhde_tv_title.setText(String.format("搜索结果(%s)", adappter.getCount()));
            OnEorr();
        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
        }
    };
}
