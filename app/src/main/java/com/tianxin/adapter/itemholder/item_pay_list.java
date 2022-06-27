package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.opensource.model.Socket.data;
import com.tencent.opensource.model.byprice;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.listener.Callback;

import butterknife.BindView;

public class item_pay_list extends BaseHolder {
    private static final String TAG = item_pay_list.class.getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.radiobutton)
    RadioButton radiobutton;

    public item_pay_list(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_play_list, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        byprice byprice = (com.tencent.opensource.model.byprice) object;
        title.setText(byprice.getName());
        Glideload.loadImage(image, byprice.getIconurl());
        if (byprice.isCheckbox()) {
            radiobutton.setChecked(true);
            radiobutton.setVisibility(View.VISIBLE);
        } else {
            radiobutton.setChecked(false);
            radiobutton.setVisibility(View.GONE);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byprice.setCheckbox(isChecked);
                callback.OnClickListener(position);

            }
        });

        //点击事件item
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
