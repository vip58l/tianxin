package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.Citychat;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class item_cityshow extends BaseHolder {
    private static final String TAG = item_cityshow.class.getSimpleName();
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.lin1)
    LinearLayout lin1;

    public item_cityshow(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        Citychat citychat = (Citychat) object;
        title.setText(citychat.getTitle());
        List<String> result = Arrays.asList(citychat.getPicture().split(","));
        if (result.size() > 1) {
            for (String path : result) {
                myimage(path);
            }
        } else {
            Glideload.loadImage(image, citychat.getPicture(), 4);
        }

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

    public void myimage(String path) {
        ImageView newimage = new ImageView(DemoApplication.instance());
        //newimage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height = 1200; //高度
        layoutParams.setMargins(10, 5, 10, 20); //距离
        newimage.setLayoutParams(layoutParams);
        Glideload.loadImage(newimage, path, 4);
        lin1.addView(newimage);
    }
}
