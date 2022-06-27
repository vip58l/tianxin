package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.tencent.opensource.model.item;
import com.tianxin.R;
import com.tianxin.getlist.HotRooms;

public class zbVideo extends FrameLayout {
    public FrameLayout fragment;
    public ImageView zbbg;
    public ViewPager viewpager;

    public zbVideo(@NonNull Context context) {
        super(context);
        init();
    }

    public zbVideo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public zbVideo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public zbVideo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.activity_movie_zbativity, this);
        fragment = findViewById(R.id.fragment);
        zbbg = findViewById(R.id.zbbg);
        viewpager = findViewById(R.id.viewpager);
    }

    public void setinit(item item) {
        HotRooms hotRooms = (HotRooms) item.object;
        Glide.with(getContext()).load(hotRooms.bigImageOriginal).override(320, 640).into(zbbg);
    }
}
