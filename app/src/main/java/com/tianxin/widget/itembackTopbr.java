package com.tianxin.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.Glideload;


/**
 * 自定义头部引用
 */
public class itembackTopbr extends FrameLayout implements View.OnClickListener {
    public TextView tvback;
    public TextView contertext;
    public TextView righttext;
    public TextView sendright;
    public ImageView sendbtn;
    public ImageView iv_back_img;
    private LinearLayout layoutback;
    private RelativeLayout haidtop;
    private Class<?> cls;
    private Activity activity;
    private Context context;

    public void setHaidtopBackgroundColor(int BackgroundColor) {
        this.haidtop.setBackgroundColor(BackgroundColor);
    }

    public void setHaidtopBackgroundColor() {
        this.haidtop.setBackgroundColor(getContext().getResources().getColor(R.color.home3));
    }

    /**
     * 透明背景
     */
    public void setTRANSPARENT() {
        haidtop.setBackgroundColor(Color.TRANSPARENT);
        settvsetTextColor(Color.WHITE);
        setIv_back_img(R.mipmap.back);
        this.settitle("");

    }

    public void setHaidtopBackgroundColor(boolean b) {
        if (b) {
            //透明隐藏
            this.haidtop.getBackground().mutate().setAlpha(1);

        } else {
            this.haidtop.setBackgroundColor(getContext().getResources().getColor(R.color.home3));
        }
    }

    public void settvsetTextColor(int Color) {
        tvback.setTextColor(Color);
        contertext.setTextColor(Color);
    }

    public itembackTopbr(Context context) {
        this(context, null);

    }

    public itembackTopbr(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public itembackTopbr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.itembackTopbr);
        String title_name = a.getString(R.styleable.itembackTopbr_to_name);
        String title_msg = a.getString(R.styleable.itembackTopbr_to_msg);
        a.recycle();

        inflate(context, R.layout.item_back, this);
        iv_back_img = findViewById(R.id.iv_back_img);
        tvback = findViewById(R.id.tvback);
        haidtop = findViewById(R.id.haidtop);
        layoutback = findViewById(R.id.layoutback);
        contertext = findViewById(R.id.tv2title);
        righttext = findViewById(R.id.tv3title);
        sendbtn = findViewById(R.id.sendbtn);
        sendright = findViewById(R.id.sendright);
        activity = (Activity) getContext();
        this.context = context;
        tvback.setOnClickListener(this);
        iv_back_img.setOnClickListener(this);
        contertext.setText(title_name);
        righttext.setText(title_msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvback:
            case R.id.iv_back_img:
                activity.finish();
                break;
            case R.id.tv3title:
                context.startActivity(new Intent(context, cls));
                break;
        }
    }

    public void settitle(String tv1, String tv2) {
        contertext.setText(tv1);
        righttext.setText(tv2);

    }

    public void settitle(String tv1) {
        contertext.setText(tv1);

    }

    public void setLayoutback(boolean b) {
        if (b) {
            layoutback.setVisibility(GONE);
        }
    }

    public void setsendright(String msg) {
        sendright.setText(msg);
        sendright.setVisibility(TextUtils.isEmpty(msg) ? View.GONE : VISIBLE);
    }

    public void sendrightbg() {
        sendright.setBackground(getContext().getDrawable(R.drawable.bg_radius_bottom_pink2));
    }


    public void setIv_back_img(String path) {
        Glideload.loadImage(iv_back_img, path);
    }

    public void setIv_back_img(int path) {
        Glideload.loadImage(iv_back_img, path);
    }

    public void setIv_back_img(Drawable path) {
        Glideload.loadImage(iv_back_img, path);
    }

    public void setIv_back_img(Bitmap path) {
        Glideload.loadImage(iv_back_img, path);
    }

    public void setSendbtnshow(boolean b) {
        this.sendbtn.setVisibility(b ? VISIBLE : GONE);
    }

    public void setSendbtn(int path) {
        Glideload.loadImage(sendbtn, path);
    }

    public void setSendbtn(Drawable path) {
        Glideload.loadImage(sendbtn, path);
    }

    public void setSendbtn(Bitmap path) {
        Glideload.loadImage(sendbtn, path);
    }

    public void setSendbtn(String path) {
        Glideload.loadImage(sendbtn, path);
    }

    public void setrighttext(String msg) {
        righttext.setText(msg);
        righttext.setVisibility(TextUtils.isEmpty(msg) ? View.GONE : VISIBLE);
    }

    public void setOnClickRight(Class<?> cls) {
        this.cls = cls;
        righttext.setOnClickListener(this::onClick);
    }
}
