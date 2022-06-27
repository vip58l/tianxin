package com.tencent.Camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.RequestListener;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;

import static com.tencent.liteav.SelectContactActivity.RADIUS;

public class camera_item extends FrameLayout {
    private ImageView image;
    private TextView video_name;
    private TextView video_text;

    public camera_item(@NonNull Context context) {
        super(context);
        init();
    }

    public camera_item(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public camera_item(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public camera_item(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.activity_camera_to, this);
        image = findViewById(R.id.image);
        video_name = findViewById(R.id.video_name);
        video_text = findViewById(R.id.video_text);

    }

    public void setImage(String path) {
        GlideEngine.loadCornerImage(image, path, null, RADIUS*2);

    }

    public void setVideo_name(String msg) {
        this.video_name.setText(msg);
    }

    public void setVideo_text(String msg) {
        this.video_text.setText(msg);
    }
}
