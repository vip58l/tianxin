package com.tianxin.activity.game;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Gametitle;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 游戏列表
 */
public class Game_Activity_title extends BasActivity2 {
    private static final String TAG = Game_Activity_title.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.backtitle)
    Backtitle backtitle;

    @Override
    protected int getview() {
        return R.layout.activity_game_title;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tm21));
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.Activity_title, callback));
        //recyclerview.addItemDecoration(new MyDecoration(context, 0));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                initData();
            }
        });

    }

    @Override
    public void initData() {
        totalPage++;
        datamodule.gametitle(totalPage, paymnets);
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                break;
        }
    }

    @Override
    public void OnEorr() {
        refreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(R.string.eorrfali2);
            onError();

        }

        @Override
        public void onFail() {
            Toashow.show(R.string.eorrfali3);
            onError();
        }

        @Override
        public void onSuccess(Object object) {
            List<Gametitle> gametitles = (List<Gametitle>) object;
            list.addAll(gametitles);
            radapter.notifyDataSetChanged();
            onError();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                Toashow.show(msg);
            }
            onError();
        }
    };

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            Object o = list.get(position);
            Gametitle gametitle = (Gametitle) o;
            Intent date = new Intent();
            date.putExtra(Constants.JSON, gson.toJson(gametitle));
            date.putExtra(Constants.Edit, o.toString());
            setResult(Constants.REQUEST_CODE, date);
            finish();
        }

        @Override
        public void onSuccess() {

        }
    };

}