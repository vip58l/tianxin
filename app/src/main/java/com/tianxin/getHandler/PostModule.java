package com.tianxin.getHandler;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.tianxin.Util.checkagent;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.getlist.okhttp;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.io.File;
import java.util.Map;

import okhttp3.RequestBody;

public class PostModule extends Handler {
    private Paymnets paymnets;

    private final String TAG = PostModule.class.getSimpleName();

    public PostModule(String path, Paymnets paymnets) {
        this.paymnets = paymnets;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = okhttp.get(path);
                    Message message = new Message();
                    message.obj = response;
                    message.what = Config.sussess;
                    sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public PostModule(String path, RequestBody requestBody) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    String date = okhttp.POST(path, requestBody);
                    Message message = new Message();
                    message.what = Config.sussess;
                    message.obj = date;
                    sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendEmptyMessage(Config.fail);
                }
            }
        }.start();
    }

    public PostModule(Map<String, String> hashMap, File file) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                okhttp.okhttp_image(hashMap, file, PostModule.this);
            }
        }.start();
    }

    public PostModule(Map<String, String> hashMap, RequestBody requestBody) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                okhttp.okhttpimage(hashMap, requestBody, PostModule.this);
            }
        }.start();
    }

    /**
     * get请求数据
     *
     * @param path
     * @param paymnets
     */
    public static void getModule(String path, Paymnets paymnets) {
        if (!BuildConfig.isWifiProxy) {
            //判断添加了网络代理或网络异常返回
            if (!Config.isNetworkAvailable() || checkagent.isWifiProxy1()) {
                paymnets.isNetworkAvailable();
                return;
            }
        }
        PostModule module = new PostModule(path, paymnets);
        module.paymnets = paymnets;
    }

    /**
     * 发送POST数据
     *
     * @param path
     * @param requestBody
     * @param paymnets
     */
    public static void postModule(String path, RequestBody requestBody, Paymnets paymnets) {
        if (!BuildConfig.isWifiProxy) {
            //判断添加了网络代理或网络异常返回
            if (!Config.isNetworkAvailable() || checkagent.isWifiProxy1()) {
                paymnets.isNetworkAvailable();
                return;
            }
        }
        PostModule module = new PostModule(path, requestBody);
        module.paymnets = paymnets;
    }

    /**
     * 提交上传文件
     *
     * @param file
     * @param hashMap
     * @param paymnet
     */
    public static void okhttpimage(Map<String, String> hashMap, File file, Paymnets paymnet) {
        /**
         * 代理不请求数据
         */
        if (!BuildConfig.isWifiProxy) {
            if (checkagent.isWifiProxy1()) {
                return;
            }
        }
        PostModule module = new PostModule(hashMap, file);
        module.paymnets = paymnet;

    }

    /**
     * 提交上传文件
     *
     * @param hashMap
     * @param requestBody
     * @param paymnet
     */
    public static void okhttpimage(Map<String, String> hashMap, RequestBody requestBody, Paymnets paymnet) {
        if (!Config.isNetworkAvailable()) {
            paymnet.isNetworkAvailable();
            return;
        }
        if (!BuildConfig.isWifiProxy) {
            //判断添加了网络代理或网络异常返回
            if (checkagent.isWifiProxy1()) {
                return;
            }
        }
        PostModule module = new PostModule(hashMap, requestBody);
        module.paymnets = paymnet;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case Config.sussess:
                String s = msg.obj.toString();
                if (!TextUtils.isEmpty(s)) {
                    paymnets.success(s);
                }
                break;
            case Config.fail:
                paymnets.fall(400);
                break;
        }

    }

}
