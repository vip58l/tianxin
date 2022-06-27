/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/22 0022
 */


package com.tianxin.Module;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.pili.pldroid.player.widget.PLVideoView;

import static com.tianxin.activity.video.videoijkplayer0.SHOW_PROGRESS;

/**
 * 自定义播放器监听事件处理方便统一管理
 */
public class myonplVideoViewpage extends onplVideoViewpage {
    private final String TAG = "myonplVideoViewpage";
    private final PLVideoView splvideoView;
    private final ImageView pathimg;
    private int playconunt;
    private final SeekBar seekBar;
    private final Handler mHandler;


    public static void play(String path, PLVideoView myplVideoView, ImageView pathimg, SeekBar seekBar, Handler mHandler) {
        new myonplVideoViewpage(path, myplVideoView, pathimg, seekBar, mHandler);
    }

    public myonplVideoViewpage(String path, PLVideoView myplVideoView, ImageView img, SeekBar myseekBar, Handler mymHandler) {
        pathimg = img;
        seekBar = myseekBar;
        mHandler = mymHandler;
        splvideoView = myplVideoView;
        splvideoView.setVideoPath(path);
        splvideoView.setOnCompletionListener(this);
        splvideoView.setOnPreparedListener(this);
        splvideoView.setOnErrorListener(this);
        splvideoView.setOnInfoListener(this);
        splvideoView.setOnSeekCompleteListener(this);
        splvideoView.setOnInfoListener(this);
        splvideoView.setOnVideoSizeChangedListener(this);
        splvideoView.setLooping(true); //重复播放 则播放结束后会自动重新开始，不会回调本接口 setOnCompletionListener


        //视频截图贞
        //截取调用此方法后相应毫秒后的视频画面，仅对点播流生效
        splvideoView.captureImage(5000);
    }

    @Override
    public void onCompletion() {
        splvideoView.start();
    }

    @Override
    public boolean onError(int i, Object o) {
        switch (i) {
            case MEDIA_ERROR_UNKNOWN:
                Toashow.showShort("未知错误");
                break;
            case ERROR_CODE_OPEN_FAILED:
                Toashow.showShort("视频无法播放");
                break;
            case ERROR_CODE_IO_ERROR://-3	网络异常
                if (!Config.isNetworkAvailable()) {
                    splvideoView.pause();
                    Toashow.showShort("网络异常");
                }
                break;

        }

        return false;

//        MEDIA_ERROR_UNKNOWN	-1	未知错误
//        ERROR_CODE_OPEN_FAILED	-2	播放器打开失败
//        ERROR_CODE_IO_ERROR	-3	网络异常
//        ERROR_CODE_SEEK_FAILED	-4	拖动失败
//        ERROR_CODE_CACHE_FAILED	-5	预加载失败
//        ERROR_CODE_HW_DECODE_FAILURE	-2003	硬解失败
//        ERROR_CODE_PLAYER_DESTROYED	-2008	播放器已被销毁，需要再次 setVideoURL 或 prepareAsync
//        ERROR_CODE_PLAYER_VERSION_NOT_MATCH	-9527	so 库版本不匹配，需要升级
//        ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED	-4410	AudioTrack 初始化失败，可能无法播放音频
    }

    @Override
    public void onInfo(int i, int i1, Object o) {
        switch (i) {
            case MEDIA_INFO_BUFFERING_START:
                //开始缓冲
                if (splvideoView.isPlaying()) {
                    //splvideoView.pause();
                }

                break;
            case MEDIA_INFO_BUFFERING_END:
            case MEDIA_INFO_VIDEO_RENDERING_START:
                //停止缓冲
                splvideoView.start();
                break;

        }

//        what	value	描述
//        MEDIA_INFO_UNKNOWN	1	未知消息
//        MEDIA_INFO_VIDEO_RENDERING_START	3	第一帧视频已成功渲染
//        MEDIA_INFO_CONNECTED	200	连接成功
//        MEDIA_INFO_METADATA	340	读取到 metadata 信息
//        MEDIA_INFO_BUFFERING_START	701	开始缓冲
//        MEDIA_INFO_BUFFERING_END	702	停止缓冲
//        MEDIA_INFO_SWITCHING_SW_DECODE	802	硬解失败，自动切换软解
//        MEDIA_INFO_CACHE_DOWN	901	预加载完成
//        MEDIA_INFO_LOOP_DONE	8088	loop 中的一次播放完成
//        MEDIA_INFO_VIDEO_ROTATION_CHANGED	10001	获取到视频的播放角度
//        MEDIA_INFO_AUDIO_RENDERING_START	10002	第一帧音频已成功播放
//        MEDIA_INFO_VIDEO_GOP_TIME	10003	获取视频的I帧间隔
//        MEDIA_INFO_VIDEO_BITRATE	20001	视频的码率统计结果
//        MEDIA_INFO_VIDEO_FPS	20002	视频的帧率统计结果
//        MEDIA_INFO_AUDIO_BITRATE	20003	音频的帧率统计结果
//        MEDIA_INFO_AUDIO_FPS	20004	音频的帧率统计结果
//        MEDIA_INFO_VIDEO_FRAME_RENDERING	10004	视频帧的时间戳
//        MEDIA_INFO_AUDIO_FRAME_RENDERING	10005	音频帧的时间戳
//        MEDIA_INFO_CACHED_COMPLETE	1345	离线缓存的部分播放完成
//        MEDIA_INFO_IS_SEEKING	565	上一次 seekTo 操作尚未完成
    }

    @Override
    public void onPrepared(int i) {
        splvideoView.start();
        long position = splvideoView.getCurrentPosition();
        long duration = splvideoView.getDuration();
        seekBar.setMax((int) duration);
        seekBar.setThumbOffset(1);
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        splvideoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pathimg.setVisibility(View.GONE);
            }
        }, 500);
    }

    @Override
    public void onSeekComplete() {
    }

    @Override
    public void onVideoSizeChanged(int i, int i1) {
        //该回调用于监听当前播放器已经缓冲的数据量占整个视频时长的百分比，在播放直播流中无效，仅在播放文件和回放时才有效
    }

    @Override
    public void onImageCaptured(byte[] bytes) {
        //4.3.13 视频截图编码后的 jpeg 截图数据


    }
}

