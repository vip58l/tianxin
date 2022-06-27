package com.tianxin.activity.game;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Gamelist;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加游戏
 */
public class Game_Activity_list extends BasActivity2 {
    private static final String TAG = Game_Activity_list.class.getSimpleName();
    @BindView(R.id.refreshlayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    private boolean delete = false;

    @Override
    protected int getview() {
        return R.layout.activity_game_list;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tm23));
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.Activity_list_game, callback));
        recyclerview.addItemDecoration(new MyDecoration(context, 0));
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
        datamodule.gamelist(totalPage, paymnets, true);
    }

    @Override
    @OnClick({R.id.eorr, R.id.buutton, R.id.backright})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                loadMoreData();
                break;
            case R.id.buutton:
                if (userInfo.getState() == 3) {
                    Toashow.show(getString(R.string.tm50));
                    return;
                }
                tostartActivity(Game_Activity_add.class, 0, Constants.requestCode);
                break;
            case R.id.backright:
                if (!delete) {
                    delete = true;
                    backtitle.setBackright(getString(R.string.dialog_cols));
                } else {
                    delete = false;
                    backtitle.setBackright(getString(R.string.tv_msg78));
                }
                if (list.size() > 0) {
                    radapter.setDelshow(delete);
                }

                break;
        }
    }

    @Override
    public void OnEorr() {
        refreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        if (list.size() > 0) {
            backtitle.setBackright(getString(R.string.tv_msg78));
            backtitle.backright.setTextColor(getResources().getColor(R.color.colorAccent));
            radapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.REQUEST_CODE) {
            loadMoreData();
        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();

        }

        @Override
        public void onSuccess(Object object) {
            List<Gamelist> gamelists = (List<Gamelist>) object;
            list.addAll(gamelists);
            radapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {

                if (TextUtils.isEmpty(msg)) {
                    Toashow.show(getString(R.string.eorrtext));
                } else {
                    Toashow.show(msg);
                }
            }
            OnEorr();

        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();
        }
    };

    private Callback callback = new Callback() {
        @Override
        public void LongClickListener(int position) {
            Gamelist gamelist = (Gamelist) list.get(position);
            tostartActivity(Game_bin.class, userInfo.getUserId(), gson.toJson(gamelist));
        }

        @Override
        public void OnClickListener(int position) {
            Gamelist gamelist = (Gamelist) list.get(position);
            tostartActivity(Game_Activity.class, userInfo.getUserId(), gson.toJson(gamelist));
        }

        @Override
        public void OndeleteListener(int position) {
            Gamelist gamelist = (Gamelist) list.get(position);
            Paymnets delListener = new Paymnets() {
                @Override
                public void onSuccess(String msg) {
                    Toashow.show(msg);
                    radapter.removenotifyDate(position);
                }

                @Override
                public void success(String msg) {
                    Toashow.show(msg);
                }
            };
            datamodule.gamedelete(gamelist, delListener);
        }

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK && delete) {
            delete = false;
            backtitle.setBackright(getString(R.string.tv_msg78));
            radapter.setDelshow(delete);
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}