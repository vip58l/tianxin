/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/5/1 0001
 */


package com.tianxin.Test;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class maTimeRunnable extends AppCompatActivity {
    private static final String TAG = "maTimeRunnable";
    private Runnable mTimeRunnable;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inihandler();
    }

    public void inihandler() {
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();

        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        smTimeRunnable();
    }

    public void smTimeRunnable() {
        mTimeRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "mTimeRunnable:1111");
                srunOnUiThread();
            }
        };

        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    public void srunOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "runOnUiThread:2222");

            }
        });
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }
}

