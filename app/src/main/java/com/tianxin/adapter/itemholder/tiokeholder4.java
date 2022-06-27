package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class tiokeholder4 extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.video_view)
    com.tianxin.activity.video2.widget.player video;

    public tiokeholder4(Context mContext, ViewGroup parent) {
        super(LayoutInflater.from(mContext).inflate(R.layout.fragment_video, parent, false));
    }

    public void bind(Object object) {
        if (object instanceof videolist) {
            videolist play = (videolist) object;

            //video.setPath(play.getPlayurl()); //有值会自动播放视频
            video.setTag(play.getPlayurl());    //保存在tag对像方便外部调用
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

