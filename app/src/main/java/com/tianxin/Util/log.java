package com.tianxin.Util;

import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 打印输出
 */
public class log {

    private static final String Tag = "tag";
    private static boolean yes = true;

    public static void setYes(boolean y) {
        yes = y;
    }

    public static boolean isYes() {
        return yes;
    }

    public static void d(String msg) {
        if (!yes) return;
        Log.d(Tag, msg);
    }

    public static void d(float msg) {
        if (!yes) return;
        Log.d(Tag, String.valueOf(msg));
    }

    public static void d(Boolean msg) {
        if (!yes) return;
        Log.d(Tag, String.valueOf(msg));
    }

    public static void d(double msg) {
        if (!yes) return;
        Log.d(Tag, String.valueOf(msg));
    }

    public static void d(int msg) {
        if (!yes) return;
        Log.d(Tag, String.valueOf(msg));
    }

    public static void d(View msg) {
        if (!yes) return;
        Log.d(Tag, String.valueOf(msg));
    }

    public static void d(JSONObject msg) {
        if (!yes) return;
        Log.d(Tag, msg.toString());
    }

    public static void d(JSONArray msg) {
        if (!yes) return;
        Log.d(Tag, msg.toString());
    }

    public static void d(String TAG, String msg) {
        if (!yes) return;
        Log.d(TAG, msg);
    }

    public static void d(String TAG, float msg) {
        if (!yes) return;
        Log.d(TAG, String.valueOf(msg));
    }

    public static void d(String TAG, Boolean msg) {
        if (!yes) return;
        Log.d(TAG, String.valueOf(msg));
    }

    public static void d(String TAG, double msg) {
        if (!yes) return;
        Log.d(TAG, String.valueOf(msg));
    }

    public static void d(String TAG, int msg) {
        if (!yes) return;
        String s = String.valueOf(msg);
        Log.d(TAG, s);
    }

    public static void d(String TAG, JSONObject msg) {
        if (!yes) return;
        Log.d(TAG, msg.toString());
    }

    public static void d(String TAG, JSONArray msg) {
        if (!yes) return;
        Log.d(TAG, msg.toString());
    }


}
