package com.tianxin.Receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.app.DemoApplication;

public class FloatingService extends Service {
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams;
    View floatView;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showFloatingWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取到WindowManager对象
            windowManager = (WindowManager) DemoApplication.instance().getSystemService(Context.WINDOW_SERVICE);
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
            floatView = LayoutInflater.from(DemoApplication.instance()).inflate(R.layout.activity_theresa_call, null);
            floatView.setOnTouchListener(new FloatingOnTouchListener());
            floatView.findViewById(R.id.colas).setOnClickListener(v -> actTRTCVideoCallActivity());
            floatView.findViewById(R.id.agree).setOnClickListener(v -> actTRTCVideoCallActivity());
            windowManager.addView(floatView, layoutParams);
        }
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

    public void actTRTCVideoCallActivity() {
        windowManager.removeView(floatView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(floatView);
    }
}