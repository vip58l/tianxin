package com.tencent.liteav;

import static android.net.Uri.parse;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.Camera.MediaPlayHelper;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.model.ITRTCAVCall;
import com.tencent.liteav.model.TRTCAVCallImpl;
import com.tencent.liteav.model.TRTCAVCallListener;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.videolayout.TRTCVideoLayout;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.Glideloads;
import com.tencent.qcloud.tim.uikit.utils.PermissionUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;
import java.util.Map;

/***
 * 弹出悬浮窗
 */
public class FloatingService extends Service {
    private static final String TAG = "FloatingService";
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    View floatView;
    UserModel model;
    MediaPlayHelper mMediaPlayHelper;
    private ITRTCAVCall mITRTCAVCall;
    private ImageView image;
    private TextView name, call_msg;

    /**
     * 监听事件处理
     */
    private TRTCAVCallListener mTRTCAVCallListener = new TRTCAVCallListener() {
        @Override
        public void onError(int code, String msg) {
            Log.d(TAG, "onError: 发生了错误，报错并退出该页面");
            playHangupMusic();
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
            Log.d(TAG, "onUserEnter: 进入通话中");
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
            Log.d(TAG, "作为被邀请方会收到，收到该回调说明本次通话被取消了");
            playHangupMusic();

        }

        @Override
        public void onCallingTimeout() {
            Log.d(TAG, "作为被邀请方会收到，收到该回调说明本次通话超时未应答");
            playHangupMusic();

        }

        @Override
        public void onCallEnd() {
            Log.d(TAG, "收到该回调说明本次通话结束了");

        }

        @Override
        public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
            Log.d(TAG, "true:远端用户打开麦克风  false:远端用户关闭麦克风");

        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
            Log.d(TAG, " true:远端用户打开摄像头  false:远端用户关闭摄像头");
        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {

        }

        @Override
        public void getgiftInfo(GiftInfo giftInfo) {
            Log.d(TAG, "getgiftInfo: 礼物");

        }

        @Override
        public void longrangecall(String json) {
            Log.d(TAG, "longrangecall: 自定义消息");

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayHelper = new MediaPlayHelper(TUIKit.getAppContext());
        mITRTCAVCall = TRTCAVCallImpl.sharedInstance(TUIKit.getAppContext());
        mITRTCAVCall.addListener(mTRTCAVCallListener);
    }

    /**
     * 启动服务
     *
     * @param context
     * @param model
     */
    public static void startBeingCall(Context context, UserModel model) {
        Intent intent = new Intent(context, FloatingService.class);
        intent.putExtra(Constants.json, new Gson().toJson(model));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String json = intent.getStringExtra("json");
        model = new Gson().fromJson(json, UserModel.class);
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取到WindowManager对象
     */
    private void showFloatingWindow() {
        windowManager = (WindowManager) TUIKit.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        //创建一个WindowManager.LayoutParams对象，用于设置一些悬浮view的参数
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.x = 0;
        layoutParams.y = 0;
        layoutParams.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        //悬浮窗弹出的位置
        layoutParams.gravity = Gravity.TOP | Gravity.CENTER;
        //将悬浮view和layoutParams调用windowmanager的方法addView显示出来
        floatView = LayoutInflater.from(TUIKit.getAppContext()).inflate(R.layout.activity_theresa_call, null);

        image = floatView.findViewById(R.id.image);
        name = floatView.findViewById(R.id.name);
        call_msg = floatView.findViewById(R.id.call_msg);
        floatView.setOnTouchListener(new FloatingOnTouchListener());
        floatView.findViewById(R.id.colas).setOnClickListener(v -> {
            mITRTCAVCall.reject();
            playHangupMusic();
        });
        floatView.findViewById(R.id.agree).setOnClickListener(v -> actTRTCVideoCallActivity());
        Glideloads.loadImage(image, model.userAvatar);
        name.setText(String.format(getString(R.string.call_a1), model.userName));
        if (UserInfo.getInstance().getSex().equals("2")) {
            call_msg.setText(R.string.tm_call_s4);
        }
        windowManager.addView(floatView, layoutParams);
        startDialingMusic();
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    // 更新悬浮窗控件布局最新位置
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 接听通话
     */
    public void actTRTCVideoCallActivity() {
        model.floating = true;//进入自动接听操作
        TRTCVideoCallActivity.startBeingCall(TUIKit.getAppContext(), model, null);
        stopService(new Intent(TUIKit.getAppContext(), FloatingService.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mITRTCAVCall.removeListener(mTRTCAVCallListener);
        windowManager.removeView(floatView);
        stopMusic();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
            mMediaPlayHelper = null;
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
        stopService(new Intent(TUIKit.getAppContext(), FloatingService.class));
    }


}