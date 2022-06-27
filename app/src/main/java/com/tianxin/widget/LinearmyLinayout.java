package com.tianxin.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.R;

public class LinearmyLinayout extends FrameLayout {
    LinearLayout a1;
    ImageView icon;
    TextView title;
    TextView msg;

    public LinearmyLinayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LinearmyLinayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LinearmyLinayout(Context context) {
        super(context);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_bar_view, this);
        a1 = findViewById(R.id.a1);
        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        msg = findViewById(R.id.msg);

    }


    public void setIcon(String path) {
        Glide.with(getContext()).load(path).into(icon);
    }

    public void setIcon(int path) {
         Glide.with(getContext()).load(path).into(icon);
    }

    public void setIcon(Drawable path) {
        Glide.with(getContext()).load(path).into(icon);
    }

    public void setIcon(Bitmap path) {
        Glide.with(getContext()).load(path).into(icon);
    }

    public void setTitle(String msg) {
        this.title.setText(msg);
    }

    public void setMsg(String msg) {
        this.msg.setText(msg);
    }
}
