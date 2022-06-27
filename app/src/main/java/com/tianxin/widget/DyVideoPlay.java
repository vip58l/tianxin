/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/23 0023
 */
package com.tianxin.widget;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseFrameLayout;
import com.tianxin.adapter.Radapter;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.Util.log;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.curriculum;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 课程播放器
 */
public class DyVideoPlay extends BaseFrameLayout implements PLOnCompletionListener, PLOnPreparedListener, PLOnInfoListener, PLOnErrorListener {
    private static final String TAG = DyVideoPlay.class.getSimpleName();
    @BindView(R.id.plvideoView)
    PLVideoView plvideoView;
    @BindView(R.id.play_mp)
    ImageView play_mp;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.restart1)
    ImageView restart1;
    @BindView(R.id.download)
    ImageView download;
    @BindView(R.id.vde_img_ps_mpc)
    ImageView vde_img_ps_mpc;
    @BindView(R.id.videobg)
    ImageView videobg;
    @BindView(R.id.onaudio)
    ImageView onaudio;
    @BindView(R.id.fandal)
    ImageView fandal;
    @BindView(R.id.loadingview)
    ProgressBar loadingview;
    @BindView(R.id.tv_startime)
    TextView tv_startime;
    @BindView(R.id.tv_endtime)
    TextView tv_endtime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.how)
    TextView how;
    @BindView(R.id.vde_lin_mp)
    LinearLayout vde_lin_mp;
    @BindView(R.id.video_layout)
    RelativeLayout video_layout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private Handlerplay handlerplay = new Handlerplay();
    private boolean isboolean = false;
    private boolean risboolean = false;
    private boolean audion = false;
    private boolean videoplay = false;
    private int playdate = 5;
    private int totalPage;
    private String videoPath;
    private LinearLayoutManager manager1, manager2;

    public DyVideoPlay(@NonNull Context context) {
        super(context);
        init();
    }

    public DyVideoPlay(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DyVideoPlay(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.activity_dyvideoplay, this);
        activity = (Activity) getContext();
        context = getContext();
        ButterKnife.bind(this);

        radapter = new Radapter(context, mlist, Radapter.activity_list_column);
        radapter.setPaymnets(paymnets);
        manager1 = new LinearLayoutManager(context);
        manager2 = new GridLayoutManager(context, 2);
        recyclerview.setLayoutManager(manager1);
        recyclerview.setAdapter(radapter);
        recyclerview.addOnScrollListener(listener);
        InitDate();
    }

    /**
     * 监听事件
     */
    public void Listener() {
        plvideoView.setOnCompletionListener(this::onCompletion);
        plvideoView.setOnPreparedListener(this::onPrepared);
        plvideoView.setOnInfoListener(this::onInfo);
        plvideoView.setOnErrorListener(this::onError);
    }

    /**
     * 设置播放地址
     *
     * @param curriculum
     */
    public void setVideoPath(curriculum curriculum) {
        this.videoPath = curriculum.getVideo();
        if (!videoPath.toLowerCase().endsWith(".mp4") && !videoPath.toLowerCase().endsWith(".mp3") && !videoPath.toLowerCase().endsWith(".m4a")) {
            Toashow.show("文件不支持播放");
            loadingview.setVisibility(GONE);
            vde_img_ps_mpc.setImageResource(R.mipmap.icon_play_mp);
            return;
        }
        plvideoView.setVideoPath(curriculum.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(videoPath) : videoPath);
        loadingview.setVisibility(VISIBLE);
        seekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);
        tags.setText(curriculum.getTag());
        title.setText(curriculum.getTitle());
        views.setText(curriculum.getMsg());
        Listener();//播放器监听事件
    }

    /**
     * 开始播放
     */
    public void onStart() {
        playdate = 5;
        isboolean = true;
        setVISIBLE();
        plvideoView.start();
        play_mp.setVisibility(GONE);
        loadingview.setVisibility(GONE);
        Glide.with(context).load(R.mipmap.icon_stop_mp).into(vde_img_ps_mpc);
        handlerplay.sendEmptyMessageDelayed(Config.sussess, Constants.REQUEST_CODE);

    }

    private void onPause() {
        plvideoView.pause();
        handlerplay.removeMessages(Config.sussess);
    }

    /**
     * 停止播放
     */
    public void stopPlayback() {
        plvideoView.stopPlayback();
        handlerplay.removeMessages(Config.sussess);
    }

    /**
     * 暂停播放
     */
    public void pause() {
        isboolean = false;
        plvideoView.pause();
        play_mp.setVisibility(VISIBLE);
        Glide.with(getContext()).load(R.mipmap.icon_play_mp).into(vde_img_ps_mpc);
        handlerplay.removeMessages(Config.sussess);
    }

    @OnClick({R.id.back, R.id.play_mp, R.id.full, R.id.vde_img_ps_mpc, R.id.videobg, R.id.how2, R.id.restart1, R.id.download, R.id.onaudio, R.id.fandal})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onKeyDown_back();
                break;
            case R.id.play_mp:
            case R.id.vde_img_ps_mpc:
                if (isboolean) {
                    pause();
                } else {
                    onStart();
                }
                break;
            case R.id.videobg:
                playdate = 5;
                setVISIBLE();
                break;
            case R.id.full:
                //模屏
                videoframelayout();
                break;
            case R.id.how2:
                risboolean = !risboolean;
                radapter.notifyData(!risboolean ? Radapter.activity_list_column1 : Radapter.activity_list_column2);
                recyclerview.setLayoutManager(!risboolean ? manager1 : manager2);
                recyclerview.setAdapter(radapter);
                break;
            case R.id.restart1:
                Dviewrestart();
                break;
            case R.id.download:
                Toashow.show(getContext().getString(R.string.Toast_msg16));
                break;
            case R.id.onaudio:
                if (!audion) {
                    audion = true;
                    plvideoView.setVolume(0f, 0f);
                    Glideload.loadImage(onaudio, R.mipmap.ms_sp_boy_sound_close);
                    ToastUtil.toastShortMessage(getContext().getString(R.string.Toat_msg18));
                } else {
                    audion = false;
                    plvideoView.setVolume(1f, 80f);
                    Glideload.loadImage(onaudio, R.mipmap.ms_sp_boy_sound_open);
                    ToastUtil.toastShortMessage(getContext().getString(R.string.Toast_msg19));
                }
                break;
            case R.id.fandal:
                slayoutParams();
                break;

        }
    }

    /**
     * UI更新
     */
    private void VSeekBar() {
        long currentPosition = plvideoView.getCurrentPosition();
        long duration = plvideoView.getDuration();
        tv_startime.setText(Config.generateTime(currentPosition));
        tv_endtime.setText(Config.generateTime(duration));
        seekbar.setProgress((int) currentPosition);
        seekbar.setMax((int) duration);
    }

    private void VSeekBar(SeekBar seekBar, int progress) {
        tv_startime.setText(Config.generateTime(progress));
        tv_endtime.setText(Config.generateTime(seekBar.getMax()));
    }

    public void InitDate() {
        totalPage++;
        datamodule.curriculum(totalPage, paymnets);
    }

    @Override
    public void onCompletion() {
        seekbar.setProgress(0);
        isboolean = false;
        setVISIBLE();
        Glide.with(getContext()).load(R.mipmap.icon_play_mp).into(vde_img_ps_mpc);
        handlerplay.removeMessages(Config.sussess);
    }

    @Override
    public boolean onError(int i, Object o) {
        switch (i) {
            case MEDIA_ERROR_UNKNOWN:
                Toashow.showShort("未知错误");
                ((Activity) getContext()).finish();
                break;
            case ERROR_CODE_OPEN_FAILED:
                ToastUtils.showShort("无法播放");
                break;
            case ERROR_CODE_IO_ERROR://-3	网络异常
                if (!Config.isNetworkAvailable()) {
                    pause();
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

    @Override
    public void onInfo(int i, int i1, Object o) {
        switch (i) {
            case MEDIA_INFO_BUFFERING_START:
                log.d("正在缓冲----");
                loadingview.setVisibility(VISIBLE);
                plvideoView.pause();
                break;
            case MEDIA_INFO_BUFFERING_END:
            case MEDIA_INFO_VIDEO_RENDERING_START:
                log.d("缓冲完成----");
                plvideoView.start();
                loadingview.setVisibility(GONE);
                break;

        }
    }

    @Override
    public void onPrepared(int i) {
        VSeekBar();
        onStart();
    }

    private class Handlerplay extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    if (plvideoView.isPlaying()) {
                        if (playdate > 0) {
                            playdate--;
                            if (playdate <= 0) {
                                setGONE();
                            }
                        }
                        VSeekBar();
                        handlerplay.sendEmptyMessageDelayed(Config.sussess, 1000);
                    }
                    break;
                case Config.fail:
                    break;

            }
        }
    }

    /**
     * 处理屏幕宽高转换处理
     */
    private void videoframelayout() {
        int portrait = getResources().getConfiguration().orientation;
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        if (portrait == ActivityInfo.SCREEN_ORIENTATION_USER) {
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);   //坚屏
        }
        if (portrait == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);   //横屏
        }

    }

    private void onKeyDown_back() {
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
            videoframelayout();
        } else {
            stopPlayback();
            activity.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //隐藏虚拟按键，并且全屏恢复默认
            View decorView = ((Activity) getContext()).getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.GONE);
            //设置播放器控件高度
            ViewGroup.LayoutParams layoutParams = video_layout.getLayoutParams();
            layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.30);
            video_layout.setLayoutParams(layoutParams);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //设置播放器控件全屏显示现在是横屏
            ViewGroup.LayoutParams layoutParams = video_layout.getLayoutParams();
            layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getContext().getResources().getDisplayMetrics().heightPixels;
            video_layout.setLayoutParams(layoutParams);
            hideBottomUIMenu();
            //设置全底部导航黑色
            Config.AsetctivityBLACK(activity);
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = ((Activity) getContext()).getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = ((Activity) getContext()).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 设置音量
     */
    private void setMUSIC() {
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        setAttributes(0);
    }

    /**
     * 亮度
     *
     * @param detlaY
     */
    private void setAttributes(float detlaY) {
        //获取亮度的值
        WindowManager.LayoutParams attributes = ((Activity) getContext()).getWindow().getAttributes();
        float screenBrightness = attributes.screenBrightness;
        //设置亮度的设节
        attributes.screenBrightness = 1f;
        ((Activity) getContext()).getWindow().setAttributes(attributes);
    }


    /**
     * 设置音量解析
     */
    private void setMUSICTEST() {

        //AudioManager am = (AudioManager) getSystemService( Context.AUDIO_SERVICE );
        //am.adjustStreamVolume( AudioManager.STREAM_RING, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI );

        /*解释一下三个参数
        第一个streamType是需要调整音量的类型,这里设的是媒体音量,可以是:
        字段	说明
        STREAM_ALARM	警报
        STREAM_MUSIC	音乐回放即媒体音量
        STREAM_NOTIFICATION	窗口顶部状态栏Notification,
        STREAM_RING	铃声
        STREAM_SYSTEM	系统
        STREAM_VOICE_CALL	通话

        第二个direction,是调整的方向,增加或减少,可以是:

        字段	说明
        ADJUST_LOWER	降低音量
        ADJUST_RAISE	升高音量
        ADJUST_SAME	保持不变,这个主要用于向用户展示当前的音量量时播


        第三个flags是一些附加参数,只介绍两个常用的

        字段	说明
        FLAG_PLAY_SOUND	调整音量时播放声音
        FLAG_SHOW_UI	调整时显示音量条,就是按音量键出现的那个*/
    }

    /**
     * 获取视频页贞图片
     */
    public static Bitmap getNetVideoBitmap(String path) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path, new HashMap());
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    /**
     * 坚屏视频
     */
    private void slayoutParams() {
        ViewGroup.LayoutParams layoutParams = video_layout.getLayoutParams();
        layoutParams.width = getContext().getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getContext().getResources().getDisplayMetrics().heightPixels;
        if (videoplay) {
            videoplay = false;
            layoutParams.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.30);
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
            Glideload.loadImage(fandal, R.mipmap.hv_player_scale_btn);
        } else {
            videoplay = true;
            Glideload.loadImage(fandal, R.mipmap.top_call_retract_icon);
        }
        video_layout.setLayoutParams(layoutParams);
        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);

    }

    /**
     * 处理视频比例情况
     */
    private void Dviewrestart() {
//        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
//        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
//        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
//        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
//        plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);
        int displayAspectRatio = plvideoView.getDisplayAspectRatio();
        if (PLVideoView.ASPECT_RATIO_ORIGIN == displayAspectRatio) {
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        }
        if (PLVideoView.ASPECT_RATIO_FIT_PARENT == displayAspectRatio) {
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        }
        if (PLVideoView.ASPECT_RATIO_PAVED_PARENT == displayAspectRatio) {
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_16_9);
        }
        if (PLVideoView.ASPECT_RATIO_16_9 == displayAspectRatio) {
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_4_3);
        }
        if (PLVideoView.ASPECT_RATIO_4_3 == displayAspectRatio) {
            plvideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
        }


    }

    private void setVISIBLE() {
        play_mp.setVisibility(plvideoView.isPlaying() ? GONE : VISIBLE);
        vde_lin_mp.setVisibility(VISIBLE);
        back.setVisibility(VISIBLE);
        restart1.setVisibility(VISIBLE);
        download.setVisibility(VISIBLE);
        fandal.setVisibility(VISIBLE);
    }

    private void setGONE() {
        vde_lin_mp.setVisibility(GONE);
        play_mp.setVisibility(GONE);
        download.setVisibility(GONE);
        back.setVisibility(GONE);
        restart1.setVisibility(GONE);
        fandal.setVisibility(GONE);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            List<curriculum> curriculumList = (List<curriculum>) object;
            mlist.addAll(curriculumList);
            radapter.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(View view, int position) {
            curriculum curriculum = (curriculum) mlist.get(position);
            if (curriculum.getVideo().equals(videoPath)) {
                return;
            }
            plvideoView.stopPlayback();
            setVideoPath(curriculum);//重新设置播放器
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }
    };

    private EndLessOnScrollListener listener = new EndLessOnScrollListener(manager1) {
        @Override
        public void onLoadMore(int currentPage) {
            InitDate();
        }
    };

    private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                VSeekBar(seekBar, progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handlerplay.removeMessages(Config.sussess);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            plvideoView.seekTo(seekBar.getProgress());
            handlerplay.sendEmptyMessageDelayed(Config.sussess, Constants.REQUEST_CODE);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onPause();
        Log.d(TAG, "onDetachedFromWindow:从窗口分离");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onStart();
        Log.d(TAG, "onAttachedToWindow:附着到窗口上");

    }

}
