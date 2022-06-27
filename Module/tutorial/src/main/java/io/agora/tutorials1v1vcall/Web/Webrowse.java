package io.agora.tutorials1v1vcall.Web;

import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 向服务器发起请求类
 */
public class Webrowse {

    public static final String pos = "POST";
    public static final String get = "GET";
    private static final int TIMEOUT = 5000;
    private static  final String AGORA=BuildConfig.HTTP_API+"/agora";

    public static void Http(final String method, final Map<String, String> params, final callback callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                submitPostData(method, params, callback);
            }
        }.start();
    }

    /*
     * Function  :   发送Post请求到服务器
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    private static void submitPostData(String method, Map<String, String> params, callback callback) {
        try {
            URL url = new URL(AGORA);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(TIMEOUT);        //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod(method);             //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] data = getRequestData(params, "utf-8").toString().getBytes();//获得请求体
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                String returns = dealResponseResult(inptStream);
                callback.onSuccess(returns);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFail();
        }

    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     * Author    :   博客园-依旧淡然
     */
    private static StringBuffer getRequestData(Map<String, String> params, String encode) {
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
    private static String dealResponseResult(InputStream inputStream) {
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
        }
        return new String(byteArrayOutputStream.toByteArray());
    }


}
