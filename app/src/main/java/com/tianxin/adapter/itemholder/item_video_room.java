package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.opensource.svgaplayer.SVGAImageView;
import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Config;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;

public class item_video_room extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.nums)
    TextView nums;
    @BindView(R.id.svgaImage)
    SVGAImageView svgaImage;

    public item_video_room(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        videolist videolist = (com.tencent.opensource.model.videolist) object;
        title.setText(videolist.getTitle());
        nums.setText(String.format("在线 %s", Config.random()));
        Glideload.loadImage(image, videolist.getBigpicurl(), 6);
        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
