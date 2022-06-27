package com.tianxin.Receiver;

import static com.tianxin.Receiver.MyService_Notification.createNotificationChannel;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.utils.MySocket;
import com.tianxin.activity.video2.activity.activity_video;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;

/**
 * 服务检测虚拟视频呼叫视频通话
 */
public class videoService extends Service {
    private String TAG = videoService.class.getSimpleName();
    private Handler myHandler;
    private int delayMillis = 30000;

    @Override
    public void onCreate() {
        super.onCreate();
        myHandler = new myHandler();
        myHandler.sendEmptyMessageDelayed(Config.sussess, delayMillis);

        // 获取服务通知
        //Notification notification = createForegroundNotification1();

        // 获取服务通知
        //Notification notification = createForegroundNotification2();

        //获取服务通知
        Notification notification = createForegroundNotification3();

        //将服务置于启动状态 ,NOTIFICATION_ID 通知的ID
        startForeground(123, notification);

        //用户在线发送心跳包Alive...
        try {
            if (!MySocket.getMySocket().isAlive()) {
                MySocket.getMySocket().start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "启动命令: ");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "服务停止了");
        stopForeground(true);
        myHandler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    private class myHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    Log.d(TAG, "服务检测呼叫视频通话: ");
                    Datamodule.getInstance().tovideocall(paymnets);

                    //随机30秒-3分钟
                    int random = (int) (Math.random() * 5 + 1);
                    int delayMillisi = delayMillis * random;
                    Log.d(TAG, String.format("等待:%s秒", delayMillisi / 1000));
                    sendEmptyMessageDelayed(Config.sussess, delayMillisi);
                    break;
            }
        }
    }

    public class MyBinder extends Binder {
        public int getdelayMillis() {
            return delayMillis;
        }

    }

    /**
     * 创建前台服务通知 确保服务在前台运行
     */
    private Notification createForegroundNotification1() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";
        //用户可见的通道名称
        String channelName = getString(R.string.tv_msg63);
        String description = getString(R.string.app_name);

        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //通道的重要程度
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(description);

//            //LED灯
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            //震动
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(DemoApplication.instance(), notificationChannelId);
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle(String.format("%s%s", getString(R.string.app_name), getString(R.string.tv_msg195)));
        //通知内容
        builder.setContentText(getString(R.string.tv_msg194));
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());

        //设定启动的内容
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        //创建通知并返回
        return builder.build();
    }

    /**
     * 服务设置为前台 否则在后台运行 容易被干掉
     *
     * @return
     */
    public Notification createForegroundNotification2() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DemoApplication.instance(), 123, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), createNotificationChannel(getApplicationContext()));
        Notification notification = notificationCompatBuilder
                //设置通知栏中显示的标题
                .setContentTitle(getString(R.string.app_name))
                //设置通知内容
                .setContentText(getString(R.string.app_name) + getString(R.string.tv_msg63))
                //设置显示的小文件内容
                .setTicker(getString(R.string.app_name) + getString(R.string.tv_msg63)) //安卓5.0以上系统notification中setTicker无效
                //设置通知左侧的小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
                .setContentInfo("2")
                //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
                .setNumber(20)
                //设置通知右侧的大图标
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置图标color
                //.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                //设置点击通知时的响应事件 关联点击通知栏跳转页面
                .setContentIntent(pendingIntent)
                //设置删除通知时的响应事件
                .setDeleteIntent(pendingIntent)
                //设置显示通知时间
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())//通知栏显示时间
                //设置正在进行中
                .setOngoing(true)
                //设置点击后自动自动删除通知
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)//通知栏提示音、震动、闪灯等都设置为默认
                .setCategory(Notification.CATEGORY_REMINDER)
                //.setVibrate(new long[]{0, 1000, 1000, 1000}) //通知栏消息震动
                .setLights(Color.GREEN, 1000, 2000) //通知栏消息闪灯(亮一秒间隔两秒再亮)
                .build();

        return notification;
    }

    /**
     * 服务设置为前台 否则在后台运行 防止被Kii干掉
     *
     * @return
     */
    public Notification createForegroundNotification3() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        //通知通道的id.
        String notificationChannelId = "notification_channel_id_01";
        //通道名称
        String channelName = getString(R.string.tv_msg63);
        //频道描述
        String description = getString(R.string.app_name);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(notificationChannelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(DemoApplication.instance(), notificationChannelId);
            return getnotification(builder);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(DemoApplication.instance(), notificationChannelId);
            return getnotification(builder);
        }


    }

    private Notification getnotification(Notification.Builder builder) {
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle(String.format("%s%s", getString(R.string.app_name), getString(R.string.tv_msg195)));
        //通知内容
        builder.setContentText(getString(R.string.tv_msg194));
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());

        //跳转启动的Activity
        Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DemoApplication.instance(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    private Notification getnotification(NotificationCompat.Builder builder) {
        //通知小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //通知标题
        builder.setContentTitle(String.format("%s%s", getString(R.string.app_name), getString(R.string.tv_msg195)));
        //通知内容
        builder.setContentText(getString(R.string.tv_msg194));
        //设定通知显示的时间
        builder.setWhen(System.currentTimeMillis());

        //跳转启动的Activity
        Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(DemoApplication.instance(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    /**
     * 检测服务是否正在运行
     *
     * @param context
     * @param service_Name
     * @return
     */
    public static boolean isServiceRunning(Context context, String service_Name) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service_Name.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(String msg) {
            //配合服务发起视频通话标识
            if (activity_video.call_state == 0) {
                activity_video.starsetAction(getApplicationContext(), msg);
            }

        }
    };
}