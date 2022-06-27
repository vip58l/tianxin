package com.tianxin.IMtencent.scenes.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tianxin.IMtencent.scenes.net.RoomManager;
import com.tianxin.R;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;

public class LiveRoomAnchorActivity2 extends AppCompatActivity implements TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate {


    private TUILiveRoomAnchorLayout mLayoutTuiLiverRomAnchor;

    public static void start(Context context, String groupID) {
        Intent starter = new Intent(context, LiveRoomAnchorActivity2.class);
        if (context instanceof Application) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        starter.putExtra(RoomManager.GROUP_ID, groupID);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room_anchor2);
        mLayoutTuiLiverRomAnchor = findViewById(R.id.tui_liveroom_anchor_layout);
        mLayoutTuiLiverRomAnchor.setLiveRoomAnchorLayoutDelegate(this);
        mLayoutTuiLiverRomAnchor.initWithRoomId(getSupportFragmentManager(), 12369857);
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int errorCode, String errorMsg) {

    }

    @Override
    public void onRoomCreate(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {

    }

    @Override
    public void onRoomDestroy(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {

    }

    @Override
    public void getRoomPKList(TUILiveRoomAnchorLayout.OnRoomListCallback callback) {

    }
}