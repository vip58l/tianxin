package com.tianxin.adapter.itemholder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.tupianzj;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class mvidewloder16 extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.tv_state_msg)
    TextView tv_state_msg;
    @BindView(R.id.iv_tv)
    TextView iv_tv;
    @BindView(R.id.topzd)
    TextView topzd;
    @BindView(R.id.circleimageview)
    ImageView circleimageview;
    @BindView(R.id.relayout)
    RelativeLayout relayout;
    @BindView(R.id.linearLayout15)
    LinearLayout linearLayout15;

    public mvidewloder16(Context context, ViewGroup parent, int viewType) {
        super(LayoutInflater.from(context).inflate(R.layout.item_list_img, parent, false));
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }

    /**
     * 个人主页视频列表
     *
     * @param svideo
     * @param member2
     * @param position
     * @param callback
     */
    public void bind(videolist svideo, member member2, int position, Callback callback) {
        String pathurl = svideo.getBigpicurl();
        try {
            pathurl = svideo.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(svideo.getBigpicurl()) : svideo.getBigpicurl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ciidr = TextUtils.isEmpty(member2.getPicture()) ? pathurl : member2.getPicture();
        ImageLoadHelper.glideShowCornerImageWithUrl(context, pathurl, image);
        Glideload.loadImage(circleimageview, ciidr);
        title.setText(svideo.getTitle());
        time.setText(svideo.getTime());
        name.setText(member2.getTruename());

        tv_state_msg.setVisibility(svideo.getStatus() == 0 ? GONE : VISIBLE);
        topzd.setVisibility(svideo.getTop() == 0 ? GONE : VISIBLE);
        linearLayout15.setVisibility(GONE);
        switch (svideo.getStatus()) {
            case 1:
                tv_state_msg.setText(R.string.tv_msg66);
                break;
            case 2:
                tv_state_msg.setText(R.string.tv_msg65);
                break;
        }
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
            itemView.setOnLongClickListener(v -> {
                callback.LongClickListener(position);
                return true;
            });
        }

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
        relayout.setLayoutParams(params);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        tupianzj tupianzj = (com.tencent.opensource.model.tupianzj) object;
        title.setText(tupianzj.getTitle());
        if (userInfo.getVip() == 0) {
            Glideload.loadImage(image, tupianzj.getPic(), 4, 4, 15);
            iv_tv.setVisibility(VISIBLE);
        } else {
            iv_tv.setVisibility(GONE);
            ImageLoadHelper.glideShowCornerImageWithUrl(context, tupianzj.getPic(), image);
        }

        circleimageview.setVisibility(GONE);
        time.setVisibility(GONE);
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
            itemView.setOnLongClickListener(v -> {
                callback.LongClickListener(position);
                return true;
            });
        }

        Rect rect = new Rect();
        rect.top = 10;
        rect.left = 10;
        rect.bottom = 10;
        rect.right = 10;

    }


    public void bind() {

    }

}
