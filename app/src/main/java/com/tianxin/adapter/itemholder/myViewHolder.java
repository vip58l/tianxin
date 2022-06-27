package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.member;

import butterknife.BindView;

public class myViewHolder extends BaseHolder {

    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;

    public myViewHolder(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_greet, parent, false));
    }

    public void bind(Object itemobject, int position, Paymnets paymnets) {
        member member = (member) itemobject;
        if (member == null) {
            return;
        }
        String p1 = member.getPicture();
        try {
            p1 = member.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(member.getPicture())) {
            int icons = member.getSex() == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose;
            Glideload.loadImage(icon, icons);
        } else {
            Glideload.loadImage(icon, p1);
        }
        name.setText(member.getTruename());
        if (!TextUtils.isEmpty(member.getCity())) {
            address.setText(member.getCity().replace("市", ""));
        }
        if (paymnets != null) {
            itemView.setOnClickListener(v -> paymnets.status(position));
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
