package com.tianxin.Fragment.page2.fragment;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.blankj.utilcode.util.StringUtils.getString;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.activity_svip;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.liteav.TUIVoiceRoomLogin;
import com.tencent.liteav.trtcvoiceroom.model.TRTCVoiceRoom;
import com.tencent.opensource.model.LiveRoom;
import com.tencent.qcloud.tim.uikit.TUIKit;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 语音聊天室列表
 */
public class LiveTuivoiceRoom extends BasFragment {
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    String TAG = LiveTuivoiceRoom.class.getSimpleName();
    TRTCVoiceRoom trtcVoiceRoom = TUIKit.getTrtcVoiceRoom(); //获取TUIKit 初始化配置

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fmessage_view_item, null);
    }

    @Override
    public void iniview() {
        tuiVoiceRoomLogin = new TUIVoiceRoomLogin(context);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list2, Radapter.LiveTuivoiceRoom, callback));
        recyclerview.addItemDecoration(new MyDecoration(context, 0));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
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
        totalPage++;
        datamodule.getLiveRoom(totalPage, paymnets);
        datamodule.getvip(null);//获取用户最新状态
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
            smartRefreshLayout.setVisibility(list2.size() == 0 ? View.GONE : View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {

    }

    public void createRoom() {
        //设置进入头像+名称
        tuiVoiceRoomLogin.setSelfProfile(trtcVoiceRoom);
        //主播语音号创建房间
        tuiVoiceRoomLogin.createRoom();
    }

    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            OnEorr();
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            totalPage--;
            OnEorr();
        }

        @Override
        public void onSuccess(Object object) {
            List<LiveRoom> liveRooms = (List<LiveRoom>) object;
            list2.addAll(liveRooms);
            radapter.setDelshow(type == 1 ? false : true);
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
    };

    public Paymnets paymnets2 = new Paymnets() {
        @Override
        public void onRefresh() {
            startActivity(new Intent(context, activity_svip.class));
        }

        @Override
        public void onSuccess() {
            McallBack.starsetAction(context);
        }
    };

    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void OnClickListener(int position) {
            //进入语音群聊
            LiveRoom liveRoom = (LiveRoom) list2.get(position);
            if (liveRoom != null) {
                int hashCode = String.valueOf(liveRoom.getUserid()).hashCode();
                //设置进入头像+名称
                tuiVoiceRoomLogin.setSelfProfile(trtcVoiceRoom);
                //观众进入房间
                tuiVoiceRoomLogin.enterRoom(String.valueOf(hashCode), liveRoom);
            }
        }
    };

}
