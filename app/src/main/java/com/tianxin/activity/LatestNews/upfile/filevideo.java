package com.tianxin.activity.LatestNews.upfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.activity.LatestNews.activity_news;

public class filevideo extends FrameLayout implements View.OnClickListener {
    private activity_news news;
    ImageView play_bg, del_play, play_mp;
    RelativeLayout play;
    String path;
    private Bitmap bitmap;

    public filevideo(@NonNull Context context) {
        super(context);
        init();
    }

    public filevideo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public filevideo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.player, this);
        play_bg = findViewById(R.id.play_bg);
        del_play = findViewById(R.id.del_play);
        play_mp = findViewById(R.id.play_mp);
        play = findViewById(R.id.play);
        del_play.setOnClickListener(this::onClick);
        play_mp.setOnClickListener(this::onClick);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setpalwy(Bitmap bitmap) {
        setBitmap(bitmap);
        Glideload.loadImage(play_bg, bitmap, 6);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setpalwy(String path) {
        Glide.with(getContext()).load(path).into(play_bg);
    }

    public void show(activity_news news) {
        this.news = news;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.del_play:
                setPath("");
                setVisibility(GONE);
                news.loationimges.setVisibility(View.VISIBLE);
                break;
            case R.id.play_mp:
                news.tostartActivity("预览视频", path, "", 0);
                break;
        }


    }
}
