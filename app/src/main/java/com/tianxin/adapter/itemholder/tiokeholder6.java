package com.tianxin.adapter.itemholder;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.opensource.svgaplayer.SVGAImageView;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.listener.Callback;
import com.pili.pldroid.player.widget.PLVideoView;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class tiokeholder6 extends BaseHolder {
    @BindView(R.id.bgmplay)
    ImageView bgmplay;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.video_view)
    PLVideoView video;

    @BindView(R.id.rightlayout)
    LinearLayout rightlayout;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.follow)
    SVGAImageView follow;

    @BindView(R.id.relayout1)
    RelativeLayout relayout1;
    @BindView(R.id.axicone)
    SVGAImageView axicone;
    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.relayout2)
    RelativeLayout relayout2;
    @BindView(R.id.icon_jiaobiao_hot)
    ImageView icon_jiaobiao_hot;
    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.relayout3)
    RelativeLayout relayout3;
    @BindView(R.id.vipshow)
    ImageView vipshow;
    @BindView(R.id.tv3)
    TextView tv3;

    @BindView(R.id.lin1date)
    LinearLayout lin1date;
    @BindView(R.id.timeimg)
    ImageView timeimg;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.show_vip)
    RelativeLayout show_vip;

    public tiokeholder6(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.item_chid_play2, parent, false));
    }

    public void bind(Object object) {
        if (object instanceof videolist) {
            videolist play = (videolist) object;
            String path = TextUtils.isEmpty(play.getPlaytest()) ? play.getPlayurl() : play.getPlaytest();
            path = tiokeholder2.getvideo(path, play.getTencent());
            video.setVideoPath(path);
            video.setTag(play);
            video.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
            title.setText(play.getTitle());
            tv1.setText(play.getAnum());
            tv2.setText(play.getPnum());
            tv3.setText(play.getFnum());

            //腾讯云
            String bigpicurl = tiokeholder2.getvideo(play.getBigpicurl(), play.getTencent());
            member member = play.getMember();

            //收费视频 非VIP会员模糊显示video.getType()=1  收费 userInfo.getVip()=0 非VIP
            if (MyOpenhelper.getOpenhelper().Query(MyOpenhelper.videolist, Integer.parseInt(play.getId()), userInfo.getUserId(), 1).size() > 0) {
                Glide.with(context).load(bigpicurl).into(bgmplay);
            } else {
                if (play.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0 && !userInfo.getUserId().equals(play.getUserid())) {
                    Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(10, 25))).into(bgmplay);
                } else {
                    Glide.with(context).load(bigpicurl).into(bgmplay);
                }
            }

            if (member != null) {
                String picture = TextUtils.isEmpty(member.getPicture()) ? bigpicurl : member.getPicture();
                Glide.with(context).load(picture).into(icon);
            } else {
                Glide.with(context).load(bigpicurl).into(icon);
            }

        }

    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        if (object instanceof videolist) {
            videolist play = (videolist) object;
            String path = TextUtils.isEmpty(play.getPlaytest()) ? play.getPlayurl() : play.getPlaytest();
            path = tiokeholder2.getvideo(path, play.getTencent());
            video.setVideoPath(path);
            video.setTag(play);
            video.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
            title.setText(play.getTitle());
            title.setTag(position);
            tv1.setText(play.getAnum());
            tv2.setText(play.getPnum());
            tv3.setText(play.getFnum());

            //腾讯云
            String bigpicurl = tiokeholder2.getvideo(play.getBigpicurl(), play.getTencent());
            member member = play.getMember();

            //收费视频 非VIP会员模糊显示video.getType()=1  收费 userInfo.getVip()=0 非VIP
            if (MyOpenhelper.getOpenhelper().Query(MyOpenhelper.videolist, Integer.parseInt(play.getId()), userInfo.getUserId(), 1).size() > 0) {
                Glide.with(context).load(bigpicurl).into(bgmplay);
            } else {
                if (play.getType() == Constants.TENCENT && userInfo.getVip() == Constants.TENCENT0 && !userInfo.getUserId().equals(play.getUserid())) {
                    Glide.with(context).load(bigpicurl).apply(bitmapTransform(new BlurTransformation(10, 25))).into(bgmplay);
                } else {
                    Glide.with(context).load(bigpicurl).into(bgmplay);
                }
            }

            if (member != null) {
                String picture = TextUtils.isEmpty(member.getPicture()) ? bigpicurl : member.getPicture();
                Glide.with(context).load(picture).into(icon);
            } else {
                Glide.with(context).load(bigpicurl).into(icon);
            }

        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }


}

