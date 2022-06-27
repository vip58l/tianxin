package com.tianxin.Fragment.page3.fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.game.Game_Activity;
import com.tianxin.activity.game.Game_bin;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Gamelist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 游戏陪玩
 */
public class page3_3 extends BasFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    boolean isVisibleToUser;

    public static page3_3 show() {
        page3_3 page3_3 = new page3_3();
        return page3_3;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser && list2.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.autoRefresh();
                }
            }, 100);
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.layout_item_page3, null);
    }

    @Override
    public void iniview() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list2, radapter.gamelist, callback));
        recyclerview.addItemDecoration(new MyDecoration(context, 0));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                initData();
            }
        });

    }

    @Override
    public void initData() {
        if (!isVisibleToUser) {
            return;
        }
        totalPage++;
        datamodule.gamelist(totalPage, paymnets, false);
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                loadMoreData();
                break;
        }

    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onRefresh() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Object object) {
            List<Gamelist> gamelists = (List<Gamelist>) object;
            list2.addAll(gamelists);
            radapter.notifyDataSetChanged();
            OnEorr();
        }


        @Override
        public void onSuccess(String msg) {
            if (totalPage > 1) {
                totalPage--;
                Toashow.show(msg);
            }
            OnEorr();
        }


    };

    private Callback callback = new Callback() {
        @Override
        public void LongClickListener(int position) {
            Gamelist gamelist = (Gamelist) list2.get(position);
            tostartActivity(Game_bin.class, gson.toJson(gamelist));
        }

        @Override
        public void OnClickListener(int position) {
            Gamelist gamelist = (Gamelist) list2.get(position);
            tostartActivity(Game_Activity.class, gson.toJson(gamelist));
        }
    };

}
