package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianxin.R;

public class fragment_page5 extends LinearLayout {

    public fragment_page5(Context context) {
        this(context, null);
    }

    public fragment_page5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public fragment_page5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.page5);
        String title_name = a.getString(R.styleable.page5_page5_name);
        String title_msg = a.getString(R.styleable.page5_page5_msg);
        final Drawable d = a.getDrawable(R.styleable.page5_page5_src);
        a.recycle();

        inflate(context, R.layout.fragment_item_page5, this);
        TextView tv1 = findViewById(R.id.tv1);
        ImageView icon = findViewById(R.id.icon);
        icon.setImageDrawable(d);
        tv1.setText(title_name);
    }

}
