package com.tianxin.activity.video.Call;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Receiver.MyService;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tencent.liteav.model.ITRTCAVCall.TYPE_AGORA_CALL;
import static com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity.TYPE_BEING_CALLED;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_BEINGCALL_USER;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_GROUP_ID;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_OTHER_INVITING_USER;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_TYPE;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.PARAM_USER;
import static com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity.TYPE_CALL;

/**
 * 接听来电视频
 */
public class Call_video extends BasActivity2 {
    private static final String TAG = "CallVideo";
    private Vibrator mVibrator;
    private Ringtone mRingtone;
    private Call_View videoItemTr;
    private int mCallType;                                         //用于区分主叫和被叫使用
    private ITRTCAVCall mITRTCAVCall;
    private int mTimeCount;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;
    private Runnable mTimeRunnable;
    private UserModel mSelfModel;                                   // 被叫方
    private UserModel mSponsorUserModel;                            // 邀请呼叫方
    private List<UserModel> mCallUserModelList = new ArrayList<>(); // 被邀请呼叫方
    private List<UserModel> mOtherInvitingUserModelList;            // 被叫方
    private final Map<String, UserModel> mCallUserModelMap = new HashMap<>();
    private Allcharge allcharge;
    private String mGroupId;
    private final Paymnets paymest = new Paymnets() {
        @SuppressLint("MissingPermission")
        @Override
        public void onSuccess() {

            //dialogconfig.dialogshow(context, member); //提示金币不足
            //startActivity(new Intent(context, VideoChatViewActivity.class)); //转到1V1直播中

            //开始接听
            if (mITRTCAVCall != null) {
                mITRTCAVCall.accept();
                stopTimeCount();
            }

            //关闭震动
            if (mVibrator != null) {
                mVibrator.cancel();
            }

            //关闭铃声
            if (mRingtone != null) {
                mRingtone.stop();
            }
            //关闭当前页面
            finishActivity();
        }

        @Override
        public void onFail() {
            //当您作为被邀请方收到 {@link TRTCAVCallListener#onInvited } 的回调时，可以调用该函数拒绝来电
            mITRTCAVCall.reject();
            finishActivity();
        }
    };

    /**
     * 拨号的回调监听
     */
    private final TRTCAVCallListener mTRTCAudioCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            finishActivity();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {

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
            Log.d(TAG, "onCallingCancel: 作为被邀请方会收到，收到该回调说明本次通话被取消了");
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(com.tencent.qcloud.tim.uikit.R.string.cancle_calling));
            }
            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            Log.d(TAG, "onCallingTimeout: 作为被邀请方会收到，收到该回调说明本次通话超时未应答");
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(com.tencent.qcloud.tim.uikit.R.string.call_time_out));
            }
            finishActivity();
        }

        @Override
        public void onCallEnd() {
            finishActivity();
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

        }
    };

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, Call_video.class);
        starter.putExtra(PARAM_TYPE, TYPE_AGORA_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 主动拨打给某些用户
     * 群组功能
     *
     * @param context
     * @param models
     */
    public static void startCallSomePeople(Context context, List<UserModel> models, String groupId) {
        TUIKitLog.i(TAG, "startCallSomePeople");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, Call_video.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 作为用户被叫
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        TUIKitLog.i(TAG, "设置等待最后一个活动已完成");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, Call_video.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected int getview() {
        configgetWindow();
        return R.layout.activity_svideo;
    }

    @Override
    public void iniview() {
        videoItemTr = findViewById(R.id.video_item);
        allcharge = Allcharge.getInstance(); //音视频扣费限制

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED); //类型 用于区分主叫和被叫使用
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // 有些手机禁止后台启动Activity，但是有bug，比如一种场景：对方反复拨打并取消，三次以上极容易从后台启动成功通话界面，
            // 此时对方再挂断时，此通话Activity调用finish后，上一个从后台启动的Activity就会弹出。此时这个Activity就不能再启动。
            finishActivity();
            return;
        }

        //使用该功能，需要开启权限，鉴于您禁用相关权限，请手动设置开启权限
        PermissionUtils.checkPermission(context, Manifest.permission.CAMERA); //相机权限申请
        PermissionUtils.checkPermission(context, Manifest.permission.RECORD_AUDIO); //录音权限申请
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); //震动器
        mRingtone = RingtoneManager.getRingtone(context, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }
    }

    @Override
    public void initData() {
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAudioCallListener); //加入监听事件
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());

        // 初始化从外界获取的数据
        Intent intent = getIntent();
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);
        mSelfModel = ProfileManager.getInstance().getUserModel(); //获取邀请人的资料

        //这里用于区分被叫或主叫
        if (mCallType == TYPE_BEING_CALLED) {
            // 作为被叫(接收方) 获得邀情人的信息
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);

            //设置填充邀请方的名称头像等
            videoItemTr.setTag_name(TextUtils.isEmpty(mSponsorUserModel.userName) ? getString(R.string.tv_msg177) : mSponsorUserModel.userName);
            videoItemTr.setIcon(mSponsorUserModel.userAvatar);
            videoItemTr.setBgmp(mSponsorUserModel.userAvatar);
            videoItemTr.setPaymnes(paymest); //监听事件

            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            //mVibrator.vibrate(new long[]{0, 1000, 1000}, 0); //震动器
            mRingtone.play();    //铃声播放
        } else {
            //本人 拨打出去的（邀请呼叫方）
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList = params.mUserModels;
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                }
                mRingtone.play();    //铃声播放
                startInviting();
                showInvitingView();
            }
        }

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    /**
     * 应用运行时，保持不锁屏、全屏化
     */
    private void configgetWindow() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //在Activity中停止Service
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(DemoApplication.instance(), MyService.class));
        }

        //Android中如何禁止用户截屏
        Config.mysetFlags(this);

        //底部黑色按键
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    /**
     * 移动通知监听 停止
     */
    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
    }

    /**
     * 统计语音通话时长
     */
    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        getmTimeRunnable();
        //执行时间统计开启
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void getmTimeRunnable() {
        if (mTimeRunnable == null) {
            //NeW启动线程
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            videoItemTr.setTime(getShowTime(mTimeCount));
                        }
                    });

                    mTimeHandler.postDelayed(mTimeRunnable, 1000);
                }
            };


        }

    }

    /**
     * 通话时间转换
     *
     * @param count
     * @return
     */
    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
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
        //当您处于通话中，可以调用该函数结束通话
        mITRTCAVCall.hangup();
        super.onBackPressed();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mVibrator != null) {
            mVibrator.cancel();
        }

        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.removeListener(mTRTCAudioCallListener);
        }
        stopTimeCount();
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }

    }

    /**
     * 发起邀请好友视频
     */
    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        mITRTCAVCall.groupCall(list, TYPE_AGORA_CALL, mGroupId);
    }

    /**
     * 展示邀请对方头像列表
     */
    public void showInvitingView() {
        for (UserModel userModel : mCallUserModelList) {
            videoItemTr.setTag_name(TextUtils.isEmpty(userModel.userName) ? getString(R.string.tv_msg177) : userModel.userName);
            videoItemTr.setIcon(userModel.userAvatar);
            videoItemTr.setBgmp(userModel.userAvatar);
            videoItemTr.setPaymnes(paymest);
            videoItemTr.shwovideos1();
        }

    }


}