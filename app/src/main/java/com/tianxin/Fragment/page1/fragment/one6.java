package com.tianxin.Fragment.page1.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.party.activity_party;
import com.tianxin.activity.party.activity_party_imge;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Party;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 聚会列表
 */
public class one6 extends BasFragment {
    private static final String TAG = one6.class.getName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    boolean menuVisible = false;

    public static one6 show(int type) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, type);
        one6 one6 = new one6();
        one6.setArguments(args);
        return one6;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.menuVisible = menuVisible;
        if (menuVisible) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iniload();
                }
            }, 200);
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home_name_one6, null);
    }

    @Override
    public void iniview() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(radapter = new Radapter(context, list2, Radapter.one6, callback));
        recyclerView.setBackgroundColor(getResources().getColor(R.color.home3));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list2.clear();
                radapter.notifyDataSetChanged();
                iniload();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                iniload();
            }
        });

    }

    @Override
    public void initData() {


    }

    private void iniload() {
        totalPage++;
        datamodule.partylist(totalPage, 0, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                OnEorr();
            }

            @Override
            public void onFail(String msg) {
                if (totalPage > 1) {
                    Toashow.show(msg);
                }
                OnEorr();
            }

            @Override
            public void onSuccess(Object object) {
                List<Party> parties = (List<Party>) object;
                list2.addAll(parties);
                radapter.notifyDataSetChanged();
                totalPage++;
                OnEorr();

            }

            @Override
            public void onFail() {
                OnEorr();
            }
        });
    }

    @Override
    @OnClick({R.id.send, R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                activity_party.setAction(context);
                break;
            case R.id.eorr:
                smartRefreshLayout.setVisibility(View.VISIBLE);
                initData();
                break;
        }

    }

    @Override
    public void OnEorr() {
        totalPage--;
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    @Override
    public void onRefresh() {

    }

    public Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            Party party = (Party) list2.get(position);
            activity_party_imge.setAction(context, party, 1);
        }

        @Override
        public void onFall() {

        }
    };


}
