package com.tianxin.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.ColorBg;

public class item_view_text extends FrameLayout {
    private TextView name, mssge;
    private RelativeLayout relayout1;

    public item_view_text(@NonNull Context context) {
        super(context);
        init();
    }

    public item_view_text(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public item_view_text(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public item_view_text(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.item_view_text, this);
        name = findViewById(R.id.name);
        mssge = findViewById(R.id.mssge);
        relayout1 = findViewById(R.id.relayout1);
    }

    public void setName(String n) {
        name.setText(n);
    }

    public void setMssge(String m) {
        mssge.setText(m);
        this.relayout1.setBackgroundColor(Color.parseColor(ColorBg.sColor()));
    }

    public void setName(String a, String m) {
        this.name.setText(a);
        this.mssge.setText(m);
        this.relayout1.setBackgroundColor(Color.parseColor(ColorBg.sColor()));

    }

    public void setName(String a, String m, int bg) {
        this.name.setText(a);
        this.mssge.setText(m);
        this.relayout1.setBackgroundColor(Color.parseColor(ColorBg.sColor()));

    }
}
