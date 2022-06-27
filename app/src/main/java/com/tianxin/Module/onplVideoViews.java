/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/30 0030
 */


package com.tianxin.Module;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.Util.log;
import com.pili.pldroid.player.widget.PLVideoView;

/**
 * 播放器监听事件处理
 */
public class onplVideoViews extends onplVideoViewpage {

    private final Context context;
    private final PLVideoView mIjkVideoView;
    private final ImageView getPlaybg;
    private final ImageView Play;
    private int playconunt;

    public onplVideoViews(Context contexts, PLVideoView PlVideoView, ImageView Playbg,ImageView Play) {
        mIjkVideoView = PlVideoView;
        context = contexts;
        getPlaybg = Playbg;
        this.Play=Play;
        mIjkVideoView.setOnPreparedListener(this);
        mIjkVideoView.setOnCompletionListener(this);
        mIjkVideoView.setOnErrorListener(this);
        mIjkVideoView.setOnInfoListener(this);
        mIjkVideoView.setOnVideoSizeChangedListener(this);//该回调用于监听当前播放器已经缓冲的数据量占整个视频时长的百分比，在播放直播流中无效，仅在播放文件和回放时才有效
        mIjkVideoView.setOnSeekCompleteListener(this);//该回调用于监听当前播放的视频流的尺寸信息，在 SDK 解析出视频的尺寸信息后，会触发该回调，开发者可以在该回调中调整 UI 的视图尺寸
    }

    @Override
    public void onCompletion() {
        mIjkVideoView.start();
    }

    @Override
    public void onPrepared(int i) {
        mIjkVideoView.start();
        // alpha    渐变透明度动画效果
        // scale 渐变尺寸伸缩动画效果
        // translate	画面转移位置移动动画效果
        // rotate	画面转移旋转动画效果
        //getPlaybg.animate().alpha(0).setDuration(500).start();
        //getPlaybg.animate().alpha(0).rotation(50).scaleXBy(0f).scaleYBy(0f).setDuration(500).start();
        //getPlaybg.animate().alpha(0).rotation(360).scaleX(0).scaleY(0).setDuration(500).start();

        mIjkVideoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgeshide();
            }
        }, 500);
    }

    @Override
    public void onVideoSizeChanged(int i, int i1) {

    }

    @Override
    public void onSeekComplete() {

    }

    @Override
    public void onImageCaptured(byte[] bytes) {

    }

    private void imgeshide(){
        getPlaybg.animate().alpha(0f).setDuration(200).start();
        Play.animate().alpha(0f).setDuration(200).start();
    }

    @Override
    public void onInfo(int i, int i1, Object o) {
        switch (i1) {
            case MEDIA_INFO_BUFFERING_START:
                log.d("正在缓冲----");
                //mIjkVideoView.pause();
                break;
            case MEDIA_INFO_BUFFERING_END:
            case MEDIA_INFO_VIDEO_RENDERING_START:
                log.d("缓冲完成----");
                mIjkVideoView.start();
                imgeshide();
                break;

        }

    }

    @Override
    public boolean onError(int i, Object o) {
        switch (i) {
            case MEDIA_ERROR_UNKNOWN:
                Toashow.showShort(context.getString(R.string.tv_msg259));
                ((Activity) context).finish();
                break;
            case ERROR_CODE_OPEN_FAILED:
                ToastUtils.showShort(R.string.tv_msg258);
                playconunt++;
                if (playconunt < 5) {
                    mIjkVideoView.start();
                } else {
                    ((Activity) context).finish();
                }
                break;
            case ERROR_CODE_IO_ERROR://-3	网络异常
                if (!Config.isNetworkAvailable()) {
                    mIjkVideoView.pause();
                    Toashow.showShort(context.getString(R.string.tv_msg260));
                }
                break;
            case ERROR_CODE_SEEK_FAILED://	-4	拖动失败
                break;
            case ERROR_CODE_CACHE_FAILED: //-5	预加载失败
                break;
            case ERROR_CODE_HW_DECODE_FAILURE://	-2003	硬解失败
                break;
            case ERROR_CODE_PLAYER_DESTROYED://-2008	播放器已被销毁，需要再次 setVideoURL 或 prepareAsync
                break;
            case ERROR_CODE_PLAYER_VERSION_NOT_MATCH://-9527	so 库版本不匹配，需要升级
                break;
            case ERROR_CODE_PLAYER_CREATE_AUDIO_FAILED://-4410	AudioTrack 初始化失败，可能无法播放音频
                break;

        }
        return false;
    }
}
