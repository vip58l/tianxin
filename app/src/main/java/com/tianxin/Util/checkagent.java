package com.tianxin.Util;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.tianxin.app.DemoApplication;

import java.io.IOException;
import java.net.Proxy;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class checkagent {
    private static String TAG = checkagent.class.getSimpleName();

    /*
     * 判断设备 是否使用代理上网 有代理返回 true 无代理返回 false
     * */
    public static boolean isWifiProxy1() {
        Context context = DemoApplication.instance();
        final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
        String proxyAddress;
        int proxyPort;
        if (IS_ICS_OR_LATER) {
            proxyAddress = System.getProperty("http.proxyHost");
            String portStr = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(context);
            proxyPort = android.net.Proxy.getPort(context);
        }
        return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
    }

    public void run(Context context) {
        Looper.prepare();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                proxy(Proxy.NO_PROXY).
                build();
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
            Toast.makeText(context, Objects.requireNonNull(response.body()).string(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Looper.loop();
    }
}
