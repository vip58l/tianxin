package com.tianxin.activity.party;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.partyname;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已报名列表会员
 */
public class activity_party_user_list extends BasActivity2 {
    private static final String TAG = activity_party_user_list.class.getName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv3title)
    TextView tv3title;
    String id;

    public static void setAction(Context context, String id) {
        Intent intent = new Intent();
        intent.setClass(context, activity_party_user_list.class);
        intent.putExtra(Constants.id, id);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_party_user_list;
    }

    @Override
    public void iniview() {
        id = getIntent().getStringExtra(Constants.id);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.party_item01, callback));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list.clear();
                radapter.notifyDataSetChanged();
                initData();
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
        totalPage++;
        datamodule.partynameUser(totalPage, id, new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                List<partyname> partynames = (List<partyname>) object;
                list.addAll(partynames);
                radapter.notifyDataSetChanged();
            }

            @Override
            public void onFail(String msg) {
                Toashow.show(msg);
                OnEorr();
            }

            @Override
            public void isNetworkAvailable() {
                OnEorr();
            }

        });
    }

    @Override
    @OnClick({R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv3title:

                break;
        }
    }

    @Override
    public void OnEorr() {
        totalPage--;
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }

    }

    public Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            partyname partyname = (partyname) list.get(position);
            member = partyname.getMember();
            startChatActivity();
        }

        @Override
        public void onFall() {
            Callback.super.onFall();
        }
    };


    /**
     * 转到对应用户聊天界面
     */
    public void startChatActivity() {

        if (member == null) {
            Toashow.show(getString(R.string.eorrfali));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(String.valueOf(member.getId()));
        chatInfo.setChatName(member.getTruename());
        chatInfo.setIconUrlList(TextUtils.isEmpty(this.member.getPicture()) ? "" : member.getPicture());
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}