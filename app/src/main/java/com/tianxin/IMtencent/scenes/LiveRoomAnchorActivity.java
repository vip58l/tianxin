package com.tianxin.IMtencent.scenes;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.scenes.net.HeartbeatManager;
import com.tianxin.IMtencent.scenes.net.RoomManager;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAnchorLayout;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.message.LiveMessageInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

/**
 * 主播创建房间
 */
public class LiveRoomAnchorActivity extends BaseActivity {
    private final String TAG = LiveRoomAnchorActivity.class.getSimpleName();
    private TUILiveRoomAnchorLayout mLayoutTuiLiverRomAnchor;
    private String mGroupID;
    private Datamodule datamodule;

    public static void start(Context context, String groupID) {
        Intent intent = new Intent(context, LiveRoomAnchorActivity.class);
        if (context instanceof Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(RoomManager.GROUP_ID, groupID);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        datamodule = new Datamodule(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }
        setContentView(R.layout.test_activity_live_room_anchor);

        mGroupID = getIntent().getStringExtra(RoomManager.GROUP_ID);     //直播间号
        mLayoutTuiLiverRomAnchor = findViewById(R.id.tui_liveroom_anchor_layout);

        //初始化生成主播房间号ID
        int roomId = getRoomId();

        //传入系统初始化评论内容
        String msg = Config_Msg.getInstance().getMsg();
        mLayoutTuiLiverRomAnchor.setMsg(msg);
        mLayoutTuiLiverRomAnchor.setLiveRoomAnchorLayoutDelegate(Callback); //回调接口
        mLayoutTuiLiverRomAnchor.initWithRoomId(getSupportFragmentManager(), roomId);
        mLayoutTuiLiverRomAnchor.enablePK(TextUtils.isEmpty(mGroupID));   //是否开启pk

        //初始化数据init
        //mLayoutTuiLiverRomAnchor.init(msg,Callback,getSupportFragmentManager(), roomId,TextUtils.isEmpty(mGroupID));
    }

    /**
     * 返回对象的哈希码值 群主或房间号
     *
     * @return
     */
    private int getRoomId() {
        String ownerId = V2TIMManager.getInstance().getLoginUser();
        return TextUtils.isEmpty(mGroupID) ? (mGroupID + ownerId + "liveRoom").hashCode() & 0x7FFFFFFF : mGroupID.hashCode() & 0x7FFFFFFF;
    }

    @Override
    public void onBackPressed() {
        mLayoutTuiLiverRomAnchor.onBackPress();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //系统默认 未开启调用该接口会强制停止直播
        if (mLayoutTuiLiverRomAnchor != null) {
            //mLayoutTuiLiverRomAnchor.unInit();
        }
    }

    /**
     * IM通信消息发送 我开直播啦 快来围观
     *
     * @param roomInfo
     * @param roomStatus
     */
    private void sendLiveGroupMessage(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int roomStatus) {
        LiveMessageInfo liveMessageInfo = new LiveMessageInfo();
        liveMessageInfo.version = 1;
        liveMessageInfo.roomId = roomInfo.roomId;
        liveMessageInfo.roomName = roomInfo.roomName;
        liveMessageInfo.roomType = RoomManager.TYPE_LIVE_ROOM;
        liveMessageInfo.roomCover = roomInfo.coverUrl;
        liveMessageInfo.roomStatus = roomStatus;
        liveMessageInfo.anchorId = roomInfo.ownerId;
        liveMessageInfo.anchorName = roomInfo.ownerName;

        //IM通信消息发送 我开直播啦 快来围观
        GroupChatManagerKit.getInstance().sendLiveGroupMessage(mGroupID, liveMessageInfo, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.d(TAG, "onSuccess: 群直播发送成功");
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.d(TAG, "module=" + module + "errcode=" + errCode + "errMsg=" + errMsg);

            }
        });
    }

    /**
     * 直播房间创建开播了
     */
    public void livecreate(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int type) {
        if (roomInfo != null) {
            if (roomInfo.roomId > 0) {
                datamodule.livecreate(roomInfo, type);
            } else {
                Toashow.show("无法获取直播间号");
            }

        }
    }

    /**
     * 回调接口
     */
    private TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate Callback = new TUILiveRoomAnchorLayout.TUILiveRoomAnchorLayoutDelegate() {
        @Override
        public void onClose() {
            finish();
        }

        @Override
        public void onError(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo, int errorCode, String errorMsg) {
            Log.d(TAG, "onError: " + roomInfo.toString());
            ToastUtil.toastLongMessage("错误" + errorCode + "|" + errorMsg);
        }

        @Override
        public void onRoomCreate(final TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
            String type = RoomManager.TYPE_LIVE_ROOM;

            if (!TextUtils.isEmpty(mGroupID)) {
                sendLiveGroupMessage(roomInfo, 1); //开启直播群发送
                type = RoomManager.TYPE_GROUP_LIVE;
            }

            final String finalType = type;
            RoomManager.getInstance().createRoom(roomInfo.roomId, finalType, new RoomManager.ActionCallback() {
                @Override
                public void onSuccess() {
                    HeartbeatManager.getInstance().start(finalType, roomInfo.roomId);
                    Log.d(TAG, "onSuccess: 创建房间" + roomInfo.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            livecreate(roomInfo, 1); //记录创建房间
                        }
                    });
                }

                @Override
                public void onFailed(int code, String msg) {

                }
            });
        }

        @Override
        public void onRoomDestroy(TRTCLiveRoomDef.TRTCLiveRoomInfo roomInfo) {
            String type = RoomManager.TYPE_LIVE_ROOM;
            if (!TextUtils.isEmpty(mGroupID)) {
                sendLiveGroupMessage(roomInfo, 0); //关闭直播群通知
                type = RoomManager.TYPE_GROUP_LIVE;
            }
            RoomManager.getInstance().destroyRoom(roomInfo.roomId, type, null);
            HeartbeatManager.getInstance().stop(); //停止推流
            Log.d(TAG, "onRoomDestroy: 销毁房间" + roomInfo.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    livecreate(roomInfo, 2); //记录销毁房间
                }
            });

        }

        @Override
        public void getRoomPKList(final TUILiveRoomAnchorLayout.OnRoomListCallback callback) {
            RoomManager.getInstance().getRoomList(RoomManager.TYPE_LIVE_ROOM, new RoomManager.GetRoomListCallback() {
                @Override
                public void onSuccess(List<String> roomIdList) {
                    if (callback != null) {
                        callback.onSuccess(roomIdList);
                    }
                }

                @Override
                public void onFailed(int code, String msg) {
                    if (callback != null) {
                        callback.onFailed();
                    }
                }
            });
        }

    };

}
