package com.tencent.liteav;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;

import java.util.List;
import java.util.Map;

import static com.blankj.utilcode.util.ServiceUtils.startService;
import static com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity.PARAM_BEINGCALL_USER;
import static com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity.PARAM_OTHER_INVITING_USER;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_TYPE;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.TYPE_BEING_CALLED;

/**
 * 管理监听音视频通话弹出
 */
public class AVCallManager {
    private final String TAG = "AVCallManager";

    private static class AVCallManagerHolder {
        private static AVCallManager avCallManager = new AVCallManager();
    }

    public static AVCallManager getInstance() {
        return AVCallManager.AVCallManagerHolder.avCallManager;
    }

    private Context mContext;
    private ITRTCAVCall mITRTCAVCall;
    private final TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        // <editor-fold  desc="视频监听代码">
        @Override
        public void onError(int code, String msg) {
        }

        @Override
        public void onInvited(String sponsor, final List<String> userIdList, boolean isFromGroup, final int callType) {
            //处理视频或语音通话弹出activity
            processInvite(sponsor, userIdList, callType);
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {

        }

        @Override
        public void onUserEnter(String userId) {

        }

        @Override
        public void onUserLeave(String userId) {

        }

        @Override
        public void onReject(String userId) {

        }

        @Override
        public void onNoResp(String userId) {

        }

        @Override
        public void onLineBusy(String userId) {

        }

        @Override
        public void onCallingCancel() {

        }

        @Override
        public void onCallingTimeout() {

        }

        @Override
        public void onCallEnd() {

        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {

        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {
        }

        @Override
        public void longrangecall(String json) {
            Log.d(TAG, "longrangecall: " + json);
            TheresaCall();            //收到点击或邀请消息

        }


        // </editor-fold  desc="视频监听代码">
    };

    /**
     * 先查询用户ID
     *
     * @param sponsor    呼听 发起人id
     * @param userIdList
     * @param callType
     */
    private void processInvite(String sponsor, final List<String> userIdList, final int callType) {
        ProfileManager.getInstance().getUserInfoByUserId(sponsor, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(final UserModel model) {
                if (userIdList == null || userIdList.size() == 0) {
                    switch (callType) {
                        case ITRTCAVCall.TYPE_AUDIO_CALL:
                            //打开语音通话
                            TRTCAudioCallActivity.startBeingCall(mContext, model, null);
                            break;
                        case ITRTCAVCall.TYPE_VIDEO_CALL:
                            //打开视频通话
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(TUIKit.getAppContext())) {
                                    //打开视频聊天窗口
                                    TRTCVideoCallActivity.startBeingCall(mContext, model, null);
                                } else {
                                    //服务弹窗悬浮提示
                                    FloatingService.startBeingCall(mContext, model);
                                }
                            } else {
                                //服务弹窗悬浮提示
                                FloatingService.startBeingCall(mContext, model);
                            }
                            break;
                        case ITRTCAVCall.TYPE_AGORA_CALL:
                            //打开声网通话
                            CallVideo(mContext, model);
                            break;
                    }
                } else {
                    //重新再查询数据
                    selectUserModel(model, userIdList, callType);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                TUIKitLog.e(TAG, "getUserInfoByUserId failed:" + code + ", desc:" + msg);
            }
        });
    }

    /**
     * 重新再查询数据
     *
     * @param model
     * @param userIdList
     * @param callType
     */
    private void selectUserModel(final UserModel model, final List<String> userIdList, final int callType) {
        ProfileManager.getInstance().getUserInfoBatch(userIdList, new ProfileManager.GetUserInfoBatchCallback() {
            @Override
            public void onSuccess(List<UserModel> modelList) {
                if (callType == ITRTCAVCall.TYPE_VIDEO_CALL) {
                    //打开视频通话
                    TRTCVideoCallActivity.startBeingCall(mContext, model, modelList);
                } else if (callType == ITRTCAVCall.TYPE_AUDIO_CALL) {
                    //打开语音通话
                    TRTCAudioCallActivity.startBeingCall(mContext, model, modelList);

                } else if (callType == ITRTCAVCall.TYPE_AGORA_CALL) {
                    //打开声网通话
                    CallVideo(mContext, model);
                } else {
                    //打开语音通话
                    TRTCAudioCallActivity.startBeingCall(mContext, model, modelList);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                TUIKitLog.e(TAG, "getUserInfoBatch failed:" + code + ", desc:" + msg);
            }
        });
    }

    private AVCallManager() {
    }

    public void init(Context context) {
        mContext = context;
        initVideoCallData();
    }

    public void unInit() {
        if (mITRTCAVCall != null) {
            mITRTCAVCall.removeListener(mTRTCAVCallListener);
        }
        TRTCAVCallImpl.destroySharedInstance();
    }

    /**
     * 初始化数据
     */
    private void initVideoCallData() {
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(mContext);
        mITRTCAVCall.init();
        mITRTCAVCall.addListener(mTRTCAVCallListener);
        int appid = TUIKitConfigs.getConfigs().getGeneralConfig().getSDKAppId();
        String userId = ProfileManager.getInstance().getUserModel().userId;
        String userSig = ProfileManager.getInstance().getUserModel().userSig;
        mITRTCAVCall.login(appid, userId, userSig, null);
    }

    /**
     * 邀请1V1视频聊天界面声网页
     *
     * @param model
     */
    private static void CallVideo(Context context, UserModel model) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent intent = new Intent();
        intent.setAction("com.paixide.activity.video.RecyclerVideo.callmoden.CallVideo");
        intent.putExtra(PARAM_BEINGCALL_USER, model);   //邀请者信息
        intent.putExtra(PARAM_TYPE, TYPE_BEING_CALLED); // 被叫方
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 弹出邀请人视频通话
     */
    private static void TheresaCall() {
        Intent intent = new Intent();
        intent.setAction("TheresaCall");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TUIKit.getAppContext().startActivity(intent);
    }


}
