/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.Fragment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.activity_svip;
import com.tianxin.adapter.setAdapter;
import com.tianxin.dialog.dialog_msg_svip;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.R;
import com.tianxin.activity.activity_home_page;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我关注的列表
 */
public class Fromnetfollowlist extends BasFragment implements AdapterView.OnItemClickListener {
    private static final String TAG = Fromnetfollowlist.class.getSimpleName();
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.listview)
    public ListView listview;

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (isInMultiWindowMode) {
            Log.d(TAG, "onMultiWindowModeChanged: 多窗口模式已更改");
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.indexlay_followlist, null);
    }

    public static Fragment args(int TYPE) {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE, TYPE);
        Fromnetfollowlist fragment = new Fromnetfollowlist();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        item item = list.get(position);
        member member = ((followlist) item.object).getMember();
        if (member == null) {
            return;
        }
        if (userInfo.getVip() == 0) {
            dialog_msg_svip.dialogmsgsvip(context, getString(R.string.dialog_msg_svip), getString(R.string.tv_msg228), getString(R.string.tv_msg153), paymnets);
        } else {
            Intent intent = new Intent(getContext(), BuildConfig.TYPE == 1 ? activity_picenter.class : activity_home_page.class);
            intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
            startActivity(intent);
        }


    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                getdate();
                break;
        }
    }

    @Override
    public void iniview() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            TYPE = arguments.getInt(Constants.TYPE);
        }
        setAdapter = new setAdapter(context, list);
        listview.setAdapter(setAdapter);
        listview.setDividerHeight(1);
        listview.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                totalPage = 0;
                list.clear();
                setAdapter.notifyDataSetChanged();
                getdate();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
                getdate();
            }
        });
        if(getUserVisibleHint() && list.size() == 0) {
            smartRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRefresh() {

    }

    private void getdate() {
        totalPage++;
        datamodule.listowlist(totalPage, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<followlist> listm = (List<followlist>) object;
            for (followlist f : listm) {
                item item = new item();
                item.type = com.tianxin.adapter.setAdapter.followlist;
                item.object = f;
                member member = f.getMember();
                if (member != null) {
                    list.add(item);
                }
            }
            setAdapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void fall(int code) {
            Toashow.show(getString(R.string.eorrfali3));
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                Toashow.show(msg);
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            Fromnetfollowlist.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void onRefresh() {
            McallBack.starsetAction(context);

        }

    };
}
