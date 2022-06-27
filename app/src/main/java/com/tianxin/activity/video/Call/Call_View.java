package com.tianxin.activity.video.Call;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.listener.Paymnets;

public class Call_View extends FrameLayout {
    private ImageView icon, bgmp;
    private TextView tag_name;
    private TextView time, videos1, videos2;
    private Paymnets paymnes;

    public Call_View(@NonNull Context context) {
        super(context);
        init();
    }

    public Call_View(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Call_View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Call_View(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setPaymnes(Paymnets paymnes) {
        this.paymnes = paymnes;
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.video_item_tr, this);
        bgmp = findViewById(R.id.bgmp);
        icon = findViewById(R.id.icon);
        findViewById(R.id.off).setOnClickListener(v -> onFail());
        findViewById(R.id.connect).setOnClickListener(v -> onpen());
        tag_name = findViewById(R.id.tag_name);
        time = findViewById(R.id.time);
        videos1 = findViewById(R.id.videos1);
        videos2 = findViewById(R.id.videos2);


    }

    private void onpen() {
        if (paymnes != null) {
            paymnes.onSuccess();
        }
    }

    private void onFail() {
        if (paymnes != null) {
            paymnes.onFail();
        }
    }

    public void setTime(String time) {
        this.time.setText(time);
    }

    public void setTag_name(String msg) {
        this.tag_name.setText(msg);
    }

    public void setIcon(int dricon) {
        this.icon.setImageResource(dricon);
    }

    public void setIcon(Drawable dricon) {
        this.icon.setImageDrawable(dricon);

    }

    public void setIcon(Bitmap bitmap) {
        this.icon.setImageBitmap(bitmap);
    }

    public void setIcon(String url) {
        Glideload.loadImage(icon, url);
    }

    public void setBgmp(String url) {
        Glideload.loadImage(bgmp, url, 10, 25);
    }

    public void shwovideos1() {
        videos1.setText(R.string.tvmsg266);
        videos2.setText(R.string.tv_msg267);


    }
}