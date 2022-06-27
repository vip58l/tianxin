package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.Citydate;

public class item_text_title extends BaseHolder {

    public item_text_title(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        Citydate citydate = (Citydate) object;
        TextView textView = (TextView) itemView;
        textView.setText(citydate.getTitle());
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
}
