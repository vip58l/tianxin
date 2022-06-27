package com.tianxin.activity.video2.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.dialog.Dialog_bottom;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.Camera.MediaPlayHelper;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 弹窗视频接听
 */
public class activity_video extends BasActivity2 {
    String TAG = activity_video.class.getSimpleName();
    @BindView(R.id.send1)
    TextView send1;
    @BindView(R.id.send2)
    TextView send2;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.desc2)
    TextView desc2;
    @BindView(R.id.play_mp)
    player play_mp;
    @BindView(R.id.exit)
    ImageView exit;
    @BindView(R.id.image)
    ImageView image;
    int higt = 160;
    private cs_alipay csAlipay;
    public static int call_state = 0;

    public static void starsetAction(Context context, String json) {
        Intent intent = new Intent();
        intent.setClass(context, activity_video.class);
        intent.putExtra(Constants.JSON, json);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        // 应用运行时，保持不锁屏、全屏化
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE); //不允许截屏
        return R.layout.call_video;
    }

    @Override
    public void iniview() {
        Resources res = getResources();
        Drawable ic_dialing = res.getDrawable(R.drawable.ic_dialing);
        ic_dialing.setBounds(0, 0, higt, higt);
        send1.setCompoundDrawables(null, ic_dialing, null, null);

        Drawable ic_hangup = res.getDrawable(R.drawable.ic_hangup);
        ic_hangup.setBounds(0, 0, higt, higt);
        send2.setCompoundDrawables(null, ic_hangup, null, null);
        MediaPlayHelper.playHelper(context).start(R.raw.ring_tone, 10001);

        //底部导航黑色背景
        StatusBarUtil.setNavigationBarColor(activity);
    }

    @Override
    public void initData() {
        String json = getIntent().getStringExtra(Constants.JSON);
        videolist video = new Gson().fromJson(json, videolist.class);
        csAlipay = new cs_alipay(context, play);
        play_mp.setPath(video.getPlayurl());
        play_mp.setListener(new player.VideoPlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                //iMediaPlayer.start();
                Log.d(TAG, "onCompletion: ");
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG, "onError: ");
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                switch (i) {
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START: //媒体信息视频渲染开始
                        Log.d(TAG, "媒体信息视频渲染开始: ");
                        play_mp.setVolume(0, 0);
                        play_mp.setLooping(true);
                        image.animate().alpha(0f).setDuration(500).start();
                        return true;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:   //媒体信息视频跟踪滞后
                        Log.d(TAG, "媒体信息视频跟踪滞后: ");
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:       //媒体信息缓冲启动
                        Log.d(TAG, " 媒体信息缓冲启动");
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:         //媒体信息缓冲结束
                        Log.d(TAG, "媒体信息缓冲结束");
                        return true;
                }
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG, "onPrepared: ");
            }
        });
        name.setText(video.getTitle());
        desc.setText(video.getAlias());
        desc2.setText(video.getDistrict());
        if (!TextUtils.isEmpty(video.getDistrict())) {
            desc2.setVisibility(View.VISIBLE);
        }
        ImageLoadHelper.glideShowCornerImageWithUrl(context, video.getBigpicurl(), icon);
        ImageLoadHelper.glideShowImageWithUrl(context, video.getAvatar(), image);

        //视频播放中配合服务发起视频通话标识
        call_state = 1;
    }

    @Override
    @OnClick({R.id.send1, R.id.send2, R.id.exit})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                if (UserInfo.getInstance().getJinbi() > 100) {
                    finish();
                } else {
                    Toashow.show(getString(R.string.tm135));
                    Dialog_bottom.dialogsBottom(context, play);

                    //我的金币充值金币
                    //Detailedlist.starsetAction(context);
                }
                break;
            case R.id.send2:
            case R.id.exit:
                finish();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    private Paymnets play = new Paymnets() {
        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onSuccess: ");
                }
            });
        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            switch (TYPE) {
                case activity_svip.zfb:
                    //发起支付请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case activity_svip.wx:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (play_mp != null) {
            play_mp.start();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        MediaPlayHelper.playHelper(context).getmMediaPlayer().start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (play_mp != null) {
            play_mp.pause();
            MediaPlayHelper.playHelper(context).getmMediaPlayer().pause();
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (play_mp != null) {
            play_mp.stop();
            play_mp.release();
            play_mp = null;
        }
        MediaPlayHelper.playHelper(context).start(com.tencent.qcloud.tim.uikit.R.raw.hang_up, 2000);
        call_state = 0; //已退出视频配合服务发起视频通话标识
    }

    /**
     * 全屏显示
     */
    protected void hideBottomUIMenu() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            decorView.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE     //使状态栏和导航栏真正的进入沉浸模式。点击屏幕任意区域，不会退出全屏模式，只有用户上下拉状态栏或者导航栏时才会退出
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //将布局内容拓展到导航栏和状态栏的后面
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN       //将布局内容拓展到状态栏的后面
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY        //使用，使状态栏和导航栏真正的进入沉浸模式。点击屏幕任意区域，不会退出全屏模式，只有用户上下拉状态栏或者导航栏时才会退出
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION         //隐藏导航栏，点击屏幕任意区域，导航栏将重新出现
                    | View.SYSTEM_UI_FLAG_FULLSCREEN              //隐藏状态栏，从状态栏位置下拉，状态栏将重新出现
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;               //当用户上下拉状态栏或者导航栏时，这些系统栏会以半透明的状态显示，并且在一段时间后消失
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}