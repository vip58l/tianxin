/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/3 0003
 */


package com.tianxin.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class Backtitle extends FrameLayout implements View.OnClickListener {
    public RelativeLayout layout0;
    public ImageView imgback;
    public TextView backleft, title, backright;
    private String title_name, title_msg,title_right;
    float dimension;

    public Backtitle(@NonNull Context context) {
        this(context, null);
    }

    public Backtitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Backtitle(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.backtitle);
        title_name = a.getString(R.styleable.backtitle_title_name);
        title_msg = a.getString(R.styleable.backtitle_title_msg);
        title_right = a.getString(R.styleable.backtitle_title_right);
        dimension = a.getDimension(R.styleable.backtitle_title_textSize, 0);
        a.recycle();
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_var_back, this);
        layout0 = findViewById(R.id.layout0);
        imgback = findViewById(R.id.img_back);
        backleft = findViewById(R.id.backleft);
        title = findViewById(R.id.title);
        backright = findViewById(R.id.backright);
        imgback.setOnClickListener(this);

        if (!TextUtils.isEmpty(title_name)){
            title.setText(title_name);
        }
        if (!TextUtils.isEmpty(title_right)){
            backright.setVisibility(VISIBLE);
            backright.setText(title_right);
        }

        if (dimension>0){
            title.setTextSize(dimension);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Context context = getContext();
                ((Activity) context).finish();
                break;
        }
    }

    public void setBackleft(String msg) {
        backleft.setText(msg);
        backleft.setVisibility(VISIBLE);
    }

    public void setconter(String msg) {
        this.title.setText(msg);
    }

    public void setBackright(String msg) {
        backright.setText(msg);
        backright.setVisibility(VISIBLE);
    }

    /**
     * 透明背景
     */
    public void setBackground() {
        layout0.setBackground(getContext().getDrawable(R.color.transparent));
    }

    /**
     * 透明背景
     */
    public void settransparent() {
        layout0.setBackground(getContext().getDrawable(R.color.transparent));
    }

    public void settitleBackground(int corlo) {
        this.title.setTextColor(corlo);
    }

    public void totitle() {
        setconter(getContext().getString(R.string.play_videotitle));
        setBackground();
        settitleBackground(getResources().getColor(R.color.white));
        imgback.setImageResource(R.mipmap.rtc_ic_back);
    }
}
