package com.tianxin.Util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tianxin.R;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;

public class Toashow {
    private static Toast makeText;

    public static void show(int Smg) {
        if (makeText != null) {
            makeText.cancel();
            makeText = null;
        }
        View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
        TextView tv1 = inflate.findViewById(R.id.tv1);
        tv1.setText(String.valueOf(Smg));
        makeText = Toast.makeText(DemoApplication.instance(), String.valueOf(Smg), Toast.LENGTH_SHORT);
        makeText.setView(inflate);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();

    }

    public static void show(String Smg) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (makeText != null) {
                    makeText.cancel();
                    makeText = null;
                }
                View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
                TextView tv1 = inflate.findViewById(R.id.tv1);
                tv1.setText(String.valueOf(Smg));
                makeText = Toast.makeText(DemoApplication.instance(), Smg, Toast.LENGTH_SHORT);
                makeText.setView(inflate);
                makeText.setGravity(Gravity.CENTER, 0, 0);
                makeText.show();
            }

        });
    }

    public static void show(Context context, int Smg) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (makeText != null) {
                    makeText.cancel();
                    makeText = null;
                }
                View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
                TextView tv1 = inflate.findViewById(R.id.tv1);
                tv1.setText(String.valueOf(Smg));
                makeText = Toast.makeText(DemoApplication.instance(), Smg, Toast.LENGTH_SHORT);
                makeText.setView(inflate);
                makeText.setGravity(Gravity.BOTTOM, 0, 0);
                makeText.show();
            }

        });
    }

    public static void show(Context context, String Smg) {
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (makeText != null) {
                    makeText.cancel();
                    makeText = null;
                }
                View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
                TextView tv1 = inflate.findViewById(R.id.tv1);
                tv1.setText(String.valueOf(Smg));
                makeText = Toast.makeText(DemoApplication.instance(), Smg, Toast.LENGTH_SHORT);
                makeText.setView(inflate);
                makeText.setGravity(Gravity.BOTTOM, 0, 0);
                makeText.show();
            }

        });
    }

    public static void showShort(int Smg) {
        if (makeText != null) {
            makeText.cancel();
            makeText = null;
        }
        View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
        TextView tv1 = inflate.findViewById(R.id.tv1);
        tv1.setText(String.valueOf(Smg));
        makeText = Toast.makeText(DemoApplication.instance(), String.valueOf(Smg), Toast.LENGTH_SHORT);
        makeText.setView(inflate);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();

    }

    public static void showShort(String Smg) {
        if (makeText != null) {
            makeText.cancel();
            makeText = null;
        }
        View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
        TextView tv1 = inflate.findViewById(R.id.tv1);
        tv1.setText(String.valueOf(Smg));
        makeText = Toast.makeText(DemoApplication.instance(), String.valueOf(Smg), Toast.LENGTH_SHORT);
        makeText.setView(inflate);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();

    }

    public static void toastMessage(int Smg) {
        if (makeText != null) {
            makeText.cancel();
            makeText = null;
        }
        View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
        TextView tv1 = inflate.findViewById(R.id.tv1);
        tv1.setText(String.valueOf(Smg));
        makeText = Toast.makeText(DemoApplication.instance(), String.valueOf(Smg), Toast.LENGTH_SHORT);
        makeText.setView(inflate);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();
    }

    public static void toastMessage(String Smg) {
        if (makeText != null) {
            makeText.cancel();
            makeText = null;
        }
        View inflate = View.inflate(DemoApplication.instance(), R.layout.toastview, null);
        TextView tv1 = inflate.findViewById(R.id.tv1);
        tv1.setText(String.valueOf(Smg));
        makeText = Toast.makeText(DemoApplication.instance(), String.valueOf(Smg), Toast.LENGTH_SHORT);
        makeText.setView(inflate);
        makeText.setGravity(Gravity.CENTER, 0, 0);
        makeText.show();

    }


}