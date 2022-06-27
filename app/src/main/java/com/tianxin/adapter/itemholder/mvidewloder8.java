package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Gametitle;
import com.tencent.opensource.model.navigation;

import butterknife.BindView;

public class mvidewloder8 extends BaseHolder {

    @BindView(R.id.stitle)
    TextView stitle;
    @BindView(R.id.icon)
    ImageView icon;

    public mvidewloder8(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        this.callback = callback;
        Gametitle gametitle = (Gametitle) object;
        stitle.setText(gametitle.getName());
        Glideload.loadImage(icon, gametitle.getPic());
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    public void bind(Object itemobject,int position, Paymnets paymnets) {
        navigation navigation = (navigation) itemobject;
        if (!TextUtils.isEmpty(navigation.getPic())) {
            Glideload.loadImage(icon, navigation.getPic());
        }
        if (!TextUtils.isEmpty(navigation.getTitle())) {
            stitle.setText(navigation.getTitle());
        }
        if (paymnets != null) {
            itemView.setOnClickListener(v -> paymnets.status(position));
        }
    }

    @Override
    public void OnClick(View v) {

    }
}
