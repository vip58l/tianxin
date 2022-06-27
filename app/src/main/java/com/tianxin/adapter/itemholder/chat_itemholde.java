package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.chatmoney;

import butterknife.BindView;

public class chat_itemholde extends BaseHolder {
    @BindView(R.id.lv)
    TextView lv;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.t3)
    TextView t3;
    @BindView(R.id.t4)
    TextView t4;

    public chat_itemholde(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.chat_item_sttings_item, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        chatmoney schatmony = (chatmoney) object;
        lv.setText(String.format(context.getString(R.string.chat_aa12), "" + schatmony.getLevel()));
        t1.setText(schatmony.getVideo());
        t2.setText(schatmony.getAudio());
        t3.setText(schatmony.getMsg());
        t4.setText(schatmony.getContact());
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
