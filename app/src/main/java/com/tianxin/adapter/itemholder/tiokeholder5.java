package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.activity.video2.widget.player;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class tiokeholder5 extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.video_view)
    VideoView video;


    public tiokeholder5(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.fragment_video2, parent, false));
    }

    public void bind(Object object) {
        if (object instanceof videolist) {
            videolist play = (videolist) object;
            String path = TextUtils.isEmpty(play.getPlaytest()) ? play.getPlayurl() : play.getPlaytest();
            String mymPath = player.mymPath(path);
            video.setVideoPath(mymPath);
            title.setText(play.getTitle());
            Glide.with(context).load(play.getBigpicurl()).into(image);
        }
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}

