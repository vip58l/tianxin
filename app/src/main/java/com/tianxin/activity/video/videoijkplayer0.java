/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/22 0022
 */


package com.tianxin.activity.video;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Receiver.MyService;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_delete_video;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_Config;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 用户上传视后单独的视频播放页面
 */
public class videoijkplayer0 extends BasActivity2 implements View.OnTouchListener {
    String TAG = videoijkplayer0.class.getSimpleName();
    @BindView(R.id.plvideoView)
    com.tianxin.activity.video2.widget.player plvideoView;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.refresh_view)
    ImageView refresh_view;
    @BindView(R.id.bgsimg)
    ImageView bgsimg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.daytetime)
    TextView daytetime;

    private int resultposition;
    private boolean getboolean = false;
    private final Handler mHandler = new mhandler();
    public static final int SHOW_PROGRESS = 2;
    private boolean showview = false;

    public static void setAction(Context context, String title, String video, String img) {
        Intent intent = new Intent(context, videoijkplayer0.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.PATHVIDEO, video);
        intent.putExtra(Constants.PATHIMG, img);
        intent.putExtra(Constants.showview, false);
        intent.putExtra(Constants.Edit, false);
        intent.putExtra(Constants.POSITION, 0);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
        return R.layout.video_item_play;
    }

    @Override
    public void iniview() {
        //在Activity中停止Service
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(DemoApplication.instance(), MyService.class));
        }
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent date = getIntent();
        String name = date.getStringExtra(Constants.TITLE);
        String video = date.getStringExtra(Constants.PATHVIDEO);
        String img = date.getStringExtra(Constants.PATHIMG);

        showview = date.getBooleanExtra(Constants.showview, false);
        getboolean = date.getBooleanExtra(Constants.Edit, false);
        resultposition = date.getIntExtra(Constants.POSITION, -1);

        title.setText(TextUtils.isEmpty(name) ? "" : name);
        daytetime.setVisibility(View.GONE);
        Glide.with(activity).load(TextUtils.isEmpty(img) ? video : img).into(bgsimg);
        refresh_view.setVisibility(showview ? View.GONE : View.VISIBLE);

        plvideoView.setPath(video);
        plvideoView.setOnTouchListener(this);
        plvideoView.setListener(new player.VideoPlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.d(TAG, "onCompletion: ");
                plvideoView.start();
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG, "onError: ");
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG, "onInfo: ");
                switch (i) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                        Log.d(TAG, "媒体信息视频渲染开始: ");
                        //Android属性动画(Animator)
                        //translationX和translationY	x轴和y轴的偏移量
                        //rotation、rotationX和rotationY	围绕支点旋转
                        //rotation(360) 围绕支点旋转
                        //scaleX和scaleY	缩放
                        //alpha	透明度
                        //x(5000).y(5000)  移动
                        //xBy(500).xBy(500) 移动
                        //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                        play.animate().alpha(0f).setDuration(500).start();
                        bgsimg.animate().alpha(0f).setDuration(1000).start();
                        return true;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                        Log.d(TAG, "媒体信息视频跟踪滞后: ");
                        return true;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                        Log.d(TAG, " 媒体信息缓冲启动");
                        mHandler.removeMessages(SHOW_PROGRESS);
                        return true;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                        Log.d(TAG, "媒体信息缓冲结束");
                        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
                        return true;
                }
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG, "onPrepared: ");
                long duration = plvideoView.getDuration();
                long pointerIcon = plvideoView.getCurrentPosition();
                seekBar.setMax((int) duration);
                seekBar.setProgress((int) pointerIcon);
                mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
                iMediaPlayer.start();
            }

        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    daytetime.setText(String.format("%s/%s", Config.generateTime(seekBar.getProgress()), Config.generateTime(seekBar.getMax())));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                daytetime.setVisibility(View.VISIBLE);
                mHandler.removeMessages(SHOW_PROGRESS);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                plvideoView.seekTo(progress);
                daytetime.setVisibility(View.GONE);
                mHandler.sendEmptyMessage(SHOW_PROGRESS);
            }

        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    @OnClick({R.id.back, R.id.mhde_img_back, R.id.tv_delete, R.id.img_delete, R.id.refresh_view})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
            case R.id.mhde_img_back:
                finish();
                break;
            case R.id.tv_delete:
            case R.id.img_delete:
            case R.id.refresh_view:
                if (getboolean) {
                    dialog_delete_video.video(context, 0, new Paymnets() {
                        @Override
                        public void onSuccess() {
                            deletevideo();
                        }

                        @Override
                        public void onRefresh() {
                            Toashow.show("普通会员暂不支持编辑视频");
                        }

                        @Override
                        public void onLoadMore() {

                        }

                        @Override
                        public void fall(int code) {
                            Toashow.show("无法置顶");

                        }
                    });
                }
                break;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (plvideoView != null) {
            plvideoView.pause();
            mHandler.removeMessages(SHOW_PROGRESS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        playstart();
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plvideoView != null) {
            plvideoView.stop();
            plvideoView.release();
            plvideoView = null;
        }

        if (mHandler != null) {
            mHandler.removeMessages(SHOW_PROGRESS);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setDownloadParentplay();
                break;
        }
        return true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow: ");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Log.d(TAG, "onWindowFocusChanged: ");

    }

    private void setDownloadParentplay() {
        if (plvideoView.isPlaying()) {
            plvideoView.pause();
            play.animate().alpha(1f).setDuration(300).start();
            mHandler.removeMessages(SHOW_PROGRESS);
        } else {
            playstart();
        }
    }

    private void deletevideo() {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(getString(R.string.tv_a1_a2));
        dialogGame.setKankan(getString(R.string.tv_msg113));
        dialogGame.setTextColor(getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(payMnets);
    }

    private class mhandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS:
                    if (plvideoView == null) {
                        return;
                    }
                    if (!plvideoView.isPlaying()) {
                        return;
                    }
                    setProgress();
                    break;
            }
        }

    }

    private void setProgress() {
        long position = plvideoView.getCurrentPosition();
        long duration = plvideoView.getDuration();
        if (duration > 0) {
            seekBar.setProgress((int) position);
            play.animate().alpha(0f).setDuration(200).start();
        }
        daytetime.setText(String.format("%s/%s", Config.generateTime(position), Config.generateTime(duration)));
        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
    }

    private Paymnets payMnets = new Paymnets() {
        @Override
        public void onClick() {

        }

        @Override
        public void activity() {
            Intent date = new Intent();
            date.putExtra(Constants.PATHVIDEO, resultposition);
            setResult(Config.on, date);
            finish();
        }
    };

    private void playstart() {
        if (plvideoView != null) {
            plvideoView.start();
            play.setVisibility(View.VISIBLE);
            play.animate().alpha(0f).setDuration(300).start();
            bgsimg.animate().alpha(0f).setDuration(1000).start();
            mHandler.sendEmptyMessage(SHOW_PROGRESS);
        }
    }

}


