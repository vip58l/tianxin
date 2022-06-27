package com.tencent.liteav;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;

import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.liteav.basic.AvatarConstant;
import com.tencent.liteav.basic.UserModel;
import com.tencent.liteav.basic.UserModelManager;
import com.tencent.liteav.trtcvoiceroom.R;
import com.tencent.liteav.trtcvoiceroom.model.TRTCVoiceRoom;
import com.tencent.liteav.trtcvoiceroom.model.VoiceRoomManager;
import com.tencent.liteav.trtcvoiceroom.ui.room.VoiceRoomAudienceActivity;
import com.tencent.liteav.trtcvoiceroom.ui.room.VoiceRoomCreateDialog;
import com.tencent.opensource.model.LiveRoom;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.trtc.TRTCCloudDef;

import java.util.Random;

public class TUIVoiceRoomLogin {
    private String[] ROOM_COVER_ARRAY;
    private String TAG = TUIVoiceRoomLogin.class.getSimpleName();
    private Context context;

    public TUIVoiceRoomLogin(Context context) {
        this.context = context;
    }

    /**
     * 创建聊天室
     */
    public void createRoom() {
        ROOM_COVER_ARRAY = context.getResources().getStringArray(R.array.array_img);
        int index = new Random().nextInt(ROOM_COVER_ARRAY.length);
        String coverUrl = ROOM_COVER_ARRAY[index]; //封面背景
        String userName = UserModelManager.getInstance().getUserModel().userName;
        String userId = UserModelManager.getInstance().getUserModel().userId;
        String roomName = String.format(context.getString(R.string.trtcvoiceroom_create_theme) + "", userName);
        VoiceRoomCreateDialog dialog = new VoiceRoomCreateDialog(context);
        dialog.showVoiceRoomCreateDialog(userId, userName, coverUrl, TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT, true);
    }

    /**
     * 进入聊天室成功
     *
     * @param roomIdStr
     */
    public void enterRoom(final String roomIdStr, LiveRoom liveRoom) {
        member member = liveRoom.getMember();
        VoiceRoomManager.getInstance().getGroupInfo(roomIdStr, new VoiceRoomManager.GetGroupInfoCallback() {
            @Override
            public void onSuccess(V2TIMGroupInfoResult result) {
                if (isRoomExist(result)) {
                    realEnterRoom(roomIdStr);
                } else {
                    ToastUtils.showLong(String.format("无法进入【%s】的房间 主播暂时离开", member.getTruename()));
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                ToastUtils.showLong(msg);
            }
        });
    }

    /**
     * 进入房间（听众调用）
     *
     * @param roomIdStr
     */
    private void realEnterRoom(String roomIdStr) {
        UserModel userModel = UserModelManager.getInstance().getUserModel();
        String userId = userModel.userId;
        int roomId;
        try {
            roomId = Integer.parseInt(roomIdStr);
        } catch (Exception e) {
            roomId = 10000;
        }
        VoiceRoomAudienceActivity.enterRoom(context, roomId, userId, TRTCCloudDef.TRTC_AUDIO_QUALITY_DEFAULT);
    }

    /**
     * 保存登录用户头像信息等内容
     */
    public void setSelfProfile(TRTCVoiceRoom mTRTCVoiceRoom) {
        com.tencent.liteav.basic.UserModel userModel = new com.tencent.liteav.basic.UserModel();
        userModel.userId = UserInfo.getInstance().getUserId();
        userModel.userName = UserInfo.getInstance().getName();
        int index = new Random().nextInt(AvatarConstant.USER_AVATAR_ARRAY.length);
        userModel.userAvatar = TextUtils.isEmpty(UserInfo.getInstance().getAvatar()) ? AvatarConstant.USER_AVATAR_ARRAY[index] : UserInfo.getInstance().getAvatar();
        userModel.userSig = UserInfo.getInstance().getUserSig();
        UserModelManager manager = UserModelManager.getInstance();
        manager.setUserModel(userModel);

        //设置名称和头像
        mTRTCVoiceRoom.setSelfProfile(userModel.userName, userModel.userAvatar, null);
    }

    /**
     * 判断房间号是否空值
     *
     * @param result
     * @return
     */
    private boolean isRoomExist(V2TIMGroupInfoResult result) {
        if (result == null) {
            Log.e(TAG, "没有房间号");
            return false;
        }
        return result.getResultCode() == 0;
    }

}
