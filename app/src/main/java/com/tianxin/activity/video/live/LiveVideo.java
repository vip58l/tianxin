package com.tianxin.activity.video.live;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tianxin.Module.api.moneylist;
import com.tianxin.Receiver.MyService;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.ViewPager.YPagerAdapter2;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.utils.Constants;
import com.tianxin.widget.Movie_zbativity;
import com.tianxin.widget.zbVideo;
import com.tianxin.wxapi.WXpayObject;
import com.pili.pldroid.player.AVOptions;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.item;
import com.tianxin.Module.onplVideoViewpage;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.getlist.HotRooms;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.youngkaaa.yviewpager.YViewPager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LiveVideo extends BaseActivity {
    String TAG = LiveVideo.class.getName();
    @BindView(R.id.viewpager)
    YViewPager viewpager;
    List<View> viewList;
    List<item> list;
    YPagerAdapter2 yPagerAdapter2;
    int position;
    int mPlayingPosition;
    int mCurrentItems;
    int couneroo; //连续播失败5次后退出
    String playurl;//当前正在播放的连接
    PLVideoView plVideoView;
    ImageView zbbg;

    private livegetDate livegetDate;
    private Movie_zbativity movie;
    private boolean getsetVolume = false;
    private cs_alipay csAlipay;


    @Override
    public int getLayoutId() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.activity_mainvideo_zb;
    }

    @Override
    public void initView() {
        //在Activity中停止Service
        if (Config.isServiceRunning("MyService")) {
            stopService(new Intent(DemoApplication.instance(), MyService.class));
        }

        //初始化实例
        livegetDate = new livegetDate();
        csAlipay = new cs_alipay(context);

        //导航底部黑色
        Config.AsetctivityBLACK(this);

        //Android中设置禁止用户截屏
        Config.mysetFlags(this);

        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //同时将界面改为resize已达到软键盘弹出时Fragment不会跟随移动
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //播放器创建
        plVideoView = new PLVideoView(context);
        plVideoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);//满屏显示
        plVideoView.setLooping(true);
        setAVOptions();

        //接收数据并创建视频界面视频view
        Intent intent = getIntent();
        position = intent.getIntExtra(Constants.POSITION, 0);
        list = (List<item>) intent.getSerializableExtra(Constants.list);
        viewList = livegetDate.CreateViewList(context, list);
        viewpager.setAdapter(yPagerAdapter2 = new YPagerAdapter2(context, viewList));
        viewpager.addOnPageChangeListener(new YViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mCurrentItems = i;
                playsetResult(i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        starplayvideo(mCurrentItems);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                //log.d("onPageScrollStateChangedC" + i);
                if (i == View.SCROLLBAR_POSITION_DEFAULT) {

                }
            }
        });
        viewpager.setCurrentItem(position);
        viewpager.setOffscreenPageLimit(3);

        //默认0即立即播放
        if (position == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    starplayvideo(0);
                }
            });
        }

    }


    /**
     * 初始化播放资源
     * 加锁的方式是给方法加锁，加的是当前对象锁synchronized
     * 加入新的界面和视频资源
     *
     * @param mCurrentItem
     */
    private synchronized void starplayvideo(int mCurrentItem) {

        //每次进入时先移除之前的播放器
        if (plVideoView != null) {
            plVideoView.stopPlayback();
            ViewParent parent = plVideoView.getParent();
            if (parent instanceof FrameLayout) {
                ((ViewGroup) parent).removeView(plVideoView);
            }
            if (zbbg != null) {
                zbbg.animate().alpha(1f).start();
                ViewParent parent1 = zbbg.getParent();
                if (parent1 instanceof FrameLayout) {
                    ((ViewGroup) parent1).removeAllViews();
                }
            }
        }

        //重新获取最新数据源
        HotRooms Rooms = (HotRooms) list.get(mCurrentItem).object;
        //记录当前页播放的视频地址 用于Eorr失败后重新加载播放
        playurl = Rooms.getHdlUrl();
        //获取视图布局
        zbVideo itemview = (zbVideo) viewList.get(mCurrentItem);
        if (plVideoView != null) {
            //插入视频播放器
            FrameLayout frameLayout = itemview.fragment;
            frameLayout.addView(plVideoView);
            //取出数据源背景图片
            zbbg = itemview.zbbg;
            //设置播放源地址并设置监听
            plVideoView.setVideoPath(Rooms.getHdlUrl());
            plVideoView.setOnPreparedListener(new onplVideoView());
            plVideoView.setOnCompletionListener(new onplVideoView());
            plVideoView.setOnErrorListener(new onplVideoView());
            plVideoView.setOnInfoListener(new onplVideoView());
            plVideoView.setOnVideoSizeChangedListener(new onplVideoView());//该回调用于监听当前播放器已经缓冲的数据量占整个视频时长的百分比，在播放直播流中无效，仅在播放文件和回放时才有效
            plVideoView.setOnSeekCompleteListener(new onplVideoView());//该回调用于监听当前播放的视频流的尺寸信息，在 SDK 解析出视频的尺寸信息后，会触发该回调，开发者可以在该回调中调整 UI 的视图尺寸
            mPlayingPosition = mCurrentItem;//ViewPage标识知道滚动到条N条数据
        }

        //创建两个视图控制器
        setcviewpager(itemview, Rooms);

    }

    /**
     * 视频播放器监听
     */
    private class onplVideoView extends onplVideoViewpage {

        @Override
        public void onCompletion() {
            plVideoView.start();
        }

        @Override
        public void onPrepared(int i) {
            plVideoView.start();
            zbbg.animate().alpha(0f).setDuration(500).start();
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

        @Override
        public boolean onError(int i, Object o) {
            switch (i) {
                case MEDIA_ERROR_UNKNOWN://	-1	未知错误
                    finish();
                    break;
                case ERROR_CODE_OPEN_FAILED: //-2	播放器打开失败
                    couneroo++;
                    if (couneroo < 5) {
                        plVideoView.setVideoPath(playurl);
                        plVideoView.start();
                    } else {
                        ToastUtil.toastLongMessage("播放器打开失败");
                        finish();
                    }
                    break;
                case ERROR_CODE_IO_ERROR://-3
                    // 网络异常
                    if (Config.networkConnected(LiveVideo.this)) {
                        plVideoView.pause();
                        ToastUtil.toastLongMessage("主播已下线/或网络异常");
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
            switch (i1) {
                case MEDIA_INFO_BUFFERING_START:
                    if (plVideoView.isPlaying()) {
                        plVideoView.pause();
                    }
                    break;
                case MEDIA_INFO_BUFFERING_END:
                case MEDIA_INFO_VIDEO_RENDERING_START:
                    plVideoView.start();
                    break;

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (plVideoView != null) {
            plVideoView.pause();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (plVideoView != null) {
            plVideoView.start();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (plVideoView != null) {
            plVideoView.stopPlayback();
            plVideoView = null;
            viewList.clear();
            list.clear();
        }

        if (movie != null) {
            movie.timer.cancel();
            movie.task.cancel();
            movie.mAnimationUrlList.clear();
            movie.mAnimationUrlList2.clear();
            movie.svgaImageView.clear();
            movie.svgaImageView = null;
            movie.parser = null;
        }
    }


    private void playsetResult(int position) {
        Intent data = new Intent();
        data.putExtra("position", position);
        setResult(position, data);
    }

    /**
     * PLDroidPlayer SDK 提供的 AVOptions 类，可以用来配置播放参数
     */
    private void setAVOptions() {
        AVOptions options = new AVOptions();

        // DNS 服务器设置
        // 若不设置此项，则默认使用 DNSPod 的 httpdns 服务
        // 若设置为 127.0.0.1，则会使用系统的 DNS 服务器
        // 若设置为其他 DNS 服务器地址，则会使用设置的服务器
        //options.setString(AVOptions.KEY_DNS_SERVER, server);

        // DNS 缓存设置
        // 若不设置此项，则每次播放未缓存的域名时都会进行 DNS 解析，并将结果缓存
        // 参数为 String[]，包含了要缓存 DNS 结果的域名列表
        // SDK 在初始化时会解析列表中的域名，并将结果缓存
        //options.setStringArray(AVOptions.KEY_DOMAIN_LIST, domainList);

        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);


        //本地缓存功能，目前只支持 HTTP(s)-mp4 文件点播，开启本地缓存功能，只需要在播放前，配置缓存目录即可
        // 目前只支持 mp4 点播
        // 设置本地缓存目录
        //String savePathDir = getExternalCacheDir().toString();

        /**
         * 缓存时的本地父目录
         */
        //File downloadParentDir = getExternalFilesDir("");
        //options.setString(AVOptions.KEY_CACHE_DIR, downloadParentDir.toString());

        // 设置本地缓存文件的后缀名
        // 只有在设置了缓存目录后才会生效
        // 一个流地址在设置了自定义后缀名后，再次播放前必须设置相同的后缀名，否则无法打开
        // 默认值是 mp4
        //options.setString(AVOptions.KEY_CACHE_EXT, "ext");

        // 若设置为 1，则底层会进行一些针对直播流的优化
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);

        // 快开模式，启用后会加快该播放器实例再次打开相同协议的视频流的速度
        options.setInteger(AVOptions.KEY_FAST_OPEN, 1);

        // 打开重试次数，设置后若打开流地址失败，则会进行重试
        options.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);

        // 预设置 SDK 的 log 等级， 0-4 分别为 v/d/i/w/e
        options.setInteger(AVOptions.KEY_LOG_LEVEL, 2);

        // 打开视频时单次 http 请求的超时时间，一次打开过程最多尝试五次
        // 单位为 ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);

        // 默认的缓存大小，单位是 ms
        // 默认值是：500
        //options.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 5000);

        // 最大的缓存大小，单位是 ms
        // 默认值是：2000，若设置值小于 KEY_CACHE_BUFFER_DURATION 则不会生效
        //options.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 5000);

        // 是否开启直播优化，1 为开启，0 为关闭。若开启，视频暂停后再次开始播放时会触发追帧机制
        // 默认为 0
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);

        // 设置拖动模式，1 位精准模式，即会拖动到时间戳的那一秒；0 为普通模式，会拖动到时间戳最近的关键帧。默认为 0
        options.setInteger(AVOptions.KEY_SEEK_MODE, 0);

        // 设置 HLS DRM 密钥
        //byte[] key = {0x##,0x##,0x##,0x##,0x##, ……};
        //options.setByteArray(AVOptions.KEY_DRM_KEY, key);

        // 设置 MP4 DRM 密钥
        //String key = "AbcDefgh";
        //options.setString(AVOptions.KEY_COMP_DRM_KEY, key);

        // 设置偏好的视频格式，设置后会加快对应格式视频流的打开速度，但播放其他格式会出错
        // m3u8 = 1, mp4 = 2, flv = 3
        //options.setInteger(AVOptions.KEY_PREFER_FORMAT, 1);

        // 开启解码后的视频数据回调
        // 默认值为 0，设置为 1 则开启
        //options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);

        // 开启解码后的音频数据回调
        // 默认值为 0，设置为 1 则开启
        // options.setInteger(AVOptions.KEY_VIDEO_DATA_CALLBACK, 1);

        // 设置开始播放位置
        // 默认不开启，单位为 ms
        //options.setInteger(AVOptions.KEY_START_POSITION, 10 * 1000);

        // 请在开始播放之前配置
        plVideoView.setAVOptions(options);


    }

    /**
     * 创建内部头像聊在界面布局
     *
     * @param itemview
     * @param hotRooms
     */
    public void setcviewpager(zbVideo itemview, HotRooms hotRooms) {

        Log.d(TAG, "user_save_update_Profile: " + UserInfo.getInstance().toString());

        if (movie != null) {
            getsetVolume = movie.isGetsetVolume();//上一个播放器状态
            movie.timer.cancel();
            movie.task.cancel();
            movie.mAnimationUrlList.clear();
            movie.mAnimationUrlList2.clear();
            movie.svgaImageView.clear();
            movie.svgaImageView = null;
            movie.parser = null;
        }

        //头像 内容之类的交互点击送礼物
        movie = new Movie_zbativity(context);
        movie.Movie(hotRooms);
        movie.setPlay(plVideoView);
        movie.setGetsetVolume(getsetVolume);
        movie.setPaymnets(paymnets);

        //设置2个视图文件
        List<View> listvies = new ArrayList<>();
        View view = new View(context);

        //1是空的view
        listvies.add(view);
        //2是交互界面
        listvies.add(movie);

        itemview.viewpager.setAdapter(new pageadapter(context, listvies));
        itemview.viewpager.setCurrentItem(1);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (movie.linmsess.getVisibility() == VISIBLE) {
            movie.linmsess.setVisibility(GONE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public Paymnets paymnets = new Paymnets() {
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
                case 1:
                    //发起支付宝请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }
    };

}
