package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Callback;
import com.tianxin.widget.FlowLayout;
import com.tencent.opensource.model.Party;
import com.tencent.opensource.model.UserInfo;

import butterknife.BindView;

public class partyholder extends BaseHolder {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.describe)
    TextView describe;
    @BindView(R.id.datetime)
    TextView datetime;
    @BindView(R.id.flowLayout)
    FlowLayout flowLayout;
    @BindView(R.id.code)
    TextView code;
    @BindView(R.id.image)
    ImageView image;

    public partyholder(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.party_item, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
        this.position = position;
        this.context = context;
        this.object = object;
        this.callback = callback;

        Party party = (Party) object;
        ImageLoadHelper.glideShowCornerImageWithUrl(context, party.getCover(), image);
        title.setText(String.format("%s(限%s人)", party.getTitle(), party.getPartynumbe()));
        describe.setText(party.getAddress());
        datetime.setText(party.getPartytime());
        if (UserInfo.getInstance().getUserId().equals(String.valueOf(party.getUserid()))) {
            code.setText(party.getCode() == 0 ? context.getString(R.string.ta116) : context.getString(R.string.ta17));
        } else {
            code.setText(party.getCode() == 0 ? context.getString(R.string.ta16) : context.getString(R.string.ta17));
        }
        if (callback != null) {
            itemView.setOnClickListener(this::OnClick);
        }
        flowLayout.removeAllViews();
        if (!TextUtils.isEmpty(party.getTdesc())) {
            String[] split = party.getTdesc().split(",");
            if (split.length > 0) {

                int ssssi = split.length >= 4 ? 4 : split.length;
                for (int i = 0; i < ssssi; i++) {
                    TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_flowlayout, flowLayout, false);
                    tv.setText(split[i]);
                    tv.setTextColor(Color.WHITE);
                    flowLayout.addView(tv);
                }

            } else {
                TextView tv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_flowlayout, flowLayout, false);
                tv.setText(party.getTdesc());
                tv.setTextColor(Color.WHITE);
                flowLayout.addView(tv);
            }

        }
    }

    @Override
    public void OnClick(View v) {
        callback.OnClickListener(position);
    }

}
