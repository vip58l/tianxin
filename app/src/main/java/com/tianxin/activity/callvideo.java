package com.tianxin.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;
import java.util.Map;

public class callvideo extends BasActivity2 {
    private static final String TAG = "callvideo";
    private ITRTCAVCall mITRTCAVCall;
    EditText userid;

    /**
     * 拨号的回调监听
     */
    private final TRTCAVCallListener mTRTCAudioCallListener = new TRTCAVCallListener() {


        @Override
        public void onError(int code, String msg) {
            //发生了错误，报错并退出该页面
            ToastUtil.toastLongMessage(getString(com.tencent.qcloud.tim.uikit.R.string.error) + "[" + code + "]:" + msg);
            Log.d(TAG, "onError: " + code + " " + msg);
            finishActivity();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {
            Log.d(TAG, "被邀请通话回调: " + sponsor + " " + userIdList + " " + isFromGroup + " " + callType);
        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {
            Log.d(TAG, "正在IM群组通话时，如果其他与会者邀请他人，会收到此回调: ");
        }

        @Override
        public void onUserEnter(final String userId) {
            Log.d(TAG, "onUserEnter: 进入通话的用户" + userId);

        }

        @Override
        public void onUserLeave(final String userId) {
            Log.d(TAG, "onUserLeave: 离开通话的用户" + userId);

        }

        @Override
        public void onReject(final String userId) {
            Log.d(TAG, "onReject: 拒绝通话的用户" + userId);

        }

        @Override
        public void onNoResp(final String userId) {
            Log.d(TAG, "onNoResp: 无人应答的回调" + userId);

        }

        @Override
        public void onLineBusy(String userId) {
            Log.d(TAG, "onLineBusy: 邀请方忙线");

        }

        @Override
        public void onCallingCancel() {
            Log.d(TAG, "onCallingCancel: 作为被邀请方会收到，收到该回调说明本次通话被取消了");

            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            Log.d(TAG, "onCallingTimeout: 作为被邀请方会收到，收到该回调说明本次通话超时未应答");

            finishActivity();
        }

        @Override
        public void onCallEnd() {
            Log.d(TAG, "onCallEnd: 收到该回调说明本次通话结束了");
            finishActivity();
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, "onUserAudioAvailable: true:远端用户打开摄像头  false:远端用户关闭摄像头");
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, "onUserVideoAvailable: true:远端用户打开麦克风  false:远端用户关闭麦克风");
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
            }
        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {

        }

        @Override
        public void longrangecall(String json) {

        }
    };


    @Override
    protected int getview() {
        return R.layout.main_item_video;
    }

    @Override
    public void iniview() {
        userid = findViewById(R.id.userid);
        findViewById(R.id.button).setOnClickListener(v -> tostattitiviey());

    }


    @Override
    public void initData() {
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(context);
        mITRTCAVCall.addListener(mTRTCAudioCallListener); //加入监听事件
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }


    public void tostattitiviey() {


    }


    /**
     * 主动拨打给某个用户
     */
    public static void startCallSomeone(Context context) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, callvideo.class);
        context.startActivity(starter);
    }

    /**
     * 作为用户被叫
     */
    public static void startBeingCall(Context context) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, callvideo.class);
        context.startActivity(starter);
    }

    /**
     * 关闭activity
     */
    private void finishActivity() {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }


    @Override
    public void onBackPressed() {
        // 退出这个界面的时候，需要挂断
        mITRTCAVCall.hangup();//当您处于通话中，可以调用该函数结束通话
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
