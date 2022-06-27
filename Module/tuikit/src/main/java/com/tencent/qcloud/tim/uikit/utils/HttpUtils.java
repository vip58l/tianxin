/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/24 0024
 */


package com.tencent.qcloud.tim.uikit.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 封装POST请求提交数据
 */
public class HttpUtils implements Runnable {
    public static final int TIMEOUT = 30000;
    private final HttpListener mHttpListener;
    private final String mUrl;
    private final Map<String, String> mparams;

    /**
     * 构造方法
     * @param url
     * @param params
     * @param httpListener
     */
    public HttpUtils(String url, Map<String, String> params, HttpListener httpListener) {
        mHttpListener = httpListener;
        mUrl = url;
        mparams = params;
    }

    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    private String submitPostData(Map<String, String> params, String encode) {
        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {
            URL url = new URL(mUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(TIMEOUT);        //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");          //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int code = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);//处理服务器的响应结果

            } else {
                //回调错误消息
                mHttpListener.onFailed(httpURLConnection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            mHttpListener.onFailed(e.getMessage());
        }
        return "";
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    private StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     * Author    :   博客园-依旧淡然
     */
    private String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
            mHttpListener.onFailed(e.getMessage());
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * 请求网络数据
     *
     * @param url
     * @param params
     * @param httpListener
     */
    public static synchronized void RequestPost(String url, Map<String, String> params, HttpListener httpListener) {
        //创建子线程
        new Thread(new HttpUtils(url, params, httpListener)).start();
    }

    @Override
    public void run() {
        mHttpListener.success(submitPostData(mparams, "utf-8"));
    }

    /**
     * 定义一个拉口回调数据
     */
    public interface HttpListener {

        void success(String response);

        void onFailed(String message);
    }
}
