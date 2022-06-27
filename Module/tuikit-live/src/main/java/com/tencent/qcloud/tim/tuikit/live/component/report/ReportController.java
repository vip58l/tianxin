package com.tencent.qcloud.tim.tuikit.live.component.report;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Config;
import android.util.Log;
import android.widget.Toast;

import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 发送用户举报信息
 */
public class ReportController {
    private static final String TAG = "ReportController";
    private static final String REPORT_URL = "http://demo.vod2.myqcloud.com/lite/report_user";
    private static final String REPORT_URL2 = BuildConfig.HTTP_API + "/report"; //提交给后台服务端接收
    private static final String PARAM_USER_ID = "userid";
    private static final String PARAM_HOST_USER_ID = "hostuserid";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_VIDEO = "video";
    private static final String PARAM_TYPE = "type";
    private static final String[] REPORT_ITEMS = {"色情低俗涉黄", "不正当虚假交易", "未成年人不适当行为", "泄露我的隐私", "主页虚假资料", "诱导他人犯罪", "此人是骗子骗钱", "其他问题"};

    public String[] getReportItems() {
        return REPORT_ITEMS;
    }

    public void reportUser(String selfUserId, String reportUserId, String reportContent, String video, int type) {
        if (TextUtils.isEmpty(selfUserId) || TextUtils.isEmpty(reportUserId) || TextUtils.isEmpty(reportContent)) {
            return;
        }
        ReportUserTask task = new ReportUserTask();
        task.execute(selfUserId, reportUserId, reportContent, video, String.valueOf(type));
    }

    private class ReportUserTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            String selfUserId = (String) objects[0];
            String reportUserId = (String) objects[1];
            String reportContent = (String) objects[2];
            String video = (String) objects[3];
            String type = (String) objects[4];
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            JSONObject jsonObject = new JSONObject();
            boolean success = false;
            try {
                jsonObject.put(PARAM_USER_ID, reportUserId);
                jsonObject.put(PARAM_HOST_USER_ID, selfUserId);
                jsonObject.put(PARAM_REASON, reportContent);
                jsonObject.put(PARAM_VIDEO, video);
                jsonObject.put(PARAM_TYPE, type);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Request request = new Request.Builder().url(REPORT_URL2).post(body).build();
                Response response = client.newCall(request).execute();
                success = response.isSuccessful();
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Object result) {
            boolean success = (boolean) result;

            Context context = TUIKitLive.getAppContext();
            Toast.makeText(context, context.getText(R.string.report_success), Toast.LENGTH_SHORT).show();
        }
    }
}
