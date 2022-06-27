package com.tianxin.BasActivity;

import static com.tianxin.Util.Config.getFileName;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.activity.Web.JsInterface;
import com.tianxin.dialog.Dialog_mesges2;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class BasestartActivity {
    private Context context;
    private UserInfo userInfo;
    private JsInterface jsInterface;

    public BasestartActivity(Context content) {
        this.context = content;
        this.userInfo = UserInfo.getInstance();
        this.jsInterface=new JsInterface(context);
    }

    /**
     * 申请红娘
     */
    public void dDialogmesges2(Paymnets paymnets) {
        Dialog_mesges2 dialogMesges2 = dialog_Config.Dialogexit2(context);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            dialogMesges2.setIcon(userInfo.getAvatar());
        } else {
            dialogMesges2.setIcon(userInfo.getSex().equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        }
        dialogMesges2.setTv_name(userInfo.getName());
        dialogMesges2.setPaymnets(paymnets);

    }

    /**
     * 获取视频图片和播放时长
     *
     * @param file
     */
    private static void FileVideo(File file) {
        //视频获取图片
        Bitmap bitmap = getVedioThumbnail(file);
        //返回视频播放总时长
        Long vedioTotalTime = getVedioTotalTime(file);
        String time = Config.generateTime(vedioTotalTime);
    }

    /**
     * 保存图片文件到本地
     */
    public void saveBitmapfile(String path) throws ExecutionException, InterruptedException {
        //通过Glide保存图片
        File file = Glide.with(context).asFile().load(path).submit().get();
        //插入到相册
        displayToGallery(context, file);
    }

    /**
     * 获取视频缩略图 在Android里获取视频的信息主要依靠MediaMetadataRetriever实现
     *
     * @param vedioFile
     * @return
     */
    public static Bitmap getVedioThumbnail(File vedioFile) {
        if (!vedioFile.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(vedioFile.getAbsolutePath());
        Bitmap bitmap = retriever.getFrameAtTime();
        //Bitmap bitmap = retriever.getFrameAtTime(1000);//参数为毫秒,就是返回靠近这个时间的帧图
        return bitmap;
    }

    /**
     * 返回视频播放总时长
     *
     * @param vedioFile
     * @return
     */
    public static Long getVedioTotalTime(File vedioFile) {
        if (!vedioFile.exists()) {
            return null;
        }
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(vedioFile.getAbsolutePath());
        String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        Long time = Long.valueOf(timeString);
        return time;

    }

    /**
     * 将图片文件插入到相册
     *
     * @param context
     * @param file
     */
    public void displayToGallery(Context context, File file) {
        if (!file.exists()) {
            return;
        }
        String photoPath = file.getAbsolutePath();
        String photoName = file.getName();
        try {
            //把文件插入到系统图库
            //ContentResolver contentResolver = context.getContentResolver();
            //MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);

            //通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void startActivity(Class cls, String url) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.VIDEOURL, url);
        context.startActivity(intent);
    }

    public void startActivity(Class cls, String url, String json) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.VIDEOURL, url);
        intent.putExtra(Constants.JSON, json);
        context.startActivity(intent);
    }

    public void startActivity(Class cls, String url, String json, int TYPE) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.VIDEOURL, url);
        intent.putExtra(Constants.JSON, json);
        intent.putExtra(Constants.TYPE, TYPE);
        context.startActivity(intent);
    }

    public void startActivity(Class cls, String url, String json, int TYPE, boolean value) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtra(Constants.VIDEOURL, url);
        intent.putExtra(Constants.JSON, json);
        intent.putExtra(Constants.TYPE, TYPE);
        intent.putExtra(Constants.ICON_URL, value);
        context.startActivity(intent);
    }

    /**
     * 浏览器设置
     * @param webView
     */
    public void WebSettings(WebView webView){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);            //还需要设置一些属性支持js
        settings.setAllowFileAccess(true);              //是否允许访问文件
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(true);                  //支持缩放
        settings.setAppCacheEnabled(true);              //开启H5(APPCache)缓存功能
        settings.setDomStorageEnabled(true);            //开启 DOM storage 功能
        settings.setLoadWithOverviewMode(true);         //调整到适合webview大小
        settings.setUseWideViewPort(true);              //支持可任意比例缩放
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//设置 缓存模式
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //设置脚本是否允许自动打开弹窗
        settings.setDisplayZoomControls(false);         //是否展现在屏幕缩放控件上
        settings.setBuiltInZoomControls(false);         //启用WebView内置缩放功能
        settings.setDatabaseEnabled(true);              // 应用可以有数据库
        settings.setSaveFormData(true);                 // 保存表单数据
        settings.setGeolocationEnabled(true);           // 启用地理定位
        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);// 屏幕自适应网页,如果没有这个，在低分辨率的手机上显示可能会异常
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportMultipleWindows(true);               //app内的webview突然出现点击网页内的url无法跳转的bug true
        settings.setMediaPlaybackRequiresUserGesture(false);    //播放音频，多媒体需要用户手动
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH); //提高网页加载速度，暂时阻塞图片加载，然后网页加载好了，在进行加载图片

        //设置WebView JavaScript接口可以供页面JS调用 JSInvoker.xxx() 在js网页中调用
        webView.addJavascriptInterface(jsInterface, "JSInvoker");
        webView.setHorizontalScrollBarEnabled(false);
        //加快HTML网页加载完成速度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            settings.setLoadsImagesAutomatically(false);
            settings.setBlockNetworkImage(true);
        }
        if (!Config.isNetworkAvailable()) {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);     //不使用网络，只加载缓存
        }
    }


}
