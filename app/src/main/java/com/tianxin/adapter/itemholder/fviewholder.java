package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.followlist;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.partyname;

import butterknife.BindView;

public class fviewholder extends BaseHolder {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_map)
    TextView tv_map;
    @BindView(R.id.tv_gx)
    TextView tv_gx;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv_vip)
    TextView tv_vip;
    @BindView(R.id.lay_money)
    LinearLayout lay_money;
    @BindView(R.id.tv_money)
    TextView tv_money;

    public fviewholder(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.followlist_item, null));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        partyname partyname = (com.tencent.opensource.model.partyname) object;
        member member = partyname.getMember();
        Glideload.loadImage(icon, member.getPicture());
        tv_title.setText(member.getTruename());
        tv_map.setText(member.getTruename());
        tv_money.setText(member.getTruename());
        lay_money.setVisibility(View.GONE);
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }
    }

    public void bind(Object object, int position, Callback callback, boolean radapter) {
        followlist followlist = (com.tencent.opensource.model.followlist) object;
        member member = followlist.getMember();
        lay_money.setVisibility(View.GONE);
        if (userInfo.getVip() == 1) {
            tvshow1(member);
        } else {
            tvshow2(member);
        }

        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }

    public void tvshow1(member member) {
        if (member != null) {
            if (!TextUtils.isEmpty(member.getPicture())) {
                Glideload.loadImage(icon, member.getPicture());
            } else {
                Glideload.loadImage(icon, member.getSex() == 1 ? R.mipmap.boy_on : R.mipmap.girl_on);
            }
            tv_title.setText(member.getTruename());
            if (!TextUtils.isEmpty(member.getProvince())) {
                tv_map.setText(member.getProvince() + "." + member.getCity());
            } else {
                tv_map.setText(member.getSex() == 1 ? "男性" : "女生");
            }
        }

    }

    /**
     * 模糊处理
     *
     * @param member
     */
    public void tvshow2(member member) {
        if (member != null) {
            if (!TextUtils.isEmpty(member.getPicture())) {
                Glideload.loadImage(icon, member.getPicture(), 5, 5);
            }
            tv_title.setText(member.getTruename());
            if (!TextUtils.isEmpty(member.getProvince())) {
                tv_map.setText(member.getProvince() + "." + member.getCity());
            } else {
                tv_map.setText(member.getSex() == 1 ? "男性" : "女生");
            }
            tvtitle(tv_title);
            tvtitle(tv_map);
        }

    }

    /**
     * Textview字体模糊
     *
     * @param textView
     */
    public void tvtitle(TextView textView) {
        if (Build.VERSION.SDK_INT >= 11) {
            textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        float radius = textView.getTextSize() / 3;
        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
        textView.getPaint().setMaskFilter(filter);
    }


}
