package com.tencent.liteav.trtcvideocalldemo.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.google.gson.Gson;
import com.savglotti.activity.PlaySavgLotti;
import com.tencent.Camera.MediaPlayHelper;
import com.tencent.Camera.My_Camera;
import com.tencent.Camera.camera_item;
import com.tencent.liteav.BaseAppCompatActivity;
import com.tencent.liteav.FloatingService;
import com.tencent.liteav.callService;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcvideocalldemo.ui.videolayout.TRTCVideoLayout;
import com.tencent.liteav.trtcvideocalldemo.ui.videolayout.TRTCVideoLayoutManager;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.IGiftPanelView;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapterImp;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftPanelViewImp;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.DialogcallBack;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupInfo;
import com.tencent.qcloud.tim.uikit.modules.group.info.GroupUpdate;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.accordingdate;
import com.tencent.qcloud.tim.uikit.utils.goldcoin;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;
import com.tencent.qcloud.tim.uikit.utilsdialog.dialogsvip;
import com.tencent.qcloud.tim.uikit.utilsdialog.play;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于展示视频通话的主界面，通话的接听和拒绝就是在这个界面中完成的。
 *
 * @author guanyifeng
 */
public class TRTCVideoCallActivity extends BaseAppCompatActivity {
    private static final String TAG = TRTCVideoCallActivity.class.getName();
    public static final int TYPE_BEING_CALLED = 1; // 被叫方
    public static final int TYPE_CALL = 2; // 主叫方
    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_USER = "user_model";
    public static final String PARAM_BEINGCALL_USER = "beingcall_user_model";
    public static final String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int MAX_SHOW_INVITING_USER = 4;
    private static final int RADIUS = 30;

    /**
     * 界面元素相关
     */
    private ImageView mMuteImg;
    private LinearLayout mMuteLl;
    private ImageView mHangupImg;
    private LinearLayout mHangupLl;
    private ImageView mHandsfreeImg;
    private LinearLayout mHandsfreeLl;
    private ImageView mDialingImg;
    private LinearLayout mDialingLl;
    private LinearLayout mDialingLl1;
    private PlaySavgLotti playsavglotti;
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv;
    private Runnable mTimeRunnable;
    /**
     * 通话时长秒
     */
    private int mTimeCount;
    private camera_item camraitem;
    private ImageButton imageButton;
    private ImageView icon_groups;
    private My_Camera mycamera;

    /**
     * 主叫方标记扣费
     */
    private boolean callingparty = false;
    private accordingdate accordingdate;
    private UserInfo userInfo;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    /**
     * 拨号相关成员变量
     */
    private List<UserModel> mCallUserModelList = new ArrayList<>();             // 对方作为被呼叫方
    private final Map<String, UserModel> mCallUserModelMap = new HashMap<>();   // 对方作为被呼叫方
    private UserModel mSelfModel;                                               // 自己的资料
    private UserModel mSponsorUserModel;                                        // 自己为被叫方
    private List<UserModel> mOtherInvitingUserModelList;                        // 自己为被叫方

    private int mCallType;
    private ITRTCAVCall mITRTCAVCall;
    private boolean isHandsFree = true;
    private boolean isMuteMic = false;
    private boolean isFrontCamera = true; //您可以调用该函数切换前后摄像头
    private boolean iscallrontCamera = false; //正在通话中
    private String mGroupId;          //群组ID
    private Vibrator mVibrator;       //Vibrator(振动器)，是手机自带的振动器

    private MediaPlayHelper mMediaPlayHelper;
    private Ringtone mRingtone;      //铃声
    private play plays;//主叫铃声
    private boolean mEnableMuteMode = false;  // 是否开启静音模式
    private String mCallingBellPath = "";     // 被叫铃音路径

    /**
     * 拨号的回调
     */
    private final TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            //发生了错误，报错并退出该页面
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
            mSponsorUserModel = new UserModel();
            mSponsorUserModel.userId = userId;
            iscallrontCamera = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showCallingView();
                    //1.先造一个虚拟的用户添加到屏幕上
                    UserModel model = new UserModel();
                    model.userId = userId;
                    model.phone = "";
                    model.userName = userId;
                    model.userAvatar = "";
                    mCallUserModelList.add(model);
                    mCallUserModelMap.put(model.userId, model);
                    TRTCVideoLayout videoLayout = addUserToManager(model);
                    if (videoLayout == null) {
                        return;
                    }
                    videoLayout.setVideoAvailable(false);

                    //2. 再获取用户资料，如果搜索到了该用户，更新用户的信息
                    ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                        @Override
                        public void onSuccess(UserModel model) {
                            UserModel oldModel = mCallUserModelMap.get(model.userId);
                            if (oldModel != null) {
                                //填充用户资料
                                oldModel.userName = model.userName;
                                oldModel.userAvatar = model.userAvatar;
                                oldModel.phone = model.phone;
                                TRTCVideoLayout videoLayout = mLayoutManagerTrtc.findCloudViewView(model.userId);
                                if (videoLayout != null) {
                                    GlideEngine.loadCornerImage(videoLayout.getHeadImg(), oldModel.userAvatar, null, RADIUS);
                                    videoLayout.getUserNameTv().setText(oldModel.userName);
                                }
                            }
                        }

                        @Override
                        public void onFailed(int code, String msg) {
                            ToastUtil.toastLongMessage(getString(R.string.get_user_info_tips_before) + userId + getString(R.string.get_user_info_tips_after));
                        }
                    });
                }
            });


        }

        @Override
        public void onUserLeave(final String userId) {
            Log.d(TAG, "onUserLeave: 对方离开通话的用户" + userId);
            mSponsorUserModel = new UserModel();
            mSponsorUserModel.userId = userId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. 删除用户model
                    UserModel userModel = mCallUserModelMap.remove(userId);
                    if (userModel != null) {
                        mCallUserModelList.remove(userModel);
                    }
                }
            });
        }

        @Override
        public void onReject(final String userId) {
            Log.d(TAG, "onReject: 拒绝通话的用户" + userId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // 进入拒绝环节
                        //1. 回收界面元素
                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.reject_calls));
                        }
                    }
                }
            });
        }

        @Override
        public void onNoResp(final String userId) {
            Log.d(TAG, "onNoResp: 无人应答的回调" + userId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mCallUserModelMap.containsKey(userId)) {
                        // 进入无响应环节
                        //1. 回收界面元素
                        mLayoutManagerTrtc.recyclerCloudViewView(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.no_response));
                        }
                    }
                }
            });
        }

        @Override
        public void onLineBusy(String userId) {
            Log.d(TAG, "onLineBusy: 邀请方忙线");
            if (mCallUserModelMap.containsKey(userId)) {
                // 进入无响应环节
                //1. 回收界面元素
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. 删除用户model
                UserModel userModel = mCallUserModelMap.remove(userId);
                if (userModel != null) {
                    mCallUserModelList.remove(userModel);
                    ToastUtil.toastLongMessage(userModel.userName + getString(R.string.line_busy));
                }
            }
        }

        @Override
        public void onCallingCancel() {
            Log.d(TAG, "作为被邀请方会收到，收到该回调说明本次通话被取消了");
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.cancle_calling));
            }
            finishActivity();
        }

        @Override
        public void onCallingTimeout() {
            Log.d(TAG, "作为被邀请方会收到，收到该回调说明本次通话超时未应答");
            if (mSponsorUserModel != null) {
                ToastUtil.toastLongMessage(mSponsorUserModel.userName + getString(R.string.call_time_out));
            }
            finishActivity();
        }

        @Override
        public void onCallEnd() {
            Log.d(TAG, "被动本次通话结束了");
            deduction();
            finishActivity();

        }

        @Override
        public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
            Log.d(TAG, "true:远端用户打开麦克风  false:远端用户关闭麦克风");
            TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
            if (layout != null) {
                layout.setVideoAvailable(isVideoAvailable);
                if (isVideoAvailable) {
                    mITRTCAVCall.startRemoteView(userId, layout.getVideoView());
                } else {
                    mITRTCAVCall.stopRemoteView(userId);
                }
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, " true:远端用户打开摄像头  false:远端用户关闭摄像头");
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
                TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
                if (layout != null) {
                    layout.setAudioVolumeProgress(entry.getValue());
                }
            }
        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {
            //收到对方发来的礼物
            if (!TextUtils.isEmpty(giftInfo.giftPicUrl)) {
                playsavglotti.showGift(giftInfo);
            }

        }

        @Override
        public void longrangecall(String json) {

        }
    };

    private Group mSponsorGroup;
    private DefaultGiftAdapterImp mGiftAdapter;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo;
    private String mSelfUserId;
    private TRTCVideoLayout videoLayout;

    /**
     * 主动拨打给某个用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomeone(Context context, List<UserModel> models) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
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
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);

        Log.d(TAG, "startCallSomePeople: " + groupId);
    }

    /**
     * 作为被叫用户
     *
     * @param context
     * @param beingCallUserModel
     */
    public static void startBeingCall(Context context, UserModel beingCallUserModel, List<UserModel> otherInvitingUserModel) {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserInfo.getInstance();
        accordingdate = new accordingdate(80, 80, TYPE_CALL); //默认80个币1分钟 初始金币和VIP状态

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // 有些手机禁止后台启动Activity，但是有bug，比如一种场景：对方反复拨打并取消，三次以上极容易从后台启动成功通话界面，
            // 此时对方再挂断时，此通话Activity调用finish后，上一个从后台启动的Activity就会弹出。此时这个Activity就不能再启动。
            finishActivity();
            return;
        }

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE); //不允许截屏
        setContentView(R.layout.videocall_activity_online_call);

        //PermissionUtils.checkPermission(this, Manifest.permission.CAMERA);      //相机权限申请
        //PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO); //录音权限申请

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);//震动器
        //mRingtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)); //铃声
        //初始化铃声播放

        //打出去的铃声
        plays = play.cerplay(TUIKit.getAppContext());
        //接听来电铃声
        mMediaPlayHelper = new MediaPlayHelper(TUIKit.getAppContext());

        initView();
        initData();
        initListener();
    }

    private void finishActivity() {
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        play.cerplay(TUIKit.getAppContext()).btnDestroy();
        playHangupMusic();
        stopMusic();
        finish();

    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: 用户主动退出");
        deduction();
        if (mITRTCAVCall != null) {
            if (iscallrontCamera) {
                mITRTCAVCall.hangup();
            } else {
                mITRTCAVCall.reject();
            }
        }
        super.onBackPressed();
        finishActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callService.isboocall = false; //可以允自动打电话
        if (mVibrator != null) {
            mVibrator.cancel();
        }
        if (mRingtone != null) {
            mRingtone.stop();
        }
        if (mITRTCAVCall != null) {
            mITRTCAVCall.closeCamera();
            mITRTCAVCall.removeListener(mTRTCAVCallListener);
        }
        stopTimeCount();
        if (mTimeHandlerThread != null) {
            mTimeHandlerThread.quit();
        }

    }

    /**
     * 音量开关监听
     */
    private void initListener() {
        mMuteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuteMic = !isMuteMic;
                mITRTCAVCall.setMicMute(isMuteMic);
                mMuteImg.setActivated(isMuteMic);
                ToastUtil.toastLongMessage(isMuteMic ? getString(R.string.open_silent) : getString(R.string.close_silent));
            }
        });

        //扩音监听
        mHandsfreeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandsFree = !isHandsFree;
                mITRTCAVCall.setHandsFree(isHandsFree);
                mHandsfreeImg.setActivated(isHandsFree);
                ToastUtil.toastLongMessage(isHandsFree ? getString(R.string.use_speakers) : getString(R.string.use_handset));
            }
        });
        mMuteImg.setActivated(isMuteMic);
        mHandsfreeImg.setActivated(isHandsFree);

        //调用该函数切换前后摄像头
        imageButton.setOnClickListener(v -> {
            isFrontCamera = isFrontCamera ? false : true;
            mITRTCAVCall.switchCamera(isFrontCamera);
        });
    }

    /**
     * 发起邀请好友视频
     */
    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }
        //发起通话对方userModel.userId发起邀请视频通话
        Log.d(TAG, "startInviting: " + mGroupId);
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_VIDEO_CALL, mGroupId);
    }

    /**
     * 控件初始化
     */
    private void initView() {
        mMuteImg = (ImageView) findViewById(R.id.img_mute);
        mMuteLl = (LinearLayout) findViewById(R.id.ll_mute);
        mHangupImg = (ImageView) findViewById(R.id.img_hangup);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup);
        mHandsfreeImg = (ImageView) findViewById(R.id.img_handsfree);
        mHandsfreeLl = (LinearLayout) findViewById(R.id.ll_handsfree);
        mDialingImg = (ImageView) findViewById(R.id.img_dialing);
        mDialingLl = (LinearLayout) findViewById(R.id.ll_dialing);
        mDialingLl1 = (LinearLayout) findViewById(R.id.ll_dialing1);
        playsavglotti = (PlaySavgLotti) findViewById(R.id.playsavglotti);
        mLayoutManagerTrtc = (TRTCVideoLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mSponsorGroup = (Group) findViewById(R.id.group_sponsor);
        camraitem = findViewById(R.id.camraitem);
        mycamera = findViewById(R.id.mycamera);
        imageButton = findViewById(R.id.btn_switch_cam);
        icon_groups = findViewById(R.id.icon_groups);


    }

    /**
     * 初始化成员变量
     */
    private void initData() {
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAVCallListener);
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());

        //初始化呼叫方的数据
        Intent intent = getIntent();
        mSelfModel = ProfileManager.getInstance().getUserModel();
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);
        mAnchorInfo = new TRTCLiveRoomDef.LiveAnchorInfo();

        if (mCallType == TYPE_BEING_CALLED) {
            //自己作为被叫(被叫方、接收方)
            callingparty = false;
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            mAnchorInfo.userId = mSponsorUserModel.userId;
            mSelfUserId = UserInfo.getInstance().getUserId();
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            showWaitingResponseView();

            //初始化扣费标准
            accordingdate.dateinit(mSelfModel); //传入我的ID

            //Vibrator振动器
            //Vibrator.vibrate(300);
            mVibrator.vibrate(new long[]{0, 1000, 1000}, 0); //震动器
            //mRingtone.play();                                   //铃声播放

            //自定同意接听
            if (mSponsorUserModel.floating) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDialingLl.performClick();
                    }
                }, 200);
            }

        } else {
            //自己为拨打出去的（主叫方、呼叫方）
            callingparty = true;
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList = params.mUserModels;
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                    mAnchorInfo.userId = userModel.userId;
                    mSelfUserId = UserInfo.getInstance().getUserId();

                    //初始化扣费标准
                    accordingdate.dateinit(userModel); //传入对方的ID
                }
                startInviting();
                showInvitingView();
            }
        }

        //礼物面板mGiftAdapter创建
        mGiftAdapter = new DefaultGiftAdapterImp();
        mGiftInfoDataHandler = new GiftInfoDataHandler();
        mGiftInfoDataHandler.setGiftAdapter(mGiftAdapter);
    }

    /**
     * 等待接听界面
     */
    public void showWaitingResponseView() {
        //2. 展示对方的头像和蒙层
        mSponsorGroup.setVisibility(View.VISIBLE);
        mycamera.setPath(mSponsorUserModel.userAvatar);
        mycamera.setname(TextUtils.isEmpty(mSponsorUserModel.userName) ? mSponsorUserModel.userId : mSponsorUserModel.userName);

        //3. 展示电话对应界面
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        camraitem.setVisibility(View.GONE);
        mDialingLl1.setVisibility(View.GONE);

        //3. 设置对应的listener 拒绝接听
        mHangupLl.setOnClickListener(v -> videoCallcancel());
        //接听电话
        mDialingLl.setOnClickListener(v -> {
            //录音权限申请|相机权限申请
            if (PermissionUtils.checkPermission(this)) {
                checkAnswer();
            }
        });

        //4.展示其他用户界面
        showOtherInvitingUserView();

        //播放铃声
        startDialingMusic();

        //打开悬浮窗权权
        icon_groups.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(TUIKit.getAppContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, RADIUS);
                } else {
                    //隐藏最小化
                    ToastUtil.toastLongMessage("隐藏最小化成功");
                    FloatingService.startBeingCall(TUIKit.getAppContext(), mSponsorUserModel);
                    smMediaPlayHelper();
                }
            }

        });

    }

    /**
     * 拨打展示邀请界面
     */
    public void showInvitingView() {
        //1. 展示自己的视频界面
        openCamera();

        //展示对方头像和名称
        for (UserModel userModel : mCallUserModelList) {
            camraitem.setVisibility(View.VISIBLE);
            camraitem.setImage(userModel.userAvatar);
            camraitem.setVideo_name(TextUtils.isEmpty(userModel.userName) ? userModel.userId : userModel.userName);
            mycamera.setVisibility(View.GONE);
        }


      /*  for (UserModel userModel : mCallUserModelList) {
            TRTCVideoLayout layout = addUserToManager(userModel);
            layout.getHeadImg().setVisibility(View.VISIBLE);
        }
*/

        //2. 设置底部栏
        mHangupLl.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        mDialingLl.setVisibility(View.GONE);

        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        mDialingLl1.setVisibility(View.GONE);

        //3. 隐藏中间他们也在界面
        hideOtherInvitingUserView();
        //4. sponsor画面也隐藏
        mSponsorGroup.setVisibility(View.GONE);

        //5.播放视频呼叫声音
        if (plays != null) {
            try {
                plays.initMediaPlayer();
                plays.btnPlay();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 展示通话中的界面
     */
    public void showCallingView() {
        //1. 蒙版消失
        mSponsorGroup.setVisibility(View.GONE);
        camraitem.setVisibility(View.GONE);
        mycamera.setVisibility(View.GONE);
        mDialingLl.setVisibility(View.GONE);

        //2. 底部状态栏
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl1.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.VISIBLE);
        mMuteLl.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        playsavglotti.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deduction();
                Log.d(TAG, "onClick: 主动挂断");
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });
        mDialingLl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.gettRole() == 1) {
                    showGiftPanel();  //礼物面板
                } else {

                    //礼物送出之前先查一下自己的金币是否满足条件
                    accordingdate.goldcoincallback(new accordingdate.Callback() {
                        @Override
                        public void OnSuccess() {
                            //通话时长小于5分钟，请充值后再操作
                            if (accordingdate.getTimeCount() <= 5) {
                                ToastUtil.toastLongMessage(getString(R.string.tv_msg_call_video));
                            } else {
                                showGiftPanel();//礼物面板
                            }
                        }

                        @Override
                        public void onFall() {

                        }
                    });
                }


            }
        });
        showTimeCount();
        hideOtherInvitingUserView();

        //通话中停止播铃声
        if (plays != null) {
            plays.btnStop();
            stopMusic();
        }
    }

    /**
     * 视频通话时长计时
     */
    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    TimeCount(mTimeCount);
                }
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    /**
     * 更新通话UI时间状态
     */
    private void TimeCount(int mTimeCount) {

        boolean iscall = (callingparty && accordingdate.timeout(mTimeCount) && userInfo.gettRole() == 0);

        if (mTimeTv != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //主叫方提示 通话时长小于2分钟 callingparty=true;
                    if (iscall) {
                        mTimeTv.setText(String.format(getString(R.string.tv_call_video), accordingdate.PshowTime(mTimeCount)));  //更新通话刷新
                    } else {
                        mTimeTv.setText(getShowTime(mTimeCount));  //更新通话刷新
                    }
                }
            });
        }

        //主叫方通话时间到
        if (iscall) {
            Log.d(TAG, "主叫方通话时间到");
            deduction();
            finishhangup();
            mTimeHandler.removeCallbacks(mTimeRunnable);
            mTimeHandler.removeCallbacksAndMessages(null);
            return;
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);

    }

    /**
     * 通话结束
     */
    private void stopTimeCount() {
        if (mTimeHandler != null) {
            mTimeHandler.removeCallbacks(mTimeRunnable);
        }
        mTimeRunnable = null;
    }

    /**
     * 转换通话时间长计算
     *
     * @param count
     * @return
     */
    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.size() == 0) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.small_image_size);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel userModel = mOtherInvitingUserModelList.get(index);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadCornerImage(imageView, userModel.userAvatar, null, RADIUS);
            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private TRTCVideoLayout addUserToManager(UserModel userModel) {
        TRTCVideoLayout layout = mLayoutManagerTrtc.allocCloudVideoView(userModel.userId);
        if (layout == null) {
            return null;
        }
        layout.getUserNameTv().setText(userModel.userName);
        if (!TextUtils.isEmpty(userModel.userAvatar)) {
            GlideEngine.loadCornerImage(layout.getHeadImg(), userModel.userAvatar, null, RADIUS);
        }
        return layout;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.requestCode) {
            if (PermissionUtils.checkPermission(TUIKit.getAppContext(), Constants.requestCode)) {
                checkAnswer();
            } else {
                ToastUtil.toastLongMessage(getString(R.string.ts_call_msg));
            }

        }

    }

    /**
     * 金币不足通话强制结束了
     */
    private void finishhangup() {
        if (mITRTCAVCall != null) {
            mITRTCAVCall.hangup();
        }
        finishActivity();
    }

    /**
     * 拒绝接听
     */
    private void videoCallcancel() {

        if (mVibrator != null) {
            mVibrator.cancel();    //震动器
        }
        if (mRingtone != null) {
            mRingtone.stop();      //铃声
        }
        mITRTCAVCall.reject(); //当您作为被邀请方收到 {@link TRTCAVCallListener#onInvited } 的回调时，可以调用该函数拒绝来电
        finishActivity();      //关闭activity
    }

    /**
     * 检查权限接听
     */
    private void checkAnswer() {
        //男性用户非VIP无法接听 女性会员免费接听
        if (userInfo.getSex().equals("1") && userInfo.getVip() == 0 && userInfo.gettRole() == 0) {
            dialogsvip.getdialogsvip(this, mSponsorUserModel, dialogcallBack);
            return;
        }
        stopMusic();
        stopmusicplay();
        mITRTCAVCall.accept(); //同意接听

        //接听打开相机
        openCamera();

        //3开始统计通话时间
        showCallingView();

        //4同意接听电话发送接听记录
        Postdeduction.setcallvideo(TYPE_CALL, mSelfModel.userId, mAnchorInfo.userId);

    }

    /**
     * 向服务器发起扣取金币请求
     */
    public void deduction() {
        if (mTimeCount > 0) {
            double kfmoney = accordingdate.kfmoney(mTimeCount);
            Log.d(TAG, "收取金币:" + kfmoney + "TYPE:" + TYPE_CALL);
            //发送扣费标准
            Postdeduction.deduction(callingparty, mSelfModel.userId, mSponsorUserModel.userId, String.valueOf(kfmoney), TYPE_CALL);
        }
    }

    private DialogcallBack dialogcallBack = new DialogcallBack() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    videoCallcancel();
                }
            });
        }

        @Override
        public void onError() {

        }
    };

    /**
     * 展示礼物面板 弹窗布局
     */
    private void showGiftPanel() {
        //在这里传入播主ID和观众的尖id用于扣除金币 目前只是初始弹窗dialog
        IGiftPanelView giftPanelView = new GiftPanelViewImp(this);
        giftPanelView.init(mGiftInfoDataHandler); //加载礼物内容
        giftPanelView.setGiftPanelUser(mAnchorInfo, mSelfUserId); // mAnchorInfo//主播对像 //用户mSelfUserId
        //监听赠送礼物或发起充值接口
        giftPanelView.setGiftPanelDelegate(new GiftPanelDelegate() {
            @Override
            public void onGiftItemClick(GiftInfo giftInfo) {
                ToastUtil.toastLongMessage(giftInfo.title);
                giftInfo.sendUser = userInfo.getName();
                giftInfo.sendUserHeadIcon = userInfo.getAvatar();
                giftInfo.userid = userInfo.getUserId();
                mITRTCAVCall.invitereceiver(mAnchorInfo.userId, new Gson().toJson(giftInfo));
                //回调赠送成功接口
                if (!TextUtils.isEmpty(giftInfo.giftPicUrl)) {
                    //礼物播放动作
                    playsavglotti.showGift(giftInfo);
                }
            }

            @Override
            public void onChargeClick() {
                //回调充值币金接口

            }

            @Override
            public void myoney(Object obj) {
                //送出礼物后刷新可用金币
                double momey = (double) obj;
                if (momey > 0) {
                    accordingdate.setMomey(momey);
                }
            }

        });
        giftPanelView.show();
    }


    /**
     * 1. 接通后视频展示画面
     */
    public void openCamera() {
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        //打开摄像头
        mITRTCAVCall.openCamera(true, videoLayout.getVideoView());

        //您可以调用该函数关闭摄像头
        //mITRTCAVCall.closeCamera();
        //您可以调用该函数切换前后摄像头
        //mITRTCAVCall.switchCamera(true);
    }

    /****************************************************************************************/
    /**
     * 播放来电铃声
     */
    private void startDialingMusic() {
        if (null == mMediaPlayHelper) {
            return;
        }
        mMediaPlayHelper.start(R.raw.ring_tone, 30000);
    }

    /**
     * 播放挂断播放音乐
     */
    private void stopMusic() {
        if (null == mMediaPlayHelper) {
            return;
        }
        final int resId = mMediaPlayHelper.getResId();
        // 挂断音效很短，播放完即可；主叫铃音和被叫铃音需主动stop
        if (resId != R.raw.hang_up) {
            mMediaPlayHelper.stop();
        }
    }

    /**
     * 挂断或拒绝通话播放2秒
     */
    private void playHangupMusic() {
        if (null == mMediaPlayHelper) {
            return;
        }
        mMediaPlayHelper.start(R.raw.hang_up, 2000);
    }

    private void stopmusicplay() {
        if (mVibrator != null) {
            mVibrator.cancel();    //震动器
        }
        if (mRingtone != null) {
            mRingtone.stop();      //铃声
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RADIUS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(TUIKit.getAppContext())) {
                        ToastUtil.toastLongMessage(getString(R.string.call_a3));
                    } else {
                        ToastUtil.toastLongMessage(getString(R.string.call_a4));
                    }
                }
                break;

        }
    }

    /**
     * 最小化窗口
     */
    public void smMediaPlayHelper() {
        if (mMediaPlayHelper != null) {
            mMediaPlayHelper.stop();
            mMediaPlayHelper = null;
            play.cerplay(TUIKit.getAppContext()).btnDestroy();
        }
        finish();
    }

}
