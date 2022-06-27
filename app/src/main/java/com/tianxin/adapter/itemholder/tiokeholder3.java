package com.tianxin.adapter.itemholder;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.dialog.dialog_Config;
import com.tianxin.listener.Callback;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class tiokeholder3 extends BaseHolder {
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
    PLVideoView video_view;
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


    public tiokeholder3(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_tiktok_layout3, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
        videolist video = (videolist) object;
        // //第二种方式：使用固定图片作为封面图片
        member me = video.getMember();
        String bigpicurl = tiokeholder2.getvideo(video.getBigpicurl(),video.getTencent());
        String picuser = tiokeholder2.getvideo(video.getPicuser(),video.getTencent());
        String playurl =tiokeholder2.getvideo(video.getPlayurl(),video.getTencent());

        if (me != null) {
            String picicon = TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture();
            Glideload.loadImage(mticon, picicon);
        } else {
            Glideload.loadImage(mticon, picuser);
        }

        mticon.setOnClickListener(v -> activity_picenter.open(context, video.getUserid()));
        relayout1.setOnClickListener(v -> ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234)));
        relayout2.setOnClickListener(v -> dialog_Config.Pinglun(context, video.getId()));
        relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context,video));


        //收费视频 非VIP会员模糊显示
        if (video.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0) {
            Glideload.loadImage(mThumb, bigpicurl, 10, 25);
        } else {
            Glideload.loadImage(mThumb, bigpicurl);
        }

        //收费视频背景模糊type 收费视频 vip0 普通会员 本人视频不模糊
        int vip = userInfo.getVip();
        if (video.getType() == Constants.TENCENT && vip == Constants.TENCENT0 && !UserInfo.getInstance().getUserId().equals(video.getUserid())) {
            //视频非VIP模糊显示
            Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(mThumb);
        } else {
            //免费视频正常显示
            Glideload.loadImage(mThumb, bigpicurl, 5);
        }
        video_view.setVideoPath(playurl);
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
        tv1.setText(video.getAnum());
        tv2.setText(video.getPnum());
        tv3.setText(video.getFnum());
    }

    public void bind(Context context, Object object, int position) {
        videolist video = (videolist) object;
        // //第二种方式：使用固定图片作为封面图片
        member me = video.getMember();
        String bigpicurl = tiokeholder2.getvideo(video.getBigpicurl(),video.getTencent());
        String picuser = tiokeholder2.getvideo(video.getPicuser(),video.getTencent());
        String playurl =tiokeholder2.getvideo(video.getPlayurl(),video.getTencent());

        if (me != null) {
            String picicon = TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture();
            Glideload.loadImage(mticon, picicon);
        } else {
            Glideload.loadImage(mticon, picuser);
        }

        mticon.setOnClickListener(v -> activity_picenter.open(context, video.getUserid()));
        relayout1.setOnClickListener(v -> ToastUtil.toastShortMessage(context.getString(R.string.tv_msg234)));
        relayout2.setOnClickListener(v -> dialog_Config.Pinglun(context, video.getId()));
        relayout3.setOnClickListener(v -> tiokeholder2.fenxing(context,video));

        //收费视频 非VIP会员模糊显示
        if (video.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0) {
            Glideload.loadImage(mThumb, bigpicurl, 10, 25);
        } else {
            Glideload.loadImage(mThumb, bigpicurl);
        }

        //收费视频背景模糊type 收费视频 vip0 普通会员 本人视频不模糊
        int vip = userInfo.getVip();
        if (video.getType() == Constants.TENCENT && vip == Constants.TENCENT0 && !UserInfo.getInstance().getUserId().equals(video.getUserid())) {
            //视频非VIP模糊显示
            Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(mThumb);
        } else {
            //免费视频正常显示
            Glideload.loadImage(mThumb, bigpicurl, 5);
        }
        video_view.setVideoPath(playurl);
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
     * //第一种方式：获取视频第一帧作为封面图片
     *
     * @param video
     */
    public void getvideo(videolist video) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(video.getPlayurl());
        video_view.setVideoPath(video.getPlayurl());
        mThumb.setImageBitmap(media.getFrameAtTime());
        mTitle.setText(video.getTitle());
        mMarquee.setText(video.getAlias());
        mMarquee.setSelected(true);
    }

}
