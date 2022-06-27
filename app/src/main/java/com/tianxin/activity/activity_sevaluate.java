package com.tianxin.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.widget.EndLessOnScrollListener;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.getevaluate;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 服务评价列表
 */
public class activity_sevaluate extends BasActivity2 implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "activity_sevaluate";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;


    @Override
    protected int getview() {

        return R.layout.activity_sevaluate;
    }

    @Override
    public void iniview() {
        getuserid = getIntent().getStringExtra(Constants.USERID);
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg122));
        itemback.setHaidtopBackgroundColor(true);
        radapter = new Radapter(this, list, Radapter.activity_sevaluate);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int position) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter);
        recyclerview.addOnScrollListener(new EndLessOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                initData();
            }
        });
    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        totalPage++;
        PostModule.getModule(BuildConfig.HTTP_API + "/getevaluate?userid=" + getuserid + "&page=" + totalPage + "&token=" + userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<getevaluate> mydate = JsonUitl.stringToList(date, getevaluate.class);
                    if (mydate.size() == 0) {
                        totalPage--;
                        if (totalPage > 0) {
                            Toashow.show(getString(R.string.eorrtext));
                        }
                        OnEorr();
                        return;
                    }

                    list.addAll(mydate);
                    radapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                OnEorr();
            }

            @Override
            public void fall(int code) {
                OnEorr();
            }
        });
    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                totalPage = 0;
                initData();
                break;
        }

    }

    @Override
    public void OnEorr() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
        totalPage = 0;
        list.clear();
        radapter.notifyDataSetChanged();
        initData();
    }
}