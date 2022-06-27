package com.tianxin.adapter.itemholder;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.activity.picenter.activity_personalhome;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.dialog.dialog_list_trend;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class tiokeholder1 extends BaseHolder {
    private static final String TAG = tiokeholder1.class.getSimpleName();
    @BindView(R.id.mRootView)
    RelativeLayout mRootView;
    @BindView(R.id.mThumb)
    ImageView mThumb;
    @BindView(R.id.mticon)
    ImageView mticon;
    @BindView(R.id.mPlay)
    ImageView mPlay;
    @BindView(R.id.mTitle)
    TextView mTitle;
    @BindView(R.id.mMarquee)
    TextView mMarquee;
    @BindView(R.id.video_view)
    player video_view;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.relayout1)
    LinearLayout relayout1;
    @BindView(R.id.relayout2)
    LinearLayout relayout2;
    @BindView(R.id.relayout3)
    LinearLayout relayout3;
    @BindView(R.id.circleimageview)
    ImageView circleimageview;
    @BindView(R.id.topPanel)
    View topPanel;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.daytetime)
    TextView daytetime;

    public tiokeholder1(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_tiktok_layout, parent, false));

    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
        videolist video = (videolist) object;
        //第二种方式：使用固定图片作为封面图片
        member me = video.getMember();
        String bigpicurl = tiokeholder2.getvideo(video.getBigpicurl(), video.getTencent());
        String picuser = tiokeholder2.getvideo(video.getPicuser(), video.getTencent());
        String playurl = tiokeholder2.getvideo(video.getPlayurl(), video.getTencent());
        String playtest = tiokeholder2.getvideo(video.getPlaytest(), video.getTencent());
        String path = TextUtils.isEmpty(playtest) ? playurl : playtest;

        if (me != null) {
            String picicon = TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture();
            Glideload.loadImage(mticon, picicon);

        } else {
            if (TextUtils.isEmpty(picuser) && video.getUserid().equals(userInfo.getUserId())) {
                Glideload.loadImage(mticon, userInfo.getAvatar());
            } else {
                Glideload.loadImage(mticon, picuser);
            }

        }
        relayout1.setOnClickListener(v -> ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234)));
        relayout2.setOnClickListener(v -> dialog_Config.Pinglun(context, video.getId()));
        relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context, video));
        mticon.setOnClickListener(v -> activity_personalhome.starsetAction(context, video.getUserid()));

        //收费视频背景模糊type 收费视频 vip0 普通会员 并非本人视频模糊
        int vip = userInfo.getVip();
        if (video.getType() == Constants.TENCENT && vip == Constants.TENCENT0 && !userInfo.getUserId().equals(video.getUserid())) {
            //视频非VIP模糊显示
            Glideload.loadImage(mThumb, bigpicurl, 10, 25);
            Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(mThumb);
        } else {
            //免费视频正常显示
            video_view.setPath(path);
            ImageLoadHelper.glideShowImageWithUrl(context, bigpicurl, mThumb);
        }
        mThumb.setTag(position); //指定position
        video_view.setTag(video);//播放video
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
        tv1.setText(video.getAnum());
        tv2.setText(video.getPnum());
        tv3.setText(video.getFnum());
    }

    public void bind(Context context, Object object, int position) {
        videolist video = (videolist) object;
        String bigpicurl = tiokeholder2.getvideo(video.getBigpicurl(), video.getTencent());
        String picuser = tiokeholder2.getvideo(video.getPicuser(), video.getTencent());
        String playurl = tiokeholder2.getvideo(video.getPlayurl(), video.getTencent());
        String playtest = tiokeholder2.getvideo(video.getPlaytest(), video.getTencent());

        member me = video.getMember();
        if (me != null) {
            String picicon = TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture();
            Glideload.loadImage(mticon, picicon);

        } else {
            Glideload.loadImage(mticon, picuser);
        }

        relayout1.setOnClickListener(v -> ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234)));
        relayout2.setOnClickListener(v -> dialog_Config.Pinglun(context, video.getId()));
        relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context, video));

        mticon.setOnClickListener(v -> activity_personalhome.starsetAction(context, video.getUserid()));

        //收费视频背景模糊type 收费视频 vip0 普通会员 本人视频不模糊
        int vip = userInfo.getVip();
        if (video.getType() == Constants.TENCENT && vip == Constants.TENCENT0 && !UserInfo.getInstance().getUserId().equals(video.getUserid())) {
            //视频非VIP模糊显示
            Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(mThumb);
        } else {
            //免费视频正常显示
            Glideload.loadImage(mThumb, bigpicurl, 5);
        }
        String path = TextUtils.isEmpty(playtest) ? playurl : playtest;
        video_view.setPath(path);
        mThumb.setTag(position); //指定position
        video_view.setTag(video);//播放video
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
        tv1.setText(video.getAnum());
        tv2.setText(video.getPnum());
        tv3.setText(video.getFnum());


    }

    /**
     * 相册视频滑动
     *
     * @param context
     * @param object
     * @param callback
     */
    public void bind(Context context, Object object, Callback callback) {
        videolist video = (videolist) object;
        String bigpicurl = tiokeholder2.getvideo(video.getBigpicurl(), video.getTencent());
        String picuser = tiokeholder2.getvideo(video.getPicuser(), video.getTencent());
        String playurl = tiokeholder2.getvideo(video.getPlayurl(), video.getTencent());
        String playtest = tiokeholder2.getvideo(video.getPlaytest(), video.getTencent());
        member me = video.getMember();
        Glideload.loadImage(mticon, me != null ? TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture() : bigpicurl);
        relayout1.setOnClickListener(v -> ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234)));
        relayout2.setOnClickListener(v -> dialog_list_trend.show(context, Integer.parseInt(video.getId()), Integer.parseInt(video.getUserid())));
        mticon.setOnClickListener(v -> activity_personalhome.starsetAction(context, video.getUserid()));

        //收费视频背景模糊type 收费视频 vip0 普通会员 本人视频不模糊
        int vip = userInfo.getVip();
        if (video.getType() == Constants.TENCENT && vip == Constants.TENCENT0 && !userInfo.getUserId().equals(video.getUserid())) {
            //视频非VIP模糊显示和不是我的视频
            Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(mThumb);
        } else {
            //我的视频或大家免费视频正常显示
            ImageLoadHelper.glideShowImageWithUrl(context, bigpicurl, mThumb);
        }
        String path = TextUtils.isEmpty(playtest) ? playurl : playtest;
        video_view.setPath(path);
        mThumb.setTag(position); //指定position
        video_view.setTag(video);//播放video

        video_view.setListener(new player.VideoPlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                if (callback != null) {
                    video_view.start();
                    callback.onCompletion(iMediaPlayer);
                }
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
                        if (callback != null) {
                            callback.onInfo(iMediaPlayer, i, i1);
                        }
                        //Android属性动画(Animator)
                        //translationX和translationY	x轴和y轴的偏移量
                        //rotation、rotationX和rotationY	围绕支点旋转
                        //rotation(360) 围绕支点旋转
                        //scaleX和scaleY	缩放
                        //alpha	透明度
                        //x(5000).y(5000)  移动
                        //xBy(500).xBy(500) 移动
                        //image.animate().alpha(0f).setDuration(1000).y(100).x(310).translationX(500).translationY(500).rotation(360).start();
                        mPlay.animate().alpha(0f).setDuration(500).start();
                        mThumb.animate().alpha(0f).setDuration(1000).start();
                        circleimageanimation();
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
                long duration = video_view.getDuration();
                long pointerIcon = video_view.getCurrentPosition();
                seekBar.setProgress((int) pointerIcon);
                seekBar.setMax((int) duration);
                if (video.getType() == Constants.TENCENT && userInfo.getVip() == 0 && !userInfo.getUserId().equals(video.getUserid())) {
                    video_view.pause();
                }

            }
        });
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
        tv1.setText(video.getAnum());
        tv2.setText(video.getPnum());
        tv3.setText(video.getFnum());
    }

    @Override
    public void OnClick(View v) {

    }

    /**
     * 第一种方式：获取视频第一帧作为封面图片
     *
     * @param video
     */
    public void getvideo(videolist video) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(video.getPlayurl());
        video_view.setPath(video.getPlayurl());
        mThumb.setImageBitmap(media.getFrameAtTime());
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
    }

    private void circleimageanimation() {
        //360动画转动走起
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.playmisc);
        circleimageview.startAnimation(animation); //播放动画
    }

}

