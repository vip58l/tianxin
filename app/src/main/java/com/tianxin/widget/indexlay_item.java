/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianxin.R;

public class indexlay_item extends FrameLayout {
    private ImageView icon;
    private TextView stitle;

    public indexlay_item(@NonNull Context context) {
        this(context, null);

    }

    public indexlay_item(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public indexlay_item(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.iniview(context, attrs);
    }

    private void iniview(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.indexlay_item);
        String name = a.getString(R.styleable.indexlay_item_indexlay_name);
        Drawable drawable = a.getDrawable(R.styleable.indexlay_item_indexlay_src);
        a.recycle();

        LayoutInflater.from(context).inflate(R.layout.indexlay_item, this);
        icon = findViewById(R.id.icon);
        stitle = findViewById(R.id.stitle);
        if (!TextUtils.isEmpty(name)) {
            stitle.setText(name);
        }
        if (drawable != null) {
            icon.setImageDrawable(drawable);
        }
    }

    public void setIcon(int ids) {
        Glide.with(getContext()).load(ids).into(this.icon);
    }

    public void setIcon(String path) {
        Glide.with(getContext()).load(path).into(this.icon);
    }

    public void setIcon(Bitmap ids) {
        Glide.with(getContext()).load(ids).into(this.icon);
    }

    public void setIcon(Drawable ids) {
        Glide.with(getContext()).load(ids).into(this.icon);
    }

    public void setcontertitle(String msg) {
        this.stitle.setText(msg);
    }

    public void setcontertitle(int msg) {
        this.stitle.setText(String.valueOf(msg));
    }

    public String getStitle() {
        return stitle.getText().toString().trim();
    }
}
