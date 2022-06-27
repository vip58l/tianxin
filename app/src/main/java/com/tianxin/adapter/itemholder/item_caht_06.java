package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.dialog.adapter.GiftInfo;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.Socket.data;

import butterknife.BindView;

public class item_caht_06 extends BaseHolder {
    private static final String TAG = item_caht_06.class.getSimpleName();
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.playmp)
    RelativeLayout playmp;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.content)
    ImageView content;

    public item_caht_06(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_chat_06, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        data data = (data) object;
        GiftInfo giftInfo = new Gson().fromJson(data.getMessage(), GiftInfo.class);
        if (!TextUtils.isEmpty(data.getAvatar())) {
            Glideload.loadImage(icon, data.getAvatar());
        } else {
            Glideload.loadImage(icon, data.getSex() == 1 ? R.mipmap.boy_off : R.mipmap.girl_off);
        }
        title.setText(giftInfo.title);
        Glideload.loadImage(content, giftInfo.giftPicUrl);
        play.setVisibility(!TextUtils.isEmpty(data.getVideo()) ? View.VISIBLE : View.GONE);
        if (callback != null) {
            icon.setOnClickListener(v -> callback.OndeleteListener(position));
            playmp.setOnClickListener(v -> callback.OnClickListener(position));
            playmp.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callback.LongClickListener(position);
                    return false;
                }
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
