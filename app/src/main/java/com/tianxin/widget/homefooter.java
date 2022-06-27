package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianxin.R;

public class homefooter extends LinearLayout {

    TextView homeMsg;
    ImageView image;

    public homefooter(Context context) {
        this(context, null);
    }

    public homefooter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public homefooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.homefooter);
        String msgtitle = a.getString(R.styleable.homefooter_home_name);
        Drawable d = a.getDrawable(R.styleable.homefooter_home_src);
        a.recycle();
        inflate(context, R.layout.home_footer, this);
        homeMsg = findViewById(R.id.hone_title);
        image = findViewById(R.id.img1);
        homeMsg.setText(msgtitle);
        image.setImageDrawable(d);
    }

    public void setImage(String msg, int drawable, int type) {
        homeMsg.setTextColor(getContext().getResources().getColor(type == 1 ? R.color.colorAccent : R.color.home));
        if (!TextUtils.isEmpty(msg)){
            homeMsg.setText(msg);
        }
        image.setImageResource(drawable);
    }

}
