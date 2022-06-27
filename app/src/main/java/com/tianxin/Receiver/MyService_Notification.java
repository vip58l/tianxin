/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/22 0022
 */


package com.tianxin.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.tianxin.R;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.app.DemoApplication;

/**
 * 创建前台服务通知 确保服务在前台运行
 */
public class MyService_Notification extends Service {
    private String TAG = MyService_Notification.class.getSimpleName();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        // 获取服务通知
        Notification notification = createForegroundNotification1();

        // 获取服务通知
        //Notification notification = createForegroundNotification2();

        //将服务置于启动状态 ,NOTIFICATION_ID 通知的ID
        startForeground(123, notification);
    }

    /**
     * 创建前台服务通知 确保服务在前台运行
     */
    private Notification createForegroundNotification1() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 唯一的通知通道的id.
        String notificationChannelId = "notification_channel_id_01";
        // Android8.0以上的系统，新建消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //用户可见的通道名称
            String channelName = "Foreground Service Notification";
            //通道的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId, channelName, importance);
            notificationChannel.setDescription("Channel description");
            //LED灯
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //震动
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
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
     * android 9适配通知栏
     *
     * @param context
     * @return
     */
    public static String createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channelId";
            CharSequence channelName = "channelName";
            String channelDescription = "channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            return channelId;
        } else {
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 服务停止了");
        //移除状态栏通知
        stopForeground(true);

    }


}
