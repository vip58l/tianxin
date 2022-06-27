package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Config;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.trend_comment;

import butterknife.BindView;

public class item_trend_show extends BaseHolder {
    @BindView(R.id.circleImageView)
    ImageView circleImageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.conter)
    TextView conter;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.tv_delete)
    TextView tv_delete;

    public item_trend_show(View inflate) {
        super(inflate);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        trend_comment trendComment = (trend_comment) object;
        member member = trendComment.getMember();
        if (member != null) {
            Glideload.loadImage(circleImageView, member.getPicture());
            name.setText(member.getTruename());
        } else {
            Glideload.loadImage(circleImageView, R.mipmap.rtc_user_portrait);
            name.setText("神秘人");
        }
        conter.setText(trendComment.getMcontent());
        datetime.setText(Config.timestamp(trendComment.getDatetime()));
        if (trendComment.getUserid() == Integer.parseInt(userInfo.getUserId())) {
            tv_delete.setVisibility(View.VISIBLE);
        } else {
            tv_delete.setVisibility(View.GONE);
        }
        if (callback != null) {
            tv_delete.setOnClickListener(v -> callback.OnClickListener(position));
        }

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
