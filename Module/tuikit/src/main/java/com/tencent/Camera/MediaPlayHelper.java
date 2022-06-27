package com.tencent.Camera;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class MediaPlayHelper {
    private static final String TAG = "MediaPlayHelper";
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler;
    private int mResId;
    private String mResPath;
    private static int sindex = 10; //音量
    private static MediaPlayHelper mediaPlayHelper;

    public static MediaPlayHelper playHelper(Context context) {
        if (mediaPlayHelper == null) {
            mediaPlayHelper = new MediaPlayHelper(context);
        }
        return mediaPlayHelper;
    }

    public MediaPlayer getmMediaPlayer() {
        return mMediaPlayer;
    }

    public MediaPlayHelper(Context context) {
        mContext = context;
        mMediaPlayer = new MediaPlayer();
        mResId = -1;
        mResPath = "";
        AUDIOSERVICE(context);
    }

    /**
     * Android 各种音量的获取和设置 https://blog.csdn.net/wulong710/article/details/7494192
     *
     * @param context
     */
    public static void AUDIOSERVICE(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sindex, 0);
    }

    /**
     * 系统问题音量大小
     *
     * @param state
     */
    public static void STREAMMUSIC(Context context, boolean state) {
        AudioManager mAudioManager = (AudioManager) context.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, state);
    }

    public void start(String path) {
        start(path, -1, 0);
    }

    public void start(int resId) {
        start(resId, 0);
    }

    public void start(int resId, long duration) {
        start("", resId, duration);
    }

    private void start(String resPath, final int resId, long duration) {
        preHandler();
        if ((-1 != resId && (mResId == resId)) || (!TextUtils.isEmpty(resPath) && TextUtils.equals(mResPath, resPath))) {
            return;
        }
        AssetFileDescriptor afd0 = null;
        if (TextUtils.isEmpty(resPath) || !new File(resPath).exists()) {
            //本地播放音频
            mResId = resId;
            afd0 = mContext.getResources().openRawResourceFd(resId);
            if (afd0 == null) {
                return;
            }
        } else {
            //通过地址播放音频
            mResPath = resPath;
        }
        final AssetFileDescriptor afd = afd0;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.setOnCompletionListener(null);
                mMediaPlayer.reset();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                //重新设置播放音频
                try {
                    if (null != afd) {
                        mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    } else {
                        mMediaPlayer.setDataSource(mResPath);
                    }
                } catch (IOException e) {
                    Log.i(TAG, Log.getStackTraceString(e));
                }

                //监听播放
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        //播放完成大于10秒继续播放
                        if (duration > 10000) {
                            mp.start();
                        } else {
                            stop();
                        }
                    }
                });
                try {
                    mMediaPlayer.prepare();
                } catch (IOException e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                mMediaPlayer.start();
            }
        });

        if (duration > 10000) {
            return;
        }

        //到达N秒后停止播放
        if (duration > 0) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            }, duration);
        }

    }

    public void startplay(String resPath) {
        preHandler();
        //判断是否同一首音乐
        if ((!TextUtils.isEmpty(resPath) && TextUtils.equals(mResPath, resPath))) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.setOnCompletionListener(null);
                mMediaPlayer.reset();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mResPath = resPath;
                    mMediaPlayer.setDataSource(mResPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.start();
                    }
                });
                try {
                    //mediaPlayer.prepare();    //同步的方式装载流媒体文件
                    mMediaPlayer.prepareAsync();//异步的方式装载流媒体文件
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
                mMediaPlayer.start();
            }
        });
    }

    private void preHandler() {
        if (null != mHandler) {
            return;
        }
        HandlerThread thread = new HandlerThread("Handler-MediaPlayer");
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    public int getResId() {
        return mResId;
    }

    public void stop() {
        if (null == mHandler || (-1 == getResId()) && TextUtils.isEmpty(mResPath)) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mResId = -1;
                    mResPath = "";
                    mHandler=null;
                }
            }
        });
    }
}
