/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/15 0015
 */


package com.tianxin.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.Glideload;

public class item_personal_button extends FrameLayout {
    private ImageView icon;
    private TextView title;

    public item_personal_button(@NonNull Context context) {
        super(context);
        sinit();
    }

    public item_personal_button(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sinit();
    }

    public item_personal_button(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        sinit();
    }

    public item_personal_button(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        sinit();
    }

    private void sinit() {
        //LayoutInflater.from(getContext()).inflate(R.layout.item_personal_button, this);
        inflate(getContext(), R.layout.item_personal_button, this);
        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
    }

    public void setIcon(String url) {
        Glideload.loadImage(this.icon, url);
    }

    public void setIcon(int url) {
        Glideload.loadImage(this.icon, url);
    }

    public void setIcon(Drawable url) {
        Glideload.loadImage(this.icon, url);
    }

    public void setconter(String msg) {
        this.title.setText(msg);
    }
}
