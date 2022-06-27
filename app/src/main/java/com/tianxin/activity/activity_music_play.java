package com.tianxin.activity;

import androidx.annotation.NonNull;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.misc;
import com.tianxin.R;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_servicemusic;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.widget.myFrameLayout;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 音乐试听
 */
public class activity_music_play extends BasActivity2 {
    String TAG = activity_music_play.class.getSimpleName();
    itembackTopbr itemback;
    ImageView img_bg, previous, caplay, next, oftime, myselect;
    myFrameLayout myframelayout;
    TextView nametitle, title, tag, duration;
    SeekBar seekBar;
    MyService.MyBinder mediaPlayer = null;
    MyServiceConnection myServiceConnection = new MyServiceConnection();
    Handler handler = new Handlers();
    misc activitymisc;

    public static void starsetAction(Context context) {
        Intent intent = new Intent(context, activity_music_play.class);
        context.startActivity(intent);
    }


    @Override
    protected int getview() {
        return R.layout.activity_misec;
    }

    @Override
    public void iniview() {
        itemback = findViewById(R.id.itemback);
        itemback.settitle(getString(R.string.tm66));
        itemback.setHaidtopBackgroundColor(getResources().getColor(R.color.full_transparent));
        itemback.contertext.setTextColor(getResources().getColor(R.color.white));
        itemback.tvback.setTextColor(getResources().getColor(R.color.white));
        itemback.setIv_back_img(R.mipmap.authsdk_return_bg);

        img_bg = findViewById(R.id.img_bg);
        myframelayout = findViewById(R.id.myframelayout);
        nametitle = findViewById(R.id.nametitle);
        title = findViewById(R.id.title);
        tag = findViewById(R.id.tag);
        duration = findViewById(R.id.duration);
        seekBar = findViewById(R.id.SeekBar);
        previous = findViewById(R.id.previous);
        caplay = findViewById(R.id.caplay);
        next = findViewById(R.id.next);
        oftime = findViewById(R.id.oftime);
        myselect = findViewById(R.id.myselect);

        previous.setOnClickListener(this::OnClick);
        caplay.setOnClickListener(this::OnClick);
        next.setOnClickListener(this::OnClick);
        oftime.setOnClickListener(this::OnClick);
        myselect.setOnClickListener(this::OnClick);
        myframelayout.circleimage.setOnClickListener(this::OnClick);
        nametitle.setSelected(true);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    duration.setText(Config.generateTime(progress));
                    duration.setTextSize(40);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                handler.removeMessages(Config.sussess);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                duration.setTextSize(16);
                handler.sendEmptyMessage(Config.sussess);
            }
        });
        activitymisc = (misc) getIntent().getSerializableExtra(Constants.PATHVIDEO);

    }

    @Override
    public void initData() {
        starmiscpalay();    //启动服务
        hideBottomUIMenu(); //隐藏虚拟按键，并且全屏
    }

    @Override
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.circleimageview:
            case R.id.next:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    myframelayout.pause();
                    handler.removeMessages(Config.sussess);
                    next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_play);
                } else {
                    mediaPlayer.start();
                    myframelayout.start();
                    next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_pause);
                    handler.sendEmptyMessage(Config.sussess);
                }
                break;
            case R.id.previous:
                previous.animate().rotation(90).scaleX(1f).scaleY(1f).setDuration(1000).start();
                break;
            case R.id.play:
                /*
                caplay.animate().rotation(180).scaleX(1f).scaleY(1f).setDuration(1000).start();
                caplay.animate().rotation(360).scaleX(1f).scaleY(1f).setDuration(1000).start();
                caplay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        caplay.animate().rotation(0).scaleX(1).scaleY(1).setDuration(1000).start();
                    }
                }, 1000);

                play.animate().scaleYBy(360).scaleX(1.2f).scaleY(1.2f).alpha(0.5F).setDuration(1500).start();
                play.animate().alpha(0).scaleXBy(2.0f).scaleYBy(2.0f).setDuration(1500).start();
                play.animate().scaleY(3f).scaleX(3f).alpha(0).setDuration(1500).translationY(500).start();
                play.animate().scaleY(5f).scaleX(5f).alpha(0).rotation(360).setDuration(1500).start();
*/
                break;
            case R.id.oftime:
                previous.animate().rotation(0).scaleX(1).scaleY(1).setDuration(1000).start();
                break;
            case R.id.myselect:
                dialog_servicemusic.playmisc(context, paymnets);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 背景图片模糊效果
     */
    public void starserviceactivity(String path) {
        Glide.with(activity).load(!TextUtils.isEmpty(path) ? path : R.mipmap.room_change_bg).apply(bitmapTransform(new BlurTransformation(25, 10))).into(img_bg);
        myframelayout.seticon(path);
    }

    /**
     * 启动服务可以让服务在后台一直运行
     */
    public void starmiscpalay() {
        Intent intent = new Intent(getApplicationContext(), MyService.class);
        startService(intent);
        bindService(intent, myServiceConnection, BIND_AUTO_CREATE);
    }

    //绑定服务前台操作
    public class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mediaPlayer = (MyService.MyBinder) service;
            Binder();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

    }

    /**
     * 初始化绑定数据进行播放操作
     */
    private void Binder() {

        //读取上次打开读内容
        misc misc = mediaPlayer.getmisc();
        if (misc != null) {
            nametitle.setText(misc.getTitle());
            title.setText(misc.getTitle());
            tag.setText(misc.getTag());
            starserviceactivity(misc.getPicture());
        } else {
            next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_play);
            dialog_servicemusic.playmisc(context, paymnets);
            return;
        }

        //接收数据重新设置播放音频
        if (activitymisc != null) {
            nametitle.setText(activitymisc.getTitle());
            title.setText(activitymisc.getTitle());
            tag.setText(activitymisc.getTag());
            starserviceactivity(activitymisc.getPicture());
            mediaPlayer.setPath(activitymisc.getUrl());
            mediaPlayer.setmisc(activitymisc);

        }

        //音乐播放中开始转动动画
        if (mediaPlayer.isPlaying()) {
            myframelayout.start();
            next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_pause);
            handler.sendEmptyMessage(Config.sussess);                              //发通知
        }
        seekBar.setMax(mediaPlayer.getmyDuration());                               //进度条最大时间
        seekBar.setProgress(mediaPlayer.getcurrentposition());                     //当前进度
        duration.setText(Config.generateTime(mediaPlayer.getcurrentposition()));   //显示时间
        MediaPlayer player = mediaPlayer.getMediaPlayer(); //播放器及监听事件处理
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                myframelayout.start();
                handler.sendEmptyMessage(Config.sussess); //发通知

            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
            }
        });
    }

    /**
     * 通知UI更新
     */
    private class Handlers extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getcurrentposition());
                seekBar.setMax(mediaPlayer.getmyDuration());
                duration.setText(Config.generateTime(mediaPlayer.getcurrentposition()));
                next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_pause);
                hideBottomUIMenu();
                handler.sendEmptyMessageDelayed(Config.sussess, 1000);
            } else {
                next.setImageResource(com.tencent.liteav.trtcvoiceroom.R.drawable.trtcvoiceroom_bgm_play);
                myframelayout.pause();  //停止动画转动
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除前台帮定
        unbindService(myServiceConnection);
        //移除通知内容
        handler.removeCallbacksAndMessages(null);
        mediaPlayer = null; //清空播放数据

    }

    //隐藏虚拟按键，并且全屏
    protected void hideBottomUIMenu() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
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

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess() {
            activitymisc = null;
            Binder();
        }
    };

}
