package com.tencent.qcloud.tim.uikit.utils;

import android.os.Handler;
import android.os.Looper;


public class BackgroundTasks {

    private static BackgroundTasks instance;
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public static BackgroundTasks getInstance() {
        return instance;
    }

    // 需要在主线程中初始化
    public static void initInstance() {
        instance = new BackgroundTasks();
    }

    public void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return mHandler.postDelayed(r, delayMillis);
    }

    public Handler getHandler() {
        return mHandler;
    }


}
