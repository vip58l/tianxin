package com.tianxin.Receiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.tianxin.app.DemoApplication;

public class configReceServ {
    DemoApplication instance;

    public configReceServ() {
        instance = DemoApplication.instance();
        StartService();         //启动服务
    }

    /**
     * 启动后台服务设置为前台服务
     */
    public void StartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //启动后台服务设置为前台服务
            instance.startForegroundService(new Intent(instance, MyService_Notification.class));
        } else {
            instance.startService(new Intent(instance, MyService_Notification.class));
        }
    }

    /**
     * 停止后台服务
     */
    public void StopService() {
        Intent intent = new Intent(instance, MyService_Notification.class);
        instance.stopService(intent);
    }

    /**
     * 动态注册广播接收者 代码注册：
     * 态注册时，无须在AndroidManifest中注册<receiver/>组件
     */
    public void Receiver1() {

        //过滤器
        IntentFilter intentFilter = new IntentFilter();
        //设置过滤监听的是值为xxx
        intentFilter.addAction("BROADCAST_ACTION");
        //使用registerReceiver函数注册广播接收器
        Receiver2 receiver2 = new Receiver2();
        instance.registerReceiver(receiver2, intentFilter);
    }

    /**
     * 动态注册广播接收者 代码注册：
     * 态注册时，无须在AndroidManifest中注册<receiver/>组件
     */
    public void Receiver2() {
        Receiver1 receiver1 = new Receiver1();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Receiver1.class.getSimpleName());
        instance.registerReceiver(receiver1, intentFilter);
    }

    /**
     * 发送广播
     */
    public void SendPost() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("com.scott.sayhi");
        intent.putExtra("username", "name");
        intent.putExtra("password", "pwd");
        DemoApplication.instance().sendBroadcast(intent);
    }
}
