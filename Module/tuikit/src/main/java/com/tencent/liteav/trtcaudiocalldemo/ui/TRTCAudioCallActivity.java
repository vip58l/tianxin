package com.tencent.liteav.trtcaudiocalldemo.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

import com.bumptech.glide.Glide;
import com.tencent.Camera.MediaPlayHelper;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.BaseAppCompatActivity;
import com.tencent.liteav.SelectContactActivity;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.IntentParams;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcaudiocalldemo.ui.audiolayout.TRTCAudioLayout;
import com.tencent.liteav.trtcaudiocalldemo.ui.audiolayout.TRTCAudioLayoutManager;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.DialogcallBack;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.accordingdate;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;
import com.tencent.qcloud.tim.uikit.utilsdialog.dialogsvip;
import com.tencent.qcloud.tim.uikit.utilsdialog.play;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 用于展示语音通话的主界面，通话的接听和拒绝就是在这个界面中完成的。
 *
 * @author guanyifeng
 */
public class TRTCAudioCallActivity extends BaseAppCompatActivity {
    private static final String TAG = TRTCAudioCallActivity.class.getName();
    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_BEING_CALLED_FROM_NOTIFICATION = 3;
    public static final int TYPE_CALL = 2;
    public static final String PARAM_GROUP_ID = "group_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_USER = "user_model";
    public static final String PARAM_BEINGCALL_USER = "beingcall_user_model";
    public static final String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int MAX_SHOW_INVITING_USER = 2;
    private static final int RADIUS = 30;


    /**
     * 界面元素相关
     */
    private ImageView audiocallbg;
    private ImageView mMuteImg;
    private LinearLayout mMuteLl;
    private ImageView mHangupImg;
    private LinearLayout mHangupLl;
    private ImageView mHandsfreeImg;
    private LinearLayout mHandsfreeLl;
    private ImageView mDialingImg;
    private LinearLayout mDialingLl;
    private TRTCAudioLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv, body_msg;
    private Runnable mTimeRunnable;
    private int mTimeCount;

    private boolean callingparty = false; //主叫方标记扣费
    private accordingdate accordingdate;
    private UserInfo userInfo;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    /**
     * 拨号相关成员变量
     */
    private List<UserModel> mCallUserModelList = new ArrayList<>();             // 对方作为被呼叫方
    private final Map<String, UserModel> mCallUserModelMap = new HashMap<>();   // 对方作为被呼叫方
    private UserModel mSelfModel;                                               // 自己为呼叫方
    private UserModel mSponsorUserModel;                                        // 自己为被叫方
    private List<UserModel> mOtherInvitingUserModelList;                        // 自己为被叫方


    private int mCallType;
    private ITRTCAVCall mITRTCAVCall;
    private String mGroupId;
    private boolean isHandsFree = true;
    private boolean isMuteMic = false;
    private boolean iscallrontCamera = false; //正在通话中
    private Vibrator mVibrator;  //Vibrator(振动器)，是手机自带的振动器
    private Ringtone mRingtone;  //铃声
    private MediaPlayHelper mMediaPlayHelper;
    private play plays;//主叫铃声
    private boolean mEnableMuteMode = false;  // 是否开启静音模式
    private String mCallingBellPath = "";     // 被叫铃音路径


    /**
     * 拨号的回调监听 事件处理
     */
    private final TRTCAVCallListener mTRTCAudioCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
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

                    TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                    if (layout != null) {
                        layout.stopLoading();
                    } else {
                        // 没有这个用户，需要重新分配, 先获取用户资料，在进行分配
                        ProfileManager.getInstance().getUserInfoByUserId(userId, new ProfileManager.GetUserInfoCallback() {
                            @Override
                            public void onSuccess(UserModel model) {
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }

                            @Override
                            public void onFailed(int code, String msg) {
                                // 获取用户资料失败了，模拟一个用户
                                ToastUtil.toastLongMessage(getString(R.string.get_user_info_tips_before) + userId + getString(R.string.get_user_info_tips_after));
                                UserModel model = new UserModel();
                                model.userId = userId;
                                model.phone = "";
                                model.userName = userId;
                                model.userAvatar = "";
                                mCallUserModelList.add(model);
                                mCallUserModelMap.put(model.userId, model);
                                addUserToManager(model);
                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            Log.d(TAG, "onUserLeave: 离开通话的用户" + userId);
            mSponsorUserModel = new UserModel();
            mSponsorUserModel.userId = userId;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //1. 回收界面元素
                    mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
                        //2. 删除用户model
                        UserModel userModel = mCallUserModelMap.remove(userId);
                        if (userModel != null) {
                            mCallUserModelList.remove(userModel);
                            ToastUtil.toastLongMessage(userModel.userName + getString(R.string.reject_call));
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
                        mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
                mLayoutManagerTrtc.recyclerAudioCallLayout(userId);
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
            Log.d(TAG, "收到该回调说明本次通话结束了");
            deduction();
            finishActivity();
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, " true:远端用户打开摄像头  false:远端用户关闭摄像头");
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, "true:远端用户打开麦克风  false:远端用户关闭麦克风");
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
                TRTCAudioLayout layout = mLayoutManagerTrtc.findAudioCallLayout(userId);
                if (layout != null) {
                    layout.setAudioVolume(entry.getValue());
                }
            }
        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {
            Log.d(TAG, "getgiftInfo: " + giftInfo.toString());
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
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models)); //转换添加Serializable
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * 主动拨打给某些用户
     *
     * @param context
     * @param models
     */
    public static void startCallSomePeople(Context context, List<UserModel> models, String groupId) {
        TUIKitLog.i(TAG, "startCallSomePeople");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_GROUP_ID, groupId);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_USER, new IntentParams(models)); //转换添加Serializable
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
        TUIKitLog.i(TAG, "startBeingCall");
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(context)).setWaitingLastActivityFinished(false);
        Intent starter = new Intent(context, TRTCAudioCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserModel);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserModel));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /************************ 执行接收或拨打处理 ************************************************/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = UserInfo.getInstance();
        accordingdate = new accordingdate(60, 60, TYPE_BEING_CALLED); //默认60个币1分钟

        mCallType = getIntent().getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED); //呼叫类型语音 1
        if (mCallType == TYPE_BEING_CALLED && ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).isWaitingLastActivityFinished()) {
            // 有些手机禁止后台启动Activity，但是有bug，比如一种场景：对方反复拨打并取消，三次以上极容易从后台启动成功通话界面，
            // 此时对方再挂断时，此通话Activity调用finish后，上一个从后台启动的Activity就会弹出。此时这个Activity就不能再启动。
            finishActivity();
            return;
        }

        // 是状态栏通知的管理类，负责发通知、清楚通知等
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.cancelAll();
        }

        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.audiocall_activity_call_main);

        //使用该功能，需要开启权限，鉴于您禁用相关权限，请手动设置开启权限
        //PermissionUtils.checkPermission(this, Manifest.permission.CAMERA);      //相机权限申请
        //PermissionUtils.checkPermission(this, Manifest.permission.RECORD_AUDIO); //录音权限申请

        //振动器(Vibrator)
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        //mRingtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

        //初始化铃声播放
        startCall();

        initView();
        initData();
        initListener();
    }

    /**
     * 关闭activity
     */
    private void finishActivity() {
        playHangupMusic();
        if (plays != null) {
            plays.btnDestroy();
        }
        stopMusic();
        ((TRTCAVCallImpl) TRTCAVCallImpl.sharedInstance(this)).setWaitingLastActivityFinished(true);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        deduction(); //主动退出
        if (mITRTCAVCall != null) {
            if (iscallrontCamera) {
                mITRTCAVCall.hangup();//当您处于通话中，可以调用该函数结束通话 退出这个界面的时候，需要挂断
            } else {
                mITRTCAVCall.reject(); //拒绝接听
            }
        }
        finishActivity();
    }

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
     * 监听事件处理
     */
    private void initListener() {
        mMuteLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMuteMic = !isMuteMic;
                mITRTCAVCall.setMicMute(isMuteMic); //设置静音
                mMuteImg.setActivated(isMuteMic);
                ToastUtil.toastLongMessage(isMuteMic ? getString(R.string.open_silent) : getString(R.string.close_silent));
            }
        });
        mHandsfreeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isHandsFree = !isHandsFree;
                mITRTCAVCall.setHandsFree(isHandsFree); //打开免提
                mHandsfreeImg.setActivated(isHandsFree);
                ToastUtil.toastLongMessage(isHandsFree ? getString(R.string.use_speakers) : getString(R.string.use_handset));
            }
        });
        mMuteImg.setActivated(isMuteMic);
        mHandsfreeImg.setActivated(isHandsFree);
    }

    /**
     * 初始化成员变量
     */
    private void initData() {
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(this);
        mITRTCAVCall.addListener(mTRTCAudioCallListener); //加入监听事件
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());

        // 初始化从外界获取的数据
        Intent intent = getIntent();
        mSelfModel = ProfileManager.getInstance().getUserModel();      //获取自己的资料
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED); //调用的类型
        mGroupId = intent.getStringExtra(PARAM_GROUP_ID);              //群组ID参数默认 空值
        if (mCallType == TYPE_BEING_CALLED) {
            // 作为被叫(接收方)
            callingparty = false;
            mSponsorUserModel = (UserModel) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserModelList = params.mUserModels;
            }
            showWaitingResponseView();

            //初始化扣费标准
            accordingdate.dateinit(mSelfModel); //传入我的ID

            //Vibrator振动器
            //Vibrator.vibrate(300);
            mVibrator.vibrate(new long[]{0, 1000, 1000}, 0);
            //mRingtone.play();  //播放铃声


        } else {
            //拨打出去的（主叫方）
            callingparty = true;
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            if (params != null) {
                mCallUserModelList = params.mUserModels;
                for (UserModel userModel : mCallUserModelList) {
                    mCallUserModelMap.put(userModel.userId, userModel);
                    //初始化扣费标准
                    accordingdate.dateinit(userModel); //传入对方的ID
                }

                startInviting();     //发起邀请电话
                showInvitingView();  //展示邀请列表界面
            }

        }

        //背景模糊
        if (!TextUtils.isEmpty(mSelfModel.userAvatar)) {
            Glide.with(this).load(mSelfModel.userAvatar).apply(bitmapTransform(new BlurTransformation(25, 10))).into(audiocallbg);
        } else {
            Glide.with(this).load(R.drawable.ic_avatar).apply(bitmapTransform(new BlurTransformation(25, 10))).into(audiocallbg);
        }
    }

    /**
     * 发起邀请电话
     */
    private void startInviting() {
        List<String> list = new ArrayList<>();
        for (UserModel userModel : mCallUserModelList) {
            list.add(userModel.userId);
        }

        //发起邀请电话
        mITRTCAVCall.groupCall(list, ITRTCAVCall.TYPE_AUDIO_CALL, mGroupId);
    }

    /**
     * 获取初始组件
     */
    private void initView() {
        mMuteImg = (ImageView) findViewById(R.id.img_mute);
        mMuteLl = (LinearLayout) findViewById(R.id.ll_mute);
        mHangupImg = (ImageView) findViewById(R.id.img_hangup);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup); //拒绝接或挂断
        mHandsfreeImg = (ImageView) findViewById(R.id.img_handsfree);
        mHandsfreeLl = (LinearLayout) findViewById(R.id.ll_handsfree);
        mDialingImg = (ImageView) findViewById(R.id.img_dialing);
        mDialingLl = (LinearLayout) findViewById(R.id.ll_dialing);  //接通
        mLayoutManagerTrtc = (TRTCAudioLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        body_msg = (TextView) findViewById(R.id.body_msg);
        audiocallbg = (ImageView) findViewById(R.id.audiocallbg);
    }

    /**
     * 被叫方等待接听界面
     */
    public void showWaitingResponseView() {
        //1. 展示呼叫方的画面
        TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(mSponsorUserModel.userId);
        layout.setUserId(mSponsorUserModel.userName);
        GlideEngine.loadCornerImage(layout.getImageView(), mSponsorUserModel.userAvatar, null, RADIUS);
        updateUserView(mSponsorUserModel, layout);
        //2. 展示电话对应界面
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.VISIBLE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        body_msg.setVisibility(View.VISIBLE);
        body_msg.setText(R.string.trtcaudiocall_msg);
        //3. 设置对应的listener 拒绝或挂断接听
        mHangupLl.setOnClickListener(v -> audioCallcancel());
        //接听电话
        mDialingLl.setOnClickListener(v -> {
            //录音权限申请|相机权限申请
            if (PermissionUtils.checkPermission(this)) {
                checkAnswer();
            }
        });
        //4. 展示其他用户界面
        showOtherInvitingUserView();
        //5播放铃声
        startDialingMusic();


    }

    /**
     * 展示邀请列表界面
     */
    public void showInvitingView() {
        //1. 展示自己的界面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        addUserToManager(mSelfModel);
        //2. 展示对方的画面
        for (UserModel userModel : mCallUserModelList) {
            TRTCAudioLayout layout = addUserToManager(userModel);
            layout.startLoading();
        }

        //3. 设置底部栏
        mHangupLl.setVisibility(View.VISIBLE);

        //被叫方挂断
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mITRTCAVCall.hangup(); //通话结束
                finishActivity();
            }
        });
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.GONE);
        mMuteLl.setVisibility(View.GONE);
        //4. 隐藏中间他们也在界面
        hideOtherInvitingUserView();

        //5.播放视频呼叫声音
        if (plays != null) {
            plays.initMediaPlayer();
            plays.btnPlay();
        }
    }

    /**
     * 展示通话中的界面
     */
    public void showCallingView() {
        mHangupLl.setVisibility(View.VISIBLE);
        mDialingLl.setVisibility(View.GONE);
        mHandsfreeLl.setVisibility(View.VISIBLE);
        mMuteLl.setVisibility(View.VISIBLE);
        body_msg.setVisibility(View.VISIBLE);

        //呼叫方主动挂断
        mHangupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deduction(); //呼叫方主动挂断
                mITRTCAVCall.hangup();
                finishActivity();
            }
        });  //结束通话

        showTimeCount();
        hideOtherInvitingUserView();


        //通话中停止播铃声
        if (plays != null) {
            plays.btnStop();
            stopMusic();
        }
    }

    /**
     * 统计语音通话时长
     */
    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0; //通话时长累计
        mTimeTv.setText(getShowTime(mTimeCount));      //更新通话刷新
        body_msg.setText(R.string.trtcaudiiocall_a1);                //正在通话中
        //NeW启动线程
        if (mTimeRunnable == null) {
            mTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    mTimeCount++;
                    TimeCount(mTimeCount);
                }
            };
        }
        //执行时间统计开启
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
            Log.d(TAG, "TimeCount: 主叫方通话时间到");
            deduction();
            finishhangup();
            mTimeHandler.removeCallbacks(mTimeRunnable);
            mTimeHandler.removeCallbacksAndMessages(null);
            return;
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);

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
     * 通话时间转换
     *
     * @param count
     * @return
     */
    private String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    /**
     * 展示其他用户界面
     */
    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserModelList == null || mOtherInvitingUserModelList.isEmpty()) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.contact_avatar_width);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserModelList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserModel userModel = mOtherInvitingUserModelList.get(index);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            GlideEngine.loadCornerImage(imageView, userModel.userAvatar, null, SelectContactActivity.RADIUS);
            updateUserView(userModel, imageView);
            mImgContainerLl.addView(imageView);
        }
    }

    /**
     * 隐藏用户界面
     */
    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    /**
     * 设置我的界面头像名称
     *
     * @param userModel
     * @return
     */
    private TRTCAudioLayout addUserToManager(final UserModel userModel) {
        final TRTCAudioLayout layout = mLayoutManagerTrtc.allocAudioCallLayout(userModel.userId);
        layout.setUserId(userModel.userName);
        GlideEngine.loadCornerImage(layout.getImageView(), userModel.userAvatar, null, RADIUS);
        //读取用户头像和名称
        updateUserView(userModel, layout);
        return layout;
    }

    /**
     * 读取用户头像和名称
     *
     * @param userModel
     * @param layout
     */
    private void updateUserView(final UserModel userModel, final Object layout) {
        if (!TextUtils.isEmpty(userModel.userName) && !TextUtils.isEmpty(userModel.userAvatar)) {
            return;
        }
        ArrayList<String> users = new ArrayList<>();
        users.add(userModel.userId);
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {
                TUIKitLog.w(TAG, "getUsersInfo code:" + "|desc:" + desc);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() != 1) {
                    TUIKitLog.w(TAG, "getUsersInfo v2TIMUserFullInfos error");
                    return;
                }
                if (TextUtils.isEmpty(userModel.userName)) {
                    userModel.userName = v2TIMUserFullInfos.get(0).getNickName();
                    if (layout instanceof TRTCAudioLayout) {
                        ((TRTCAudioLayout) layout).setUserId(v2TIMUserFullInfos.get(0).getNickName());
                    }
                }
                userModel.userAvatar = v2TIMUserFullInfos.get(0).getFaceUrl();
                if (layout instanceof TRTCAudioLayout) {
                    GlideEngine.loadCornerImage(((TRTCAudioLayout) layout).getImageView(), userModel.userAvatar, null, RADIUS);
                } else if (layout instanceof ImageView) {
                    GlideEngine.loadCornerImage((ImageView) layout, userModel.userAvatar, null, RADIUS);
                }
            }
        });
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
    private void audioCallcancel() {
        if (mVibrator != null) {
            mVibrator.cancel();    //关闭震动器
        }
        if (mRingtone != null) {
            mRingtone.stop();      //停止铃声
        }
        mITRTCAVCall.reject(); //当您作为被邀请方收到 {@link TRTCAVCallListener#onInvited } 的回调时，可以调用该函数拒绝来电
        finishActivity();      //关闭activity
    }

    /**
     * 检查权限是否有权接听
     */
    private void checkAnswer() {
        if (userInfo.getVip() == 0 && userInfo.getSex().equals("1") && userInfo.gettRole() == 0) {
            //弹出是否购买VIP服务
            dialogsvip.getdialogsvip(this, mSponsorUserModel, dialogcallBack);
            return;
        }

        if (mVibrator != null) {
            mVibrator.cancel(); //停止震动
        }
        if (mRingtone != null) {
            mRingtone.stop();   //停止敀铃声
        }

        //1.分配自己的画面
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        addUserToManager(mSelfModel);

        //2.同意接听
        mITRTCAVCall.accept();

        //3开始统计通话时间
        showCallingView();

        //4同意接听电话发送接听记录
        Postdeduction.setcallvideo(TYPE_BEING_CALLED, mSelfModel.userId, mSponsorUserModel.userId);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.requestCode) {
            if (PermissionUtils.checkPermission(this, Constants.requestCode)) {
                checkAnswer();
            } else {
                ToastUtil.toastLongMessage(getString(R.string.ts_call_msg));
            }

        }

    }

    /**
     * 向服务器扣取金币
     */
    public void deduction() {
        if (mTimeCount > 0) {
            double kfmoney = accordingdate.kfmoney(mTimeCount);
            Log.d(TAG, "收取金币:" + kfmoney + "TYPE:" + TYPE_BEING_CALLED);
            Postdeduction.deduction(callingparty, mSelfModel.userId, mSponsorUserModel.userId, String.valueOf(kfmoney), TYPE_BEING_CALLED);
        }
    }

    private DialogcallBack dialogcallBack = new DialogcallBack() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    audioCallcancel();
                }
            });

        }

        @Override
        public void onError() {

        }
    };


    /**
     * 初始化音乐
     */
    private void startCall() {
        //打出云的铃声
        plays = new play(TUIKit.getAppContext());
        //接听来电铃声
        mMediaPlayHelper = new MediaPlayHelper(TUIKit.getAppContext());

    }

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
     * 铃声播放
     */
    private void startRing() {
        if (null == mMediaPlayHelper || mEnableMuteMode) {
            return;
        }
        if (TextUtils.isEmpty(mCallingBellPath)) {
            mMediaPlayHelper.start(R.raw.ring_tone);
        } else {
            //铃音地址
            mMediaPlayHelper.start(mCallingBellPath);
        }
    }

    /**
     * 挂断或拒绝通话
     */
    private void playHangupMusic() {
        if (null == mMediaPlayHelper) {
            return;
        }
        mMediaPlayHelper.start(R.raw.hang_up, 2000);
    }

}
