package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class view_item_conmp extends FrameLayout {
    private TextView pertitle, agetitle;

    public view_item_conmp(@NonNull Context context) {
        super(context);
        init();
    }

    public view_item_conmp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public view_item_conmp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_item_conmp, this);
        pertitle = findViewById(R.id.pertitle);
        agetitle = findViewById(R.id.agetitle);
    }

    public void setPertitle(String title, String msg) {
        pertitle.setText(title);
        agetitle.setText(msg);
    }
}
