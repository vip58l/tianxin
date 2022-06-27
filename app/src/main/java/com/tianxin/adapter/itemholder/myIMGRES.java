package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;

import butterknife.BindView;

public class myIMGRES extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;

    public myIMGRES(Context content, ViewGroup parent, boolean b) {
        super(LayoutInflater.from(content).inflate(R.layout.item_img_pic,null));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        String pic = (String) object;
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 180);
        //image.setLayoutParams(params);
        DemoApplication.presignedURL(pic);
        ImageLoadHelper.glideShowCornerImageWithUrl(DemoApplication.instance(), pic, image);
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {

    }

    @Override
    public void OnClick(View v) {

    }
}
