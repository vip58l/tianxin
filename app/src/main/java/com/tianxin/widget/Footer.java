package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.R;

/**
 * 底部导航图标
 */
public class Footer extends RelativeLayout {
    ImageView image;
    TextView videocall;

    public Footer(Context context) {
        this(context, null);

    }

    public Footer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Footer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.foobter, this);
        image = findViewById(R.id.image);
        videocall = findViewById(R.id.videocall);
    }

    public void setImagetext(int type, String title) {
        videocall.setText(title);
        switch (type) {
            case 2:
                image.setImageResource(R.mipmap.c63);
                break;
            case 3:
                image.setImageResource(R.mipmap.rtc_camera_on);
                break;
            case 4:
                image.setImageResource(R.mipmap.item_txt_pic_icon);
                break;
            default:
            case 1:
                image.setImageResource(R.mipmap.a8z);
                break;
        }


    }

}
