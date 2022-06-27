package com.tianxin.adapter.Holder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;

import butterknife.BindView;

public class itemvideoshow1 extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.toplayout)
    LinearLayout toplayout;
    @BindView(R.id.mygroup)
    Group mygroup;
    @BindView(R.id.v3)
    ImageView v3;

    public itemvideoshow1(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.item_video_show1, null));
    }

    public void bind(Object object, int position, Callback callback, AMapLocation amapLocation) {
        this.callback = callback;
        this.samapLocation = amapLocation;
        member member = (member) object;
        personal personal = member.getPersonal();
        mygroup.setVisibility(View.GONE);
        toplayout.setVisibility(member.getOnline() == 0 ? View.GONE : View.VISIBLE);
        v3.setVisibility(member.getSex() == 1 && member.getVip() == 1 ? View.VISIBLE : View.GONE);
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
        if (personal != null) {
            age.setText(String.format("%s岁", personal.getAge()));
            address.setText(TextUtils.isEmpty(personal.getProvince()) ? context.getString(R.string.tm85) : String.format("%s.%s", personal.getProvince(), personal.getCity()));
        }
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }

        if (samapLocation != null) {
            if (personal != null) {
                if (!TextUtils.isEmpty(personal.getJwd())) {
                    String jwd = lbsamap.scalculateLineDistance(samapLocation, personal.getJwd());
                    setjwd(member, jwd);
                } else {
                    String jwd = lbsamap.scalculateLineDistance(samapLocation, member.getJwd());
                    setjwd(member, jwd);
                }
            } else {
                if (!TextUtils.isEmpty(member.getJwd())) {
                    String jwd = lbsamap.scalculateLineDistance(samapLocation, member.getJwd());
                    setjwd(member, jwd);
                }
            }

        }
    }

    private void setjwd(member member, String jwd) {
        if (member.getSex() == 2) {
            distance.setText("位置:" + context.getString(R.string.tm85));
        } else {
            distance.setText(TextUtils.isEmpty(jwd) ? context.getString(R.string.tm85) + "" : jwd);
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
