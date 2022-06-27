package com.tianxin.tencent.demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.R;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.liteav.debug.GenerateTestUserSig;
import com.tencent.liteav.login.ProfileManager;
/**
 * 后台服务处理
 * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 */
public class CallService extends Service {
    private static final int NOTIFICATION_ID = 1001;
    //启动服务
    public static void start(Context context) {
        if (ServiceUtils.isServiceRunning(CallService.class)) {
            return;
        }
        Intent starter = new Intent(context, CallService.class);
        //解决办法是在AndroidManifest中添加 //Android App兼容8.0和9.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // <!--android 9.0上使用前台服务，需要添加权限-->
            //<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
            context.startForegroundService(starter);
        } else {
            //8.0以下启动服务
            context.startService(starter);
        }
    }

    //停止服务
    public static void stop(Context context) {
        Intent intent = new Intent(context, CallService.class);
        context.stopService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationonCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //绑定前台服务 尚未实施
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //创建服务内容
    private void notificationonCreate() {
        // 获取服务通知 创建通知栏消息
        Notification notification = createForegroundNotification();
        //将服务置于启动状态 ,NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID, notification);

        // 由于两个模块公用一个IM，所以需要在这里先进行登录，您的App只使用一个model，可以直接调用VideoCall 对应函数
        // 目前 Demo 为了方便您接入，使用的是本地签发 sig 的方式，您的项目上线，务必要保证将签发逻辑转移到服务端，否者会出现 key 被盗用，流量盗用的风险。
        if (SessionWrapper.isMainProcess(this)) {
            V2TIMSDKConfig config = new V2TIMSDKConfig();
            config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_NONE);
            V2TIMManager.getInstance().initSDK(this, GenerateTestUserSig.SDKAPPID, config, new V2TIMSDKListener() {
                @Override
                public void onConnecting() {
                }

                @Override
                public void onConnectSuccess() {
                }

                @Override
                public void onConnectFailed(int code, String error) {
                }
            });
        }

        String userId = ProfileManager.getInstance().getUserModel().userId;
        String userSig = ProfileManager.getInstance().getUserModel().userSig;


        // 由于这里提前登陆了，所以会导致上一次的消息收不到，您在APP中单独使用 ITRTCAudioCall.login 不会出现这种问题
        V2TIMManager.getInstance().login(userId, userSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                //2 登录IM失败
                ToastUtils.showLong("登录IM失败，所有功能不可用[" + i + "]" + s);
            }

            @Override
            public void onSuccess() {
                ToastUtils.showLong("登录成功");
            }
        });

    }

    //创建通知栏消息
    private Notification createForegroundNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "夜聊";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("夜聊");

            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.a22);
        //通知标题
        builder.setContentTitle(getString(R.string.app_name));
        //通知内容
        builder.setContentText(getString(R.string.working));
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());

        //创建通知并返回
        return builder.build();
    }

}
