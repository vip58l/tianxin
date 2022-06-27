package com.tencent.qcloud.tim.uikit.utilsdialog;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class play {
    private String TAG = play.class.getSimpleName();
    private MediaPlayer mediaPlayer;
    private Context mContext;
    private static play splay;

    public static play cerplay(Context context) {
        if (splay == null) {
            splay = new play(context);
        }
        return splay;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public play(Context context) {
        this.mContext = context;
        mediaPlayer = new MediaPlayer();
    }

    /**
     * 播放器的初始化方法
     */
    public void initMediaPlayer() {

        //File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
        //mediaPlayer.setDataSource(file.getPath());//指定音频文件路径
        mediaPlayer = MediaPlayer.create(TUIKit.getAppContext(), R.raw.call);
        mediaPlayer.setLooping(true);//设置为循环播放
        //mediaPlayer.prepare();     //初始化播放器MediaPlayer
        //mediaPlayer.prepareAsync();  //通过异步的方式装载媒体资源
    }

    /**
     * 播放器的初始化方法
     */
    public void initMediaPlayer(int path) {
        mediaPlayer = MediaPlayer.create(TUIKit.getAppContext(), path);//指定音频文件路径
        mediaPlayer.setLooping(true);//设置为循环播放
        //mediaPlayer.prepare();     //初始化播放器MediaPlayer
        //mediaPlayer.prepareAsync();  //通过异步的方式装载媒体资源


    }

    public void btnPlay() {
        if (mediaPlayer != null) {
            //如果没在播放中，立刻开始播放。
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    public void btnPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    public void btnStop() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                initMediaPlayer();//初始化播放器 MediaPlayer
            }
        }
    }

    public void btnDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
