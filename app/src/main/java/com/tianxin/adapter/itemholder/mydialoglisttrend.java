package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.trend_comment;

import butterknife.BindView;

public class mydialoglisttrend extends BaseHolder {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;


    public mydialoglisttrend(Context content, ViewGroup parent, boolean b) {
        super(LayoutInflater.from(content).inflate(R.layout.item_msegs3, null));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        trend_comment trendComment = (trend_comment) object;
        member member = trendComment.getMember();
        if (member != null) {
            Glideload.loadImage(icon, member.getPicture(), 6);
            tv1.setText(member.getTruename());
            tv2.setText(trendComment.getMcontent());
            tv3.setVisibility(UserInfo.getInstance().getUserId().equals(String.valueOf(trendComment.getUserid())) ? View.VISIBLE : View.GONE);
            tv3.setOnClickListener(v -> callback.OndeleteListener(position));
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
