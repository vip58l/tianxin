package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BasitemBaseAdapter;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.MyOpenhelper;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.imglist;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class ysbaseadapter extends BasitemBaseAdapter {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.circleimageview)
    ImageView circleimageview;
    @BindView(R.id.bg_shade_bom)
    ImageView bg_shade_bom;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tv_state_msg)
    TextView tv_state_msg;
    @BindView(R.id.sendsvip)
    TextView sendsvip;  @BindView(R.id.topzd)
    TextView topzd;
    @BindView(R.id.relayout)
    RelativeLayout relayout;

    public static View view(Context context) {
        return View.inflate(context, R.layout.item_list_img, null);
    }

    public ysbaseadapter(View itemview) {
        super(itemview);
    }

    @Override
    public void bind(Object object, int position) {

    }

    @Override
    public void bind(Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, Paymnets paymnets) {

    }

    @Override
    public void bind(Context context, Object object, int position, int ii, Paymnets paymnets, AMapLocation aMapLocation) {

    }

    /**
     * 我的相册视频
     *
     * @param object
     */
    public void bind(Object object) {
        videolist svideo = (videolist) object;
        member member2 = svideo.getMember();
        String pathurl = tiokeholder2.getvideo(svideo.getBigpicurl(), svideo.getTencent());
        String ciidr = TextUtils.isEmpty(member2.getPicture()) ? pathurl : member2.getPicture();
        ImageLoadHelper.glideShowCornerImageWithUrl(context, pathurl, image);
        Glideload.loadImage(circleimageview, ciidr);
        name.setText(svideo.getType() == 0 ? context.getString(R.string.tv_msg25) : context.getString(R.string.tv_msg26));
        title.setText(svideo.getTitle());
        time.setText(svideo.getTime());
        tv_state_msg.setVisibility(svideo.getStatus() == 0 ? View.GONE : View.VISIBLE);
        topzd.setVisibility(svideo.getTop() == 0 ? View.GONE : View.VISIBLE);
        switch (svideo.getStatus()) {
            case 1:
                tv_state_msg.setText(R.string.tv_msg66);
                break;
            case 2:
                tv_state_msg.setText(R.string.tv_msg65);
                break;
        }
    }

    /**
     * 个人相册图片
     *
     * @param object
     */
    public void bind2(Object object) {
        imglist imglist = (imglist) object;
        String presignedURL = imglist.getPic();
        try {
            presignedURL = imglist.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(imglist.getPic()) : imglist.getPic();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageLoadHelper.glideShowCornerImageWithUrl(context, presignedURL, image);
        bg_shade_bom.setVisibility(View.GONE);
        title.setText(imglist.getTitle());
        title.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        circleimageview.setVisibility(View.GONE);
        tv_state_msg.setVisibility(imglist.getStatus() == 0 ? View.GONE : View.VISIBLE);
        switch (imglist.getStatus()) {
            case 1:
                tv_state_msg.setText(R.string.tv_msg66);
                break;
            case 2:
                tv_state_msg.setText(R.string.tv_msg65);
                break;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 400);
        relayout.setLayoutParams(params);
    }

    /**
     * 街拍视频列表
     *
     * @param object
     */
    public void bind3(Object object) {
        videolist videolist = (videolist) object;
        member me = videolist.getMember();
        String bigpicurl = tiokeholder2.getvideo(videolist.getBigpicurl(),videolist.getTencent());

        if (me == null) {
            circleimageview.setVisibility(View.GONE);
        } else {
            String Picture = TextUtils.isEmpty(me.getPicture()) ? bigpicurl : me.getPicture();
            Glideload.loadImage(circleimageview, Picture);
            name.setText(me.getTruename());
        }
        title.setText(videolist.getTitle());
        time.setText(videolist.getTime());
        if (MyOpenhelper.getOpenhelper().Query(MyOpenhelper.videolist, Integer.parseInt(videolist.getId()), UserInfo.getInstance().getUserId(), 1).size() > 0) {
            //隐藏VIP图标
            sendsvip.setVisibility(View.GONE);
        } else {
            sendsvip.setVisibility(videolist.getType() == Constants.TENCENT ? View.VISIBLE : View.GONE);
        }
        ImageLoadHelper.glideShowCornerImageWithUrl(context, bigpicurl, image);
    }

}
