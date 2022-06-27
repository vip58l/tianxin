package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class Activity_item_conter extends FrameLayout {
    private LinearLayout lin1;
    private TextView mtitle;
    private TextView conmp1;
    private ImageView imgshow;

    public Activity_item_conter(@NonNull Context context) {
        super(context);
        init();
    }

    public Activity_item_conter(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Activity_item_conter(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.item_text_conter, this);
        mtitle = findViewById(R.id.mtitle);
        imgshow = findViewById(R.id.imgshow);
        conmp1 = findViewById(R.id.conmp1);
        lin1 = findViewById(R.id.lin1);
        imgshow.setVisibility(GONE);
    }

    public void setTitle(String msg) {
        this.mtitle.setText(msg);
    }

    public void setConmp1(String msg) {
        this.conmp1.setText(msg);
    }

    public void setImgshow() {
        imgshow.setVisibility(VISIBLE);
    }
}
