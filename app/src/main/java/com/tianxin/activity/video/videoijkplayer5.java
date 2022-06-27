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
import android.os.Environment;
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
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_delete_video;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_Config;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.widget.PLVideoView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 用户上传视后单独的视频播放页面
 */
public class videoijkplayer5 extends BasActivity2 {
    String TAG = videoijkplayer5.class.getSimpleName();
    @BindView(R.id.plvideoView)
    PLVideoView plvideoView;
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
        Intent intent = new Intent(context, videoijkplayer5.class);
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
        return R.layout.video_mp_play;
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

        PlayListener playListener = new PlayListener();
        plvideoView.setVideoPath(video);
        plvideoView.setOnPreparedListener(playListener);
        plvideoView.setOnInfoListener(playListener);
        plvideoView.setOnCompletionListener(playListener);
        plvideoView.setOnErrorListener(playListener);
        plvideoView.setOnTouchListener(playListener);

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
        if (plvideoView != null) {
            playstart();
        }

        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plvideoView != null) {
            plvideoView.stop();
            plvideoView.stopPlayback();
            plvideoView = null;
        }

        if (mHandler != null) {
            mHandler.removeMessages(SHOW_PROGRESS);
        }
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

    /**
     *
     */
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
                    long pos = setProgress();
                    if (pos == -1) {
                        return;
                    }
                    sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
                    break;
            }
        }

    }

    private long setProgress() {
        long position = plvideoView.getCurrentPosition();
        long duration = plvideoView.getDuration();
        if (duration > 0) {
            seekBar.setProgress((int) position);
            play.animate().alpha(0f).setDuration(300).start();
        }
        daytetime.setText(String.format("%s/%s", Config.generateTime(position), Config.generateTime(duration)));
        return position;
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

    public static AVOptions getoptions(Context context) {
        AVOptions options = new AVOptions();

        // 默认的缓存大小，单位是 ms
        // 默认值是：500
        options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 15000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：2000，若设置值小于 KEY_CACHE_BUFFER_DURATION 则不会生效
        options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 30000);

        // 打开重试次数，设置后若打开流地址失败，则会进行重试
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);


        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "paixide";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            dirPath = context.getExternalFilesDir("").getAbsolutePath();
        }

        // 设置本地缓存目录
        // 目前只支持 mp4 点播
        // 默认值是：无
        options.setString(AVOptions.KEY_CACHE_DIR, dirPath);

        // 设置本地缓存文件的后缀名
        // 只有在设置了缓存目录后才会生效
        // 一个流地址在设置了自定义后缀名后，再次播放前必须设置相同的后缀名，否则无法打开
        // 默认值是 mp4
        options.setString(AVOptions.KEY_CACHE_EXT, "mp4");

        return options;
    }

    private class PlayListener implements PLOnCompletionListener, PLOnPreparedListener, PLOnInfoListener, PLOnErrorListener, View.OnTouchListener {

        @Override
        public void onCompletion() {
            plvideoView.start();

        }

        @Override
        public boolean onError(int i, Object o) {
            Log.d(TAG, "onError: ");
            return false;
        }

        @Override
        public void onInfo(int i, int i1, Object o) {
            switch (i1) {
                case MEDIA_INFO_BUFFERING_START:
                    mHandler.removeMessages(SHOW_PROGRESS);
                    break;
                case MEDIA_INFO_BUFFERING_END:
                case MEDIA_INFO_VIDEO_RENDERING_START:
                    playstart();
                    break;

            }
        }

        @Override
        public void onPrepared(int i) {
            long duration = plvideoView.getDuration();
            long pointerIcon = plvideoView.getCurrentPosition();
            seekBar.setMax((int) duration);
            seekBar.setProgress((int) pointerIcon);
            playstart();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    System.out.println("DOWN");
                    setDownloadParentplay();
                    break;
                case MotionEvent.ACTION_UP:
                    System.out.println("UP");
                    break;
                case MotionEvent.ACTION_MOVE:
                    System.out.println("MOVE");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    System.out.println("CANCEL");
                    break;
                case MotionEvent.ACTION_HOVER_ENTER:
                    //鼠标在view上
                    System.out.println("HOVER_ENTER");
                    break;
                case MotionEvent.ACTION_HOVER_MOVE:
                    //鼠标在view上移动
                    System.out.println("HOVER_MOVE");
                    break;
                case MotionEvent.ACTION_HOVER_EXIT:
                    //鼠标离开view
                    System.out.println("HOVER_EXIT");
                    break;
            }
            return true;
        }

    }

    /**
     *
     */
    private void playstart() {
        plvideoView.start();
        play.setVisibility(View.VISIBLE);
        play.animate().alpha(0f).setDuration(300).start();
        bgsimg.animate().alpha(0f).setDuration(1000).start();
        mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
    }


}


