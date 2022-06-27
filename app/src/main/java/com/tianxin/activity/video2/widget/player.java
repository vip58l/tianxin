package com.tianxin.activity.video2.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.danikula.videocache.HttpProxyCacheServer;
import com.tianxin.app.DemoApplication;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * https://www.imooc.com/article/72800
 * Android 实现视屏播放器、边播边缓存功能（IJKPlayer）
 * 自定义播放器
 */
public class player extends FrameLayout {
    private String TAG = player.class.getSimpleName();
    private Context mContext;                   //上下文
    private IjkMediaPlayer mMediaPlayer;        //视频控制类
    private VideoPlayerListener listener;       //自定义监听器
    private SurfaceView mSurfaceView;           //播放视图
    private String mPath;                       //视频地址

    public player(@NonNull Context context) {
        super(context);
        init();
    }

    public player(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public player(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mContext = getContext();
        setFocusable(true);
    }

    /**
     * 代理视频播放器边播边缓存
     *
     * @param path
     * @return
     */
    public static String mymPath(String path) {
        HttpProxyCacheServer proxy = DemoApplication.getProxy();
        String proxyUrl = proxy.getProxyUrl(path);
        return proxyUrl;
    }

    /**
     * 添加mSurfaceView
     */
    private void initSurfaceView() {
        mSurfaceView = new SurfaceView(mContext);
        mSurfaceView.getHolder().addCallback(new LmnSurfaceCallback());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);
        mSurfaceView.setLayoutParams(layoutParams);
        this.addView(mSurfaceView);
    }

    /**
     * surfaceView的监听器
     */
    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            loadVideo();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    /**
     * 加载视频
     */
    private void loadVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        mMediaPlayer = new IjkMediaPlayer();            //创建播放器
        mMediaPlayer.setScreenOnWhilePlaying(true);     //常亮
        setDataSource(mPath);
        mMediaPlayer.setDisplay(mSurfaceView.getHolder());
        mMediaPlayer.prepareAsync();                   //开始加载

        if (listener != null) {
            mMediaPlayer.setOnPreparedListener(listener);   //加载完成
            mMediaPlayer.setOnErrorListener(listener);      //加载失败
            mMediaPlayer.setOnCompletionListener(listener); //播放完成
            mMediaPlayer.setOnInfoListener(listener);       //开始加载
        }

        //视频大小
        //mMediaPlayer.setOnVideoSizeChangedListener(listener);
        //视频拖动
        //mMediaPlayer.setOnSeekCompleteListener(listener);
        //缓冲
        //mMediaPlayer.setOnBufferingUpdateListener(null);
        //音频类型
        //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mMediaPlayer.setOnVideoSizeChangedListener(listener);
        //mMediaPlayer.setOnVideoSizeChangedListener(listener);
        //mMediaPlayer.setOnVideoSizeChangedListener(listener);
    }

    /**
     * 加入监听事件
     *
     * @param listener
     */
    public void setListener(VideoPlayerListener listener) {
        this.listener = listener;
    }

    /**
     * 状态
     */
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 设置播放地址
     *
     * @param path
     */
    public void setPath(String path) {

        if (!TextUtils.isEmpty(path)) {
            this.mPath = path.toLowerCase().startsWith("http://") || path.toLowerCase().startsWith("https://") ? mymPath(path) : path;
            if (mSurfaceView == null) {
                initSurfaceView();
            }
        }
    }

    /**
     * 开始
     */
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    /**
     * 停止
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public void setOption(int category, String name, long value) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOption(category, name, value);
        }

    }

    /**
     * 重置
     */
    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }

    /**
     * 释放
     */
    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 进度操作
     *
     * @param l
     */
    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }

    /**
     * 循环播放
     *
     * @param b
     */
    public void setLooping(boolean b) {
        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(b);
        }
    }

    /**
     * 设置音量默认0
     */
    public void setVolume(float v1, float v2) {
        mMediaPlayer.setVolume(v1, v2);
    }

    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    /**
     * 返回播放器
     *
     * @return
     */
    public IjkMediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public IMediaPlayer iMediaPlayer() {
        return mMediaPlayer;
    }

    /**
     * 处理DNS缓存的问题
     *
     * @param mPath
     */
    private void setDataSource(String mPath) {
        try {
            mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1);
            mMediaPlayer.setDataSource(mPath);          //数据源
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 自定义监听器
     */
    public abstract static class VideoPlayerListener implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnInfoListener {
    }

}
