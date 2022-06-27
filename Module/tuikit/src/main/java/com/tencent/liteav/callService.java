package com.tencent.liteav;

import static com.tencent.liteav.model.ITRTCAVCall.TYPE_AGORA_CALL;
import static com.tencent.liteav.model.ITRTCAVCall.TYPE_AUDIO_CALL;
import static com.tencent.liteav.model.ITRTCAVCall.TYPE_VIDEO_CALL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.utils.HttpUtils;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;

import java.util.ArrayList;
import java.util.List;

/**
 * 主播挂启循环自动呼叫
 */
public class callService extends Service {
    String TAG = callService.class.getSimpleName();
    private myHandler myHandler = new myHandler();
    private final int what = 200;
    private final long delayMillis = 10000;
    private int video_call;
    //标记用于通话中true 空闲状态false
    public static boolean isboocall;
    private UserInfo userInfo;
    private UserModel mSearchModel;
    private Gson gson;
    private member member;

    /**
     * 启动服务
     *
     * @param model
     */
    public static void startCall(UserModel model) {
        Intent intent = new Intent(TUIKit.getAppContext(), callService.class);
        intent.putExtra(Constants.json, new Gson().toJson(model));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TUIKit.getAppContext().startService(intent);
    }

    /**
     * 启动服务
     *
     * @param model
     */
    public static void startCall(Context context, UserModel model) {
        Intent intent = new Intent(context, callService.class);
        intent.putExtra(Constants.json, new Gson().toJson(model));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(intent);
    }

    /**
     * 停止服务
     */
    public static void callstopService() {
        Intent intent = new Intent(TUIKit.getAppContext(), callService.class);
        TUIKit.getAppContext().stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 主播设置呼叫视频已开启");
        userInfo = UserInfo.getInstance();
        myHandler.sendEmptyMessageDelayed(what, delayMillis);
        gson = new Gson();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand连接到服务");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "重新绑定服务onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务已停止: ");
        myHandler.removeMessages(what);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

    }

    private class myHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case what:
                    video_call++;
                    if (!isboocall) {
                        Log.d(TAG, "handleMessage: " + video_call);
                        Postdeduction.getlistmember(httpListener);
                    }
                    sendEmptyMessageDelayed(what, delayMillis);
                    break;
            }
        }
    }

    /**
     * 获取服务端返回结果
     */
    private HttpUtils.HttpListener httpListener = new HttpUtils.HttpListener() {
        @Override
        public void success(String response) {
            try {
                Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                if (mesresult.isSuccess()) {
                    isboocall = true;
                    member = gson.fromJson(mesresult.getData(), member.class);
                    searchContactsByPhone(String.valueOf(member.getId()), 2);
                } else {
                    Log.d(TAG, "success: " + mesresult.getMsg());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailed(String message) {
            Log.d(TAG, "onFailed: " + message);
        }
    };

    /**
     * 搜索查询对方帐号是否存在腾讯IM中
     *
     * @param phoneNumber
     * @param type
     */
    private void searchContactsByPhone(String phoneNumber, int type) {
        ProfileManager.getInstance().getUserInfoByUserId(phoneNumber, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                mSearchModel = model;
                mSearchModel.userName = !TextUtils.isEmpty(member.getTruename()) ? member.getTruename() : mSearchModel.userName;
                mSearchModel.userAvatar = !TextUtils.isEmpty(member.getPicture()) ? member.getPicture() : mSearchModel.userAvatar;
                mSearchModel.Gender = 2;                                    //性别
                mSearchModel.tRole = 1;                                     //角色 0接收 1拨打
                mSearchModel.Level = member.getLevel();                     //级别
                mSearchModel.AllowType = 0;                                 //允许类型
                mSearchModel.phone = !TextUtils.isEmpty(member.getMobile()) ? member.getMobile() : "";         //电话
                startCallSomeone(type);                                    //开始呼叫对方
                Log.d(TAG, "正在呼叫视频:" + mSearchModel.userId + " " + mSearchModel.userName);
            }

            @Override
            public void onFailed(int code, String msg) {
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    /**
     * 开始呼叫某人
     */
    private void startCallSomeone(int mType) {
        isboocall = true;
        //呼叫方（即自己）的资料
        UserModel mSelfModel = ProfileManager.getInstance().getUserModel();
        UserModel selfInfo = new UserModel();
        selfInfo.userId = !TextUtils.isEmpty(mSelfModel.userId) ? mSelfModel.userId : userInfo.getUserId();
        selfInfo.userAvatar = !TextUtils.isEmpty(mSelfModel.userAvatar) ? mSelfModel.userAvatar : userInfo.getAvatar();
        selfInfo.userName = !TextUtils.isEmpty(mSelfModel.userName) ? mSelfModel.userName : userInfo.getName();
        selfInfo.Gender = Integer.parseInt(userInfo.getSex());
        selfInfo.token = userInfo.getToken();
        selfInfo.phone = userInfo.getPhone();
        selfInfo.tRole = 1;                  //角色 0接收 1拨打
        selfInfo.Level = userInfo.getLevel();//级别
        selfInfo.AllowType = 0;              //允许类型
        //自己的资料设置到一个静态变量中
        ProfileManager.getInstance().setUserModel(selfInfo);

        //接听收方(对方)的资料
        List<UserModel> models = new ArrayList<>();
        UserModel callUserInfo = new UserModel();
        callUserInfo.userId = mSearchModel.userId;
        callUserInfo.userAvatar = mSearchModel.userAvatar;
        callUserInfo.userName = mSearchModel.userName;
        callUserInfo.Gender = mSearchModel.Gender;
        callUserInfo.tRole = mSearchModel.tRole;
        callUserInfo.Level = mSearchModel.Level;
        callUserInfo.AllowType = mSearchModel.AllowType;
        callUserInfo.phone = mSearchModel.phone;
        models.add(callUserInfo);

        switch (mType) {
            case TYPE_AUDIO_CALL:
                TRTCAudioCallActivity.startCallSomeone(TUIKit.getAppContext(), models);
                break;
            case TYPE_VIDEO_CALL:
                TRTCVideoCallActivity.startCallSomeone(TUIKit.getAppContext(), models);
                break;
            case TYPE_AGORA_CALL:
                //声网视频
                //CallVideo.startCallSomeone(context, models);
                break;
        }

    }

}
