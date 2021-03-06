package com.tianxin.activity.DouYing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.activity.video2.activity.PagerPlay;
import com.tianxin.app.DemoApplication;
import com.tianxin.getlist.okhttp;
import com.tianxin.widget.Backtitle;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.costransferpractice.transfer.UploadActivity;
import com.tencent.qcloud.costransferpractice.utils.CacheUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

import static com.tencent.qcloud.costransferpractice.common.base.BaseActivity.mcontext;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_STATE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_TYPE;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_USERID;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIDEO;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIP;

/**
 * ??????????????????
 */
public class activity_jsonvideo extends AppCompatActivity {
    private static final String TAG = activity_jsonvideo.class.getSimpleName();
    private EditText editText;
    private TextView tvResult, tvmax;
    private KWebView webView;
    private Button button1;
    private Button button2;
    private Backtitle backtitle;
    private ProgressBar progressBar;
    private final myHandler myHandler = new myHandler();
    private UserInfo userInfo;

    private String poster;
    private String desc;
    public final static String immomo = "https://m.immomo.com/";
    public final static String douyin = "https://v.douyin.com/";
    public final static String kuaishou = "https://v.kuaishou.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);
        userInfo = UserInfo.getInstance();
        backtitle = findViewById(R.id.backtitle);
        tvResult = findViewById(R.id.tvResult);
        editText = findViewById(R.id.editText);
        webView = findViewById(R.id.webView);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        progressBar = findViewById(R.id.progressBar);
        tvmax = findViewById(R.id.tvmax);
        backtitle.setconter(getString(R.string.tv_msg246));
        webView.setHtmlCallback(this::parseHtml);
    }

    /**
     * ????????????????????????????????????
     */
    private void parseHtml(String html) {
        //?????????????????????
        final String url = getCompleteUrl(editText.getText().toString());
        boolean contains = url.contains(immomo);
        if (contains) {
            //?????????????????????
            momoparseHtml(html);
        } else {
            //????????????
            douying(html);
        }
    }

    private void douying(String html) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg247));
            return;
        }

        // ????????????video??????
        Elements theVideo = document.getElementsByTag("video");

        Log.d(TAG, "douying: "+theVideo);

        if (theVideo == null) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg248));
            return;
        }
        String videoUrl = theVideo.attr("src");
        if (TextUtils.isEmpty(videoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg254));
            return;
        }

        // ????????????????????????
        videoUrl = videoUrl.replace("playwm", "play");

        // ??????????????????URL
        String finalVideoUrl = getRealUrl(videoUrl);
        if (TextUtils.isEmpty(finalVideoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg249));
            return;
        }

        // ??????????????????????????????
        runOnUiThread(() -> {
            button1.setText(R.string.tv_msg250);
            button1.setEnabled(true);
            tvResult.setText(finalVideoUrl);
            desc = Jsoup.parse(editText.getText().toString()).text();
            copydesc(desc);

        });

    }

    /**
     * ????????????????????????????????????
     */
    private void momoparseHtml(String html) {
        Document document = Jsoup.parse(html);
        if (document == null) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg247));
            return;
        }

        // ????????????video??????
        Elements theVideo = document.getElementsByTag("video");
        if (theVideo == null) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg248));
            return;
        }

        String videoUrl = theVideo.attr("abs:src");
        poster = theVideo.attr("abs:poster");
        desc = document.getElementsByClass("info-con").get(0).getElementsByClass("desc").text();

        if (TextUtils.isEmpty(videoUrl)) {
            runOnUiThread(() -> ToastUtils.showShort(R.string.tv_msg254));
            return;
        }

        // ??????????????????????????????
        runOnUiThread(() -> {
            button1.setText(R.string.tv_msg250);
            button1.setEnabled(true);
            tvResult.setText(videoUrl);

            copydesc(desc);

        });
    }

    /**
     * ?????????????????????
     */
    private String getRealUrl(String urlStr) {
        String realUrl = urlStr;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("user-agent", "Mozilla/5.0.html (iPhone; U; CPU iPhone OS 4_3_3 like Mac " +
                    "OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) " +
                    "Version/5.0.html.2 Mobile/8J2 Safari/6533.18.5 ");
            conn.setInstanceFollowRedirects(false);
            int code = conn.getResponseCode();
            String redirectUrl = "";
            if (302 == code) {
                redirectUrl = conn.getHeaderField("Location");
            }
            if (redirectUrl != null && !redirectUrl.equals("")) {
                realUrl = redirectUrl;
            }
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realUrl;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // ??? Android Q???10?????????????????????????????????????????????????????????????????????
        // https://www.jianshu.com/p/8f2100cd1cc5
        String shareText = getShareText();
        if (!TextUtils.isEmpty(shareText) && (shareText.contains(douyin) || shareText.contains(immomo) || shareText.contains(kuaishou))) {
            editText.setText(shareText);
        }
    }

    /**
     * ?????????????????????
     *
     * @return ???????????????
     */
    public static String getShareText() {
        ClipboardManager manager = (ClipboardManager) DemoApplication.instance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }

    /**
     * ????????????
     */
    public void button1(View view) {
        final String url = getCompleteUrl(editText.getText().toString());
        if (TextUtils.isEmpty(url)) {
            Toashow.show(getString(R.string.tv_msg251));
            return;
        }
        progressBar.setProgress(0);
        button1.setText(R.string.tv_msg252);
        tvmax.setText(null);
        button1.setEnabled(false);

        //????????????????????????
        webView.setTag(url);
        webView.loadUrl(url);
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    public void button2(View view) {
        progressBar.setProgress(0);
        String completeUrl = getCompleteUrl(tvResult.getText().toString().trim());
        if (TextUtils.isEmpty(completeUrl)) {
            Toashow.show("??????????????????");
            return;
        }
        button2.setEnabled(false);

        //????????????
        new Thread() {
            @Override
            public void run() {
                super.run();
                Downloadload(completeUrl);
            }
        }.start();
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    public void button3(View view) {
        String completeUrl = getCompleteUrl(tvResult.getText().toString().trim());
        if (TextUtils.isEmpty(completeUrl)) {
            Toashow.show("??????????????????");
            return;
        }
        statcompleteUrl(completeUrl);
    }

    /**
     * ??????????????????
     *
     * @param completeUrl
     */
    public void statcompleteUrl(String completeUrl) {
        videolist videolist = new videolist();
        videolist.setId("1");
        videolist.setUserid(userInfo.getUserId());
        videolist.setPlayurl(completeUrl);
        videolist.setTencent(0);
        videolist.setType(0);
        videolist.setBigpicurl(TextUtils.isEmpty(poster) ? "" : poster);
        videolist.setTitle(TextUtils.isEmpty(desc) ? "????????????" : desc);
        videolist.setAlias(TextUtils.isEmpty(desc) ? "????????????" : desc);
        videolist.setFnum("520");
        item item = new item();
        item.object = videolist;
        List<item> itemList = new ArrayList<>();
        itemList.add(item);
        PagerPlay.starsetAction(this, itemList, 1, videolist.getType());
    }

    public void staractiviticompleteUrl(String completeUrl) {
        Intent intent = new Intent(this, videoijkplayer0.class);
        intent.putExtra(Constants.PATHVIDEO, completeUrl);
        intent.putExtra(Constants.TITLE, editText.getText().toString());
        intent.putExtra(Constants.PATHIMG, "");
        intent.putExtra(Constants.POSITION, -1);
        startActivity(intent);
    }

    /**
     * ??????????????????
     *
     * @param view
     */
    public void button4(View view) {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.isNetworkAvailable));
            return;
        }
        UserInfo userInfo = UserInfo.getInstance();
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra(ACTIVITY_USERID, userInfo.getUserId());
        intent.putExtra(ACTIVITY_TYPE, ACTIVITY_VIDEO);
        intent.putExtra(ACTIVITY_VIP, userInfo.getVip());
        intent.putExtra(ACTIVITY_STATE, userInfo.getState());
        startActivityForResult(intent, Config.sussess);
    }

    /**
     * ?????????????????????
     *
     * @param text ??????????????????????????????text??????
     */
    public static String getCompleteUrl(String text) {
        Pattern p = Pattern.compile("((http|ftp|https)://)(([a-zA-Z0-9._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9&%_./-~-]*)?", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(text);
        boolean find = matcher.find();
        if (find) {
            return matcher.group();
        } else {
            return "";
        }
    }

    /**
     * ???????????????
     *
     * @param view
     */
    public void onResultClick(View view) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = tvResult.getText().toString();
        ClipData myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toashow.show("?????????????????????");
    }

    /**
     * ??????????????????
     */
    private void Downloadload(String path) {
        // SD??????????????????
        String serva = Environment.getExternalStorageDirectory().getAbsolutePath() + "/paixide";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            serva = CacheUtil.getCacheDirectory(mcontext, null).getAbsolutePath();
        }
        String fileName = String.format("%s.mp4", Config.getRandomFileName());
        File file = new File(serva, fileName);
        try {
            Response response = okhttp.response(path);
            if (response != null) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(file);
                long length = response.body().contentLength();//??????????????????
                progressBar.setMax((int) length);
                progressBar.setProgress(0);
                int Progress = 0;
                byte[] buf = new byte[1024];
                int len = -1;
                while ((len = inputStream.read(buf)) != -1) {
                    //????????????
                    outputStream.write(buf, 0, len);
                    Progress = Progress + len;
                    progressBar.setProgress(Progress);

                    int finalProgress = Progress;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doubrune(finalProgress);
                        }
                    });

                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                response.body().close();
                response.close();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //????????????UI
                        Message message = new Message();
                        message.what = Config.sussess;
                        message.obj = file.getPath();
                        myHandler.handleMessage(message);
                    }
                });

                //????????????
                showvideo(file.getPath());
            } else {
                myHandler.sendEmptyMessage(Config.fail);
            }


        } catch (Exception e) {
            e.printStackTrace();
            myHandler.sendEmptyMessage(Config.fail);
        }


    }

    /**
     * ??????????????????
     *
     * @param path
     */
    private void showvideo(String path) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Video.Media.DATA, path);
        getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

    /**
     * ????????????
     */
    private class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    String path = msg.obj.toString();
                    Toashow.show("???????????? ???????????????" + path);
                    button2.setEnabled(true);
                    break;
                case Config.fail:
                    Toashow.show("???????????? ?????????");
                    button2.setEnabled(true);
                    break;
            }

        }
    }

    /**
     * ????????????????????????
     *
     * @param Progress
     */
    public void doubrune(int Progress) {
        int mb = Progress / 1024 / 1024 % 1024;
        int kb = Progress / 1024 % 1024;
        String conter = mb > 0 ? String.format(Locale.US, "%s.%sMB", mb, kb) : String.format(Locale.US, "%sKB", kb);
        tvmax.setText("??????" + conter);
    }

    private void copydesc(String desc) {
        ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText("text", desc);
        myClipboard.setPrimaryClip(myClip);
        Toashow.show(getString(R.string.tmjson));
    }

}