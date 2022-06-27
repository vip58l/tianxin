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
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.Socket.data;

import butterknife.BindView;

public class item_caht extends BaseHolder {
    private static final String TAG = item_caht.class.getSimpleName();
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;

    public item_caht(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_chat_list, parent, false));
    }

    public item_caht(Context content, ViewGroup parent, int type) {
        super(LayoutInflater.from(content).inflate(R.layout.item_chat_02, parent, false));
    }

    public item_caht(Context content, ViewGroup parent, int type, boolean b) {
        super(LayoutInflater.from(content).inflate(R.layout.item_chat_03, parent, false));
    }

    public item_caht(Context content, ViewGroup parent, String type) {
        super(LayoutInflater.from(content).inflate(R.layout.item_chat_04, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        data data = (data) object;
        if (!TextUtils.isEmpty(data.getAvatar())) {
            Glideload.loadImage(icon, data.getAvatar());
        } else {
            Glideload.loadImage(icon, data.getSex() == 1 ? R.mipmap.boy_off : R.mipmap.girl_off);
        }
        title.setText(data.getName());
        content.setText(data.getMessage());
        if (callback != null) {
            icon.setOnClickListener(v -> callback.OndeleteListener(position));
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
            itemView.setOnLongClickListener(v -> {
                callback.LongClickListener(position);
                return false;
            });
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }


}
