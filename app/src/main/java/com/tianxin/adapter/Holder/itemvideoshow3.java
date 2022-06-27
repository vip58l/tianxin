package com.tianxin.adapter.Holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;

import butterknife.BindView;

public class itemvideoshow3 extends BaseHolder {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.myoney)
    TextView myoney;
    @BindView(R.id.msg)
    TextView msg;

    public itemvideoshow3(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.item_video_show3, null));
    }


    public void bind(Object object, int position, Callback callback, AMapLocation amapLocation) {
        this.callback = callback;
        this.samapLocation = amapLocation;
        member member = (member) object;
        personal personal = member.getPersonal();
        if (member == null) {
            return;
        }
        if (!TextUtils.isEmpty(member.getPicture())) {
            image.setBackground(null);
            ImageLoadHelper.glideShowCornerImageWithUrl(context, member.getPicture(), image, 8);
        } else {
            int head2 = member.getSex() == 2 ? R.mipmap.icon_cert_head2 : R.mipmap.avapic;
            ImageLoadHelper.glideShowCornerImageWithUrl(context, head2, image, 8);
        }
        name.setText(member.getTruename());
        if (member.getVip() == 1) {
            name.setTextColor(context.getResources().getColor(R.color.colorAccent));
        } else {
            name.setTextColor(context.getResources().getColor(R.color.homeback));
        }
        tips.setText(String.format("Lv%s", member.getLevel()));
        msg.setVisibility(View.GONE);
        if (member.getSex() == 1) {
            myoney.setText("保密");
        }
        if (personal != null) {
            myoney.setText(String.format(context.getString(R.string.item_msg_show) + "", personal.getAge(), personal.getHeight()));
            address.setText(TextUtils.isEmpty(personal.getProvince()) ? "保密" : String.format("%s", personal.getDistrict()));
        }

        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }

    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
