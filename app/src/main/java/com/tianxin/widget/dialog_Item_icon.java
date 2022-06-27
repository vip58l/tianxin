package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class dialog_Item_icon extends LinearLayout {
    private TextView tv1;
    private ImageView image;

    public dialog_Item_icon(@NonNull Context context) {
        this(context, null);
    }

    public dialog_Item_icon(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public dialog_Item_icon(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dialog_Item_icon);
        String title_name = a.getString(R.styleable.dialog_Item_icon_t_name);
        Drawable drawable = a.getDrawable(R.styleable.dialog_Item_icon_t_src);

        inflate(context, R.layout.dialog_item_icon, this);
        tv1 = findViewById(R.id.tv1);
        image = findViewById(R.id.image);

        tv1.setText(title_name);
        image.setImageDrawable(drawable);
        a.recycle();

    }

}
