package com.tianxin.Util;

import android.util.Log;
import android.view.View;

import com.facebook.common.logging.FLogDefaultLoggingDelegate;
import com.tianxin.BuildConfig;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 打印输出
 */
public class Logi {

    public static void d(String TAG,String msg){
        if (BuildConfig.DEBUG){
            Log.d(TAG, msg);
        }

    }
    public static void d(String TAG,int msg){
        if (BuildConfig.DEBUG){
            Log.d(TAG, String.valueOf(msg));
        }

    }
}
