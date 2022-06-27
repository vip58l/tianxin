package com.tencent.Camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.liteav.SelectContactActivity;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.utils.Glideloads;

public class My_Camera extends FrameLayout {
    private ImageView img_avatar;
    private TextView tv_user_name;
    private TextView tv_video_tag;

    public My_Camera(@NonNull Context context) {
        super(context);
        init();
    }

    public My_Camera(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public My_Camera(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.activity_my_camera, this);
        img_avatar = findViewById(R.id.img_sponsor_avatar);
        tv_user_name = findViewById(R.id.tv_sponsor_user_name);
        tv_video_tag = findViewById(R.id.tv_sponsor_video_tag);
    }

    public void setname(String name) {
        tv_user_name.setText(name);
    }

    public void settag(String tag) {
        tv_video_tag.setText(tag);
    }

    public void setPath(String avatar) {
        GlideEngine.loadCornerImage(img_avatar, avatar, null, SelectContactActivity.RADIUS);
    }

    public void setPath(Bitmap avatar) {
        Glideloads.loadImage(img_avatar, avatar);
    }

    public void setPath(int avatar) {
        Glideloads.loadImage(img_avatar, avatar);
    }
}
