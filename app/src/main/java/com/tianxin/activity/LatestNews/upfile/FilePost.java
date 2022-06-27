package com.tianxin.activity.LatestNews.upfile;

import static com.tianxin.Util.Config.getFileName;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.LatestNews.activity_news;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.costransferpractice.common.FilePathHelper;

import java.io.File;

public class FilePost {
    private Context context;
    private Activity activity;
    private com.tianxin.activity.LatestNews.activity_news activity_news;
    private long filemax = 1024 * 1024 * 50;    //限制上传文件大小
    private int uptime = 60;                        //上传视频不能超过1分钟
    private Bitmap bgbitmap;

    public FilePost(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

    }

    /**
     * 打开视频相册
     */
    public void openfile() {
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("video/*");
        activity.startActivityForResult(intent, Constants.requestCode);

        //Intent 中重要方法：setAction()、setData()、setType()、putExtra()
        //二、隐式Intent它不明确指出我们想要启动哪一个活动，而是指定了一系列更为抽象的action和category等信息来过滤，找到符合条件的Activity。 intent.setAction(Intent.ACTION_DIAL);
        //1.setAction()：表明我们想要启动能够响应设置的这个action的活动，并在清单文件AndroidManifest.xml中设置action属性。如:(打开一个拨号界面)
        //2.setData()：通常是URI格式定义的操作数据。（只要设置setAction ()就要在清单文件AndroidManifest.xml中设置action属性）例如：tel: intent.setAction(Intent.ACTION_DIAL); intent.setData(Uri.parse("tel:10086"));
        //3.setType()：指定数据类型，选出适合的应用来。（只要设置setAction ()就要在清单文件AndroidManifest.xml中设置action属性）例如： intent.setAction(Intent.ACTION_SEND); intent.setType("text/plain");
        //4.putExtra()：把要传递的数据暂存在Intent。

    }

    /**
     * 获取视频缩略图 在Android里获取视频的信息主要依靠MediaMetadataRetriever实现
     *
     * @param vedioFile
     * @return
     */
    public Bitmap getVedioThumbnail(File vedioFile) {
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
    public Long getVedioTotalTime(File vedioFile) {
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
     * 获取视频图片和播放时长
     *
     * @param file
     */
    private void FileVideo(File file) {
        //视频获取图片
        Bitmap bitmap = getVedioThumbnail(file);
        //返回视频播放总时长
        Long vedioTotalTime = getVedioTotalTime(file);
        String time = Config.generateTime(vedioTotalTime);
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

    /**
     * 选中视频返回处理
     *
     * @param data
     * @param news
     */
    public void videoediting(Intent data, activity_news news) {
        this.activity_news = news;
        String path = FilePathHelper.getAbsPathFromUri(DemoApplication.instance(), data.getData());
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);

        //超出上传限制
        if (file.length() > filemax) {
            Toashow.show(String.format(context.getString(R.string.video_max) + "", filemax / 1024 / 1024));
            return;
        }

        //播放时长不能大于60秒
        if (getVedioTotalTime(file) / 1000 > uptime) {
            Toashow.show(String.format(context.getString(R.string.video_time) + "", uptime));
            return;
        }

        Bitmap bitmap = getVedioThumbnail(file);
        activity_news.loationimges.setVisibility(View.GONE);
        activity_news.filevideo.setVisibility(View.VISIBLE);
        activity_news.filevideo.show(activity_news);//预览视频
        activity_news.filevideo.setpalwy(bitmap);
        activity_news.filevideo.setPath(path);
        setBgbitmap(bitmap);
    }

    public Bitmap getBgbitmap() {
        return bgbitmap;
    }

    public void setBgbitmap(Bitmap bgbitmap) {
        this.bgbitmap = bgbitmap;
    }
}
