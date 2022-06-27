package com.tianxin.adapter.Holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.imglist;

import butterknife.BindView;

/**
 * 相册殂表
 */
public class listimageshow extends BaseHolder {
    private static final String TAG = listimageshow.class.getSimpleName();
    @BindView(R.id.image)
    ImageView image;

    public listimageshow(View inflate) {
        super(inflate);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        this.callback = callback;
        imglist imglist = (com.tencent.opensource.model.imglist) object;
        String path = imglist.getPic();
        if (imglist.getTencent() == 1) {
            path = DemoApplication.presignedURL(imglist.getPic());
        }
        Glideload.loadImage(image, path, 4);
        if (callback != null) {
            image.setOnClickListener(v -> callback.OnClickListener(position));
            image.setOnLongClickListener(v -> {
                callback.LongClickListener(position);
                return true;
            });
        }

    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}

