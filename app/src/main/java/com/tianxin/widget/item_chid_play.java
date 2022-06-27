package com.tianxin.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tianxin.BasActivity.BaseFrameLayout;
import com.tianxin.Module.Datamodule;
import com.tianxin.Module.onplVideoViews;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.activity.activity_svip;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.ddialog_jinbi;
import com.tianxin.dialog.dialog_item_vip;
import com.tianxin.dialog.dialog_item_vip2;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.listener.Paymnets;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.tuikit.live.component.common.ActionSheetDialog;
import com.tencent.qcloud.tim.tuikit.live.component.report.ReportController;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.text.DecimalFormat;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 播放操作面板
 */
public class item_chid_play extends BaseFrameLayout implements View.OnClickListener {
    private static final String TAG = item_chid_play.class.getSimpleName();
    private ImageView bgmplay, mhde_img_back, refresh_view, play;
    private ImageView icon;
    private TextView title;
    private TextView mtitle;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView viptype1title;
    private TextView viptype2title;
    private RelativeLayout relayout1;
    private RelativeLayout relayout2;
    private RelativeLayout relayout3;
    private RelativeLayout show_vip;
    private RelativeLayout sRlayout;
    private videolist video;
    private FrameLayout fragment;
    private PLVideoView plMediaPlayer;
    private Datamodule datamodule;
    private MyOpenhelper myOpenhelper;
    private SVGAImageView follow, axicone;
    private SVGAParser parser;  //动画状态

    public item_chid_play(@NonNull Context context) {
        super(context);
        init();
    }

    public item_chid_play(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public item_chid_play(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        userInfo = UserInfo.getInstance();
        context = getContext();
        activity = (Activity) getContext();

        LayoutInflater.from(getContext()).inflate(R.layout.item_chid_play, this);
        sRlayout = findViewById(R.id.sRlayout);
        refresh_view = findViewById(R.id.refresh_view);
        show_vip = findViewById(R.id.show_vip);
        fragment = findViewById(R.id.fragment);
        bgmplay = findViewById(R.id.bgmplay);
        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        mtitle = findViewById(R.id.mtitle);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        viptype1title = findViewById(R.id.viptype1title);
        viptype2title = findViewById(R.id.viptype2title);
        mhde_img_back = findViewById(R.id.mhde_img_back);
        relayout1 = findViewById(R.id.relayout1);
        relayout2 = findViewById(R.id.relayout2);
        relayout3 = findViewById(R.id.relayout3);
        follow = findViewById(R.id.follow);
        axicone = findViewById(R.id.axicone);
        play = findViewById(R.id.play);

        parser = new SVGAParser(context);
        datamodule = new Datamodule(context);
        myOpenhelper = MyOpenhelper.getOpenhelper();
        setListener();
    }

    public void mydateview(videolist video) {
        mtitle.setText("");
        title.setText(video.getTitle());
        tv1.setText(video.getAnum());
        tv2.setText(video.getPnum());
        tv3.setText(video.getFnum());
        //腾讯云
        String presignedURL1 = video.getBigpicurl();
        try {
            presignedURL1 = video.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(video.getBigpicurl()) : video.getBigpicurl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        member member = video.getMember();
        //收费视频 非VIP会员模糊显示video.getType()=1  收费 userInfo.getVip()=0 非VIP
        if (video.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0 && !userInfo.getUserId().equals(video.getUserid())) {
            Glide.with(context).load(presignedURL1).apply(bitmapTransform(new BlurTransformation(2, 15))).override(100, 100).diskCacheStrategy(DiskCacheStrategy.NONE).into(bgmplay);
        } else {
            Glide.with(context).load(presignedURL1).diskCacheStrategy(DiskCacheStrategy.NONE).override(320, 640).into(bgmplay);
        }
        if (member != null) {
            String presignedURL2 = TextUtils.isEmpty(member.getPicture()) ? presignedURL1 : member.getPicture();
            Glide.with(context).load(presignedURL2).diskCacheStrategy(DiskCacheStrategy.NONE).override(100, 100).into(icon);
        } else if (!TextUtils.isEmpty(video.getPicuser())) {
            Glide.with(context).load(video.getPicuser()).diskCacheStrategy(DiskCacheStrategy.NONE).override(100, 100).into(icon);
        } else {
            if (!TextUtils.isEmpty(video.getAvatar())) {
                Glide.with(context).load(video.getAvatar()).diskCacheStrategy(DiskCacheStrategy.NONE).override(100, 100).into(icon);
            } else {
                Glide.with(context).load(R.mipmap.icon_sex_autuor).diskCacheStrategy(DiskCacheStrategy.NONE).override(100, 100).into(icon);
            }

        }

        //背景模糊处理
        //Glide.with(DemoApplication.instance()).load(presignedURL1).apply(bitmapTransform(new BlurTransformation(2, 15))).override(100, 100).into(bgmplay);
        //Glide.with(DemoApplication.instance()).load(video.getPicuser()).apply(bitmapTransform(new BlurTransformation(2, 15))).override(100, 100).into(icon);
        //.apply(bitmapTransform(new BlurTransformation(25, 10)))
        //.apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 10))) //(int radius半径, int sampling取样)


    }

    public ImageView getBgmplay() {
        return bgmplay;
    }

    public ImageView getPlay() {
        return play;
    }

    public void setListener() {
        sRlayout.setOnClickListener(this);
        mhde_img_back.setOnClickListener(this);
        refresh_view.setOnClickListener(this);
        icon.setOnClickListener(this);
        relayout1.setOnClickListener(this);
        relayout2.setOnClickListener(this);
        relayout3.setOnClickListener(this);
        follow.setOnClickListener(this);
        relayout3.setOnClickListener(this);
    }

    public static item_chid_play show(Context context, videolist videolist) {
        item_chid_play item_chid_play = new item_chid_play(context);
        item_chid_play.mydateview(videolist);
        return item_chid_play;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sRlayout:
                if (plMediaPlayer.isPlaying()) {
                    plMediaPlayer.pause();
                    play.animate().alpha(1f).setDuration(300).start();
                } else {
                    plMediaPlayer.start();
                    play.animate().alpha(0f).setDuration(300).start();
                }
                break;
            case R.id.mhde_img_back:
                //关闭ACTIVITY
                Activity activity = (Activity) context;
                activity.finish();
                break;
            case R.id.refresh_view:
                //举报
                reportUser(context, video, 0);
                break;
            case R.id.icon:
                //转到主页
                if (TextUtils.isEmpty(video.getUserid())) {
                    break;
                }
                activity_picenter.open(context, video.getUserid());
                break;
            case R.id.follow:
                //关注对方
                SVGAImageView();
                break;
            case R.id.relayout1:
                //感谢支持
                ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234));
                SVGAImageView(axicone, "svag_dynamic_accost.svga");
                break;
            case R.id.relayout2:
                //视频评论
                dialog_Config.Pinglun(context, video.getId());
                break;
            case R.id.relayout3:
                //分享
                dialog_Config.fenxing(context);
                break;
            case R.id.viptype2title:
                //升级VIP会员
                activitysvip();
                break;
        }
    }

    /**
     * 播放视频提示升级VIP会员
     */
    private void dialogShowVideo(videolist video) {
        show_vip.setVisibility(VISIBLE);
        viptype1title.setText(R.string.svip1);
        viptype2title.setText(R.string.svip2);
        viptype2title.setOnClickListener(this::onClick);
        //弹出升级VIP提示
        dialog_item_vip2.dialogitemvip(context, paymnets, video);
    }

    /**
     * 浏览图片提示升级VIP会员
     */
    private void dialogShowImag() {
        dialog_item_vip.dialogitemvip(context, 100, paymnets);
    }

    /**
     * 升级VIP会员
     */
    private void activitysvip() {
        context.startActivity(new Intent(context, activity_svip.class));
    }

    /**
     * 举报
     */
    public static void reportUser(Context context, videolist video, int TYPE) {
        final ReportController reportController = new ReportController();
        String[] reportItems = reportController.getReportItems();
        ActionSheetDialog actionSheetDialog = new ActionSheetDialog(context);
        actionSheetDialog.builder();
        actionSheetDialog.setCancelable(false);
        int itemColor = context.getResources().getColor(R.color.half_transparent);
        actionSheetDialog.addSheetItems(reportItems, itemColor, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which, String text) {
                if (video == null) {
                    return;
                }
                //观众本人的id mSelfUserId
                String mSelfUserId = UserInfo.getInstance().getUserId(); //举报人的ID
                String reportUserId = video.getUserid();                 //被举报的ID

                if (mSelfUserId.equals(reportUserId)) {
                    ToastUtil.toastShortMessage(context.getString(R.string.tm47) + "");
                    return;
                }

                //提交举报内容
                reportController.reportUser(mSelfUserId, reportUserId, text, video.getId(), TYPE);

            }
        });
        actionSheetDialog.show();
    }

    /**
     * 设置地址播放视频
     *
     * @param plMediaPlayer
     * @param video
     */
    public void playvideo(PLVideoView plMediaPlayer, videolist video) {
        this.video = video;
        this.plMediaPlayer = plMediaPlayer;
        this.fragment.addView(plMediaPlayer);
        this.show(video);

        //本地数据库找到该条视频说明已经付费
        if (myOpenhelper.Query(MyOpenhelper.videolist, Integer.parseInt(video.getId()), userInfo.getUserId(), 1).size() > 0) {
            videoplay();
            return;
        }

        //收费视频类型 VIP会员付费视频 普通会员无权浏览
        //video.getType()   收费会员
        //userInfo.getVip() 会员不是VIP
        //!userInfo.getUserId().equals(video.getUserid()) 不是自己的视频
        //video.getJinbi() > 0 //金币大于0的
        if (video.getJinbi() > 0 || video.getType() == Constants.TENCENT) {
            if (userInfo.getVip() == 0 && !userInfo.getUserId().equals(video.getUserid())) {
                dialogShowVideo(video); //播放视频提示升级VIP会员
            } else {
                videoplay();//免费视频执行播放操作
            }
        } else {
            videoplay();//免费视频执行播放操作
        }
    }


    private void show(videolist video) {
        if (!TextUtils.isEmpty(video.getUserid())) {
            if (video.getUserid().equals(userInfo.getUserId())) {
                follow.setVisibility(GONE);
            } else {
                //查询对方是否已被关注
                datamodule.getfollowok(video.getUserid(), paymnets4);
            }
        } else {
            follow.setVisibility(GONE);
        }
    }

    /**
     * //视频执行播放操作
     */
    private void videoplay() {
        show_vip.setVisibility(GONE);
        String presignedURL = video.getPlayurl();
        try {
            presignedURL = video.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(video.getPlayurl()) : video.getPlayurl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        presignedURL = !TextUtils.isEmpty(video.getPlaytest()) ? video.getPlaytest() : presignedURL;

        //设置视频本地缓存播放
        //HttpProxyCacheServer proxy = DemoApplication.getProxy();
        //String proxyUrl = proxy.getProxyUrl(presignedURL);

        plMediaPlayer.setVideoPath(presignedURL);
        new onplVideoViews(context, plMediaPlayer, bgmplay, play);
    }

    /**
     * N万点赞
     *
     * @param val
     * @return
     */
    public String formatNumber(long val) {
        if (val < 10000) {
            return val + "";
        }
        DecimalFormat df = new DecimalFormat("######0.0");
        double d = val / 10000.0;
        return df.format(d) + "万";
    }

    /**
     * 点击购买提示回调
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onClick() {
            activitysvip(); //传到购买VIP会员
        }

        @Override
        public void onSuccess() {
            //支付金币
            ddialog_jinbi.myshow(context, paymnets2, video);
        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void payens() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toashow.show(getContext().getString(R.string.tm94));
                    //转到个人主页
                    activity_picenter.setActionactivity(getContext(), video.getUserid());
                }
            });
        }

        @Override
        public void onError() {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toashow.show("已取消息关注");
                }
            });

        }

    };

    /**
     * 支付金币成功回调
     */
    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void onFail() {
            Toashow.show(getContext().getString(R.string.tv_paylay));
        }

        @Override
        public void onSuccess() {
            if (video != null) {
                datamodule.jinbi(paymnets3, video);
            }
        }
    };

    /**
     * 支付金币成功回调
     */
    private Paymnets paymnets3 = new Paymnets() {
        @Override
        public void onFail() {
            Toashow.show(context.getString(R.string.eorrfali3));
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(context.getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess() {
            //保存支付成功视频标识ID在本地中 更换手机需要重新支付金币
            if (myOpenhelper.Query(MyOpenhelper.videolist, Integer.parseInt(video.getId()), userInfo.getUserId(), 1).size() == 0) {
                myOpenhelper.insert1(MyOpenhelper.videolist, userInfo.getUserId(), Integer.parseInt(video.getId()), 1);
                Log.d(TAG, "onSuccess: " + userInfo.getUserId());
            }
            videoplay();
        }

        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }
    };

    private Paymnets paymnets4 = new Paymnets() {
        @Override
        public void payens() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    follow.setVisibility(GONE);
                    Log.d(TAG, "已关注");
                }
            });
        }

        @Override
        public void onError() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    follow.setVisibility(VISIBLE);
                    Log.d(TAG, "未关注");
                }
            });

        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }


    };

    /**
     * 加载远端服务器中的动画
     */
    private void SVGAImageView() {
        parser.decodeFromAssets("user_transition_follow.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity videoItem) {
                SVGADrawable drawable = new SVGADrawable(videoItem);
                //正在播放动画
                if (follow != null) {
                    follow.setImageDrawable(drawable);
                    follow.startAnimation();
                    follow.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {
                            Log.d(TAG, "onPause: ");
                        }

                        @Override
                        public void onFinished() {
                            Log.d(TAG, "onFinished: ");
                            follow.stopAnimation();
                            follow.setVisibility(GONE);
                            datamodule.gofollowlist(video.getUserid(), paymnets);


                        }

                        @Override
                        public void onRepeat() {
                            Log.d(TAG, "onRepeat: ");

                        }

                        @Override
                        public void onStep(int i, double v) {
                            Log.d(TAG, "onStep: ");

                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });


    }

    /**
     * 加载远端服务器中的动画
     */
    private void SVGAImageView(SVGAImageView svgaImageView, String Assets) {
        parser.decodeFromAssets(Assets, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity videoItem) {
                SVGADrawable drawable = new SVGADrawable(videoItem);
                //正在播放动画
                if (svgaImageView != null) {
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                    svgaImageView.setCallback(new SVGACallback() {
                        @Override
                        public void onPause() {
                            Log.d(TAG, "onPause: ");

                        }

                        @Override
                        public void onFinished() {
                            Log.d(TAG, "onFinished: ");
                            follow.setVisibility(GONE);
                        }

                        @Override
                        public void onRepeat() {
                            Log.d(TAG, "onRepeat: ");

                        }

                        @Override
                        public void onStep(int i, double v) {
                            Log.d(TAG, "onStep: ");

                        }
                    });
                }
            }

            @Override
            public void onError() {

            }
        });


    }

}
