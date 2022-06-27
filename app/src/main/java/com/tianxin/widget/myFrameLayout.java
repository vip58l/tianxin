package com.tianxin.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianxin.R;

/**
 * 自定义音乐转到播放按钮
 */
public class myFrameLayout extends FrameLayout {
    public ImageView circleimage;
    private Animation playmisc;
    private ImageView play;

    public myFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public myFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public myFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_mpstar, this);
        circleimage = findViewById(R.id.circleimageview);
        play = findViewById(R.id.play);
        //360动画转动走起
        playmisc = AnimationUtils.loadAnimation(getContext(), R.anim.playmisc);
    }

    public void seticon(String path) {
        if (TextUtils.isEmpty(path)) {
            Glide.with(this).load(R.mipmap.audio_effect_setting_changetype_metal).into(circleimage);
        } else {
            Glide.with(this).load(path).into(circleimage);
        }
    }

    public void seticon(int path) {
        Glide.with(this).load(path).into(circleimage);
    }

    /**
     * 开始转动播放
     */
    public void start() {
        circleimage.startAnimation(playmisc); //播放动画
        Glide.with(getContext()).load(R.mipmap.ic_action_pause).into(play);
    }

    /**
     * 转动停止
     */
    public void pause() {
        circleimage.clearAnimation();      //停止动画
        Glide.with(getContext()).load(R.mipmap.icon_play_vde).into(play);
    }
}
