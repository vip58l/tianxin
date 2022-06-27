package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianxin.R;

public class item_dianzhang extends FrameLayout {
    private TextView untion;
    private ImageView icons;

    public item_dianzhang(@NonNull Context context) {
        super(context);
        init();
    }

    public item_dianzhang(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public item_dianzhang(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        inflate(getContext(), R.layout.item_dianzhang, this);
        icons = findViewById(R.id.icons);
        untion = findViewById(R.id.untion);
    }

    public void setuntion(int nun) {
        untion.setText(String.valueOf(nun));
    }

    public void seticons(String path) {
        Glide.with(getContext()).load(path).into(icons);

    }

    public void seticons(int path) {
        Glide.with(getContext()).load(path).into(icons);


    }

    public void setAlphas(float f) {
        this.icons = icons;
        icons.setAlpha(f);
    }
}
