/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/23 0023
 */


package com.tianxin.Fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.activity.video.videoijkplayer4;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.widget.EndLessOnScrollListener;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.curriculum;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课程列有
 */
public class fragmnet_coulmn extends BasFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.itemback)
    public itembackTopbr itemback;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    UserInfo userInfo;
    List<Object> list = new ArrayList<>();
    Radapter radapter;
    int totalPage;
    String getuserid;
    int TYPE;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        TYPE = arguments.getInt(Constants.TYPE);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_list_column, null);
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg5));
        userInfo = UserInfo.getInstance();
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), TYPE);
        RecyclerView.LayoutManager layout = TYPE > 1 ? gridLayoutManager : manager;
        radapter = new Radapter(getContext(), list, TYPE > 1 ? Radapter.activity_list_column2 : Radapter.activity_list_column1);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void onItemClick(View view, int position) {
                curriculum curriculum = (curriculum) list.get(position);
                if (curriculum == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), videoijkplayer4.class);
                intent.putExtra(Constants.ROOM, curriculum);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerview.setLayoutManager(layout);
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
        PostModule.getModule(BuildConfig.HTTP_API + "/curriculum?page=" + totalPage, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<curriculum> curriculaall = JsonUitl.stringToList(date, curriculum.class);
                    if (curriculaall.size() == 0) {
                        totalPage--;
                        if (totalPage > 1) {
                            Toashow.show(getString(R.string.eorrtext));
                        }
                        OnEorr();
                        return;
                    }
                    list.addAll(curriculaall);
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
        list.clear();
        totalPage = 0;
        initData();
    }
}
