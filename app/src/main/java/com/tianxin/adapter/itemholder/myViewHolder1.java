package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.Module.api.tasklist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;

public class myViewHolder1 extends BaseHolder {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.send)
    TextView send;

    public myViewHolder1(Context context) {
        super((LayoutInflater.from(context).inflate(R.layout.item_shere, null)));
    }

    public void bind(Object obj, int position, Paymnets paymnets) {
        tasklist tasklist = (com.tianxin.Module.api.tasklist) obj;
        send.setText(tasklist.getAction());
        tv_msg.setText(tasklist.getMsg());
        tv_title.setText(tasklist.getTitle());
        if (tasklist.getType() == References.a2 && tasklist.getQiandao() == Constants.TENCENT) {
            send.setEnabled(false);
            send.setText(R.string.tv_msg198);
            send.setTextColor(context.getResources().getColor(R.color.home));
            send.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_bottom_pink3));
        }
        if (tasklist.getType() == References.a4 && tasklist.getBind() == Constants.TENCENT) {
            send.setEnabled(false);
            send.setText(R.string.tv_msg204);
            send.setTextColor(context.getResources().getColor(R.color.home));
            send.setBackground(context.getResources().getDrawable(R.drawable.bg_radius_bottom_pink3));
        }

        if (paymnets != null) {
            send.setOnClickListener(v -> paymnets.status(position));
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
