package com.tianxin.getlist;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;

import com.tencent.opensource.model.UserInfo;
import com.tianxin.Util.Config;
import com.tianxin.Util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class okhttp {
    public final static String TAG = okhttp.class.getName();
    public final static OkHttpClient client = new OkHttpClient();
    public final static String cookie = "cookie";

    /**
     * POST提交文件
     */
    public static void okhttp_File(String path, File file) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(path)
                .post(RequestBody.create(mediaType, file))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    Log.d(TAG, headers.name(i) + ":" + headers.value(i));
                }
            }

        });
    }

    /**
     * POST提交文件
     */
    public static synchronized void okhttp_image(Map<String, String> params, File file, Handler handler) {
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        //2.创建RequestBody
        RequestBody fileBody = RequestBody.create(MEDIA_TYPE_PNG, file);
        //3.构建MultipartBody
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.getName(), fileBody)
                .addFormDataPart("userid", params.get("userid"))
                .build();
        //4.构建请求
        Request request = new Request.Builder().url(params.get("path")).post(requestBody).build();
        //5.发送请求
        try {
            Response response = client.newCall(request).execute();
            String date = response.body().string();
            if (TextUtils.isEmpty(date)) {
                handler.sendEmptyMessage(Config.fail);
                return;
            }
            Message message = new Message();
            message.what = Config.sussess;
            message.obj = date;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(Config.fail);
        }

    }

    /**
     * 提交上传文件
     *
     * @param params
     * @param requestBody
     * @param handler
     */
    public static synchronized void okhttpimage(Map<String, String> params, RequestBody requestBody, Handler handler) {
        //4.构建请求
        Request request = new Request.Builder().url(params.get("path")).post(requestBody).build();
        //5.发送请求
        try {

            Response response = client.newCall(request).execute();
            String date = response.body().string();
            if (TextUtils.isEmpty(date)) {
                handler.sendEmptyMessage(Config.fail);
                return;
            }
            Message message = new Message();
            message.what = Config.sussess;
            message.obj = date;
            handler.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(Config.fail);
        }

    }

    /**
     * POST方式提交表单
     */
    public static String POST(String path, RequestBody requestBody) {
        String cookie = CookieManager.getInstance().getCookie(okhttp.cookie);
        Request request = new Request.Builder()
                .addHeader(okhttp.cookie, TextUtils.isEmpty(cookie) ? "" : cookie)
                .addHeader(Constants.accessToken, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .url(path)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            getcookieStr(response);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     * POST方式提交表单
     */
    public static String POST2(String path, RequestBody requestBody) {
        String cookie = CookieManager.getInstance().getCookie(okhttp.cookie);
        Request request = new Request.Builder()
                .addHeader(okhttp.cookie, TextUtils.isEmpty(cookie) ? "" : cookie)
                .addHeader(Constants.accessToken, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .url(path)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            getcookieStr(response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     * get请求数据
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        String cookie = CookieManager.getInstance().getCookie(okhttp.cookie);
        Request request = new Request.Builder()
                .addHeader(okhttp.cookie, TextUtils.isEmpty(cookie) ? "" : cookie) //同步cookie
                .addHeader(Constants.accessToken, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            getcookieStr(response);
            String str = response.body().string();
            return str;
        } catch (IOException e) {
            e.printStackTrace();

        }
        return "";
    }

    /**
     * get请求数据
     *
     * @param url
     * @return
     */
    public static Response response(String url) {
        String cookie = CookieManager.getInstance().getCookie(okhttp.cookie);
        Request request = new Request.Builder()
                .addHeader(okhttp.cookie, TextUtils.isEmpty(cookie) ? "" : cookie) //同步cookie
                .addHeader(Constants.accessToken, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .url(url)
                .build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * get请求数据
     *
     * @param url
     * @return
     */
    public static Call call(String url) {
        String cookie = CookieManager.getInstance().getCookie(okhttp.cookie);
        Request request = new Request.Builder()
                .addHeader(okhttp.cookie, TextUtils.isEmpty(cookie) ? "" : cookie) //同步cookie
                .addHeader(Constants.accessToken, TextUtils.isEmpty(UserInfo.getInstance().getToken()) ? "" : UserInfo.getInstance().getToken())
                .url(url)
                .build();
        try {
            return client.newCall(request);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 获取用户的setCookie
     *
     * @param response
     */
    public static void getcookieStr(Response response) {
        StringBuilder cookieStr = new StringBuilder();
        CookieManager cookieManager = CookieManager.getInstance();
        List<String> headers1 = response.headers("Set-Cookie");
        for (String header : headers1) {
            cookieManager.setCookie(cookie, header);
            cookieStr.append(header);
        }
    }

    /**
     * 获取用户的setCookie
     *
     * @param request
     * @param response
     */
    public static void getcookieStr(Request request, Response response) {
        Headers headers = response.headers();
        HttpUrl loginUrl = request.url();
        StringBuilder cookieStr = new StringBuilder();
        List<Cookie> cookies = Cookie.parseAll(loginUrl, headers);
        for (Cookie cookie : cookies) {
            cookieStr.append(cookie.name()).append("=").append(cookie.value() + ";");
        }

        CookieManager.getInstance().removeAllCookie();
        CookieManager.getInstance().setCookie("cookie1", cookieStr.toString());
    }

}
