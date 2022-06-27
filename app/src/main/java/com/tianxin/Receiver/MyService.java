/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.Receiver;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tianxin.Module.api.misc;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Player;

/**
 * 音乐服务
 */
public class MyService extends Service {
    private final String TAG = "MyService";
    private String pathlog;
    private MediaPlayer mediaPlayer;
    private WifiManager.WifiLock wifiLock;
    private misc misc;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定前台服务
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(DemoApplication.instance(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) DemoApplication.instance().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand连接到服务");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        // 关闭wifi锁
        if (wifiLock.isHeld()) {
            wifiLock.release();
            wifiLock = null;
        }
        Log.d(TAG, "停止服务 onDestroy");
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "重新绑定服务onRebind");
        super.onRebind(intent);
    }

    /**
     * 建立与前台绑定活动页
     */
    public class MyBinder extends Binder implements Player {

        @Override
        public void setPath(String path) {
            try {
                //播放本地歌曲 首先在main文件夹下面建立assets文件夹
                //app/src/main/assets
                //AssetFileDescriptor fileDescriptor = getResources().getAssets().openFd("hw.mp3");
                //播放地址1
                //activity_player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                //播放地址2
                //activity_player.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getStartOffset());
                //activity_player.prepare();
                //activity_player.start();


                //判断播放同一首歌曲
                if (mediaPlayer.isPlaying() && path.equals(pathlog)) {
                    return;
                }

            /*    //判断播放同一首歌曲
                if (mediaPlayer.isPlaying() && TextUtils.equals(pathlog, path)) {
                    return;
                }

                //判断播放同一首歌曲
                if (mediaPlayer.isPlaying() && path.equalsIgnoreCase(pathlog)) {
                    return;
                }*/


                mediaPlayer.reset(); //复位
                pathlog = path; //记录当前播放的地址
                mediaPlayer.setDataSource(path);
                //mediaPlayer.prepare();//同步的方式装载流媒体文件
                mediaPlayer.prepareAsync();//异步的方式装载流媒体文件

                //mediaPlayer.release();//回收流媒体资源
                //mediaPlayer. setAudioStreamType(1);//：设置播放流媒体类型
                //mediaPlayer.setWakeMode(DemoApplication.instance(), PowerManager.PARTIAL_WAKE_LOCK)//：设置CPU唤醒的状态
                //mediaPlayer.setNextMediaPlayer(MediaPlayer next);//设置当前流媒体播放完毕，下一个播放的MediaPlayer
                // 启用wifi锁
                wifiLock.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void seekTo(int position) {
            mediaPlayer.seekTo(position);
        }

        @Override
        public void start() {
            mediaPlayer.start();
        }

        @Override
        public void pause() {
            mediaPlayer.pause();
        }

        @Override
        public void stop() {

            mediaPlayer.stop();
        }

        @Override
        public void release() {
            mediaPlayer.release();

        }

        @Override
        public void reset() {
            mediaPlayer.reset();
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        @Override
        public void setmisc(misc smisc) {
            misc = smisc;

        }

        @Override
        public misc getmisc() {
            return misc;
        }

        @Override
        public void setLooping(boolean b) {
            mediaPlayer.setLooping(b);
        }

        @Override
        public int getcurrentposition() {
            return mediaPlayer.getCurrentPosition();
            //当前位置 获取当前的播放进度
        }

        @Override
        public int getmyDuration() {
            return mediaPlayer.getDuration();
            //持续时间获取播放时长
        }

    }

}
