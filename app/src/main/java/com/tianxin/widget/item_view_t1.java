package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class item_view_t1 extends FrameLayout {
    private TextView c1, c2, c3;

    public item_view_t1(@NonNull Context context) {
        super(context);
        init();
    }

    public item_view_t1(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public item_view_t1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        inflate(getContext(), R.layout.item_view_t1, this);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
    }

    public void follow(String c11) {
        c1.setText(c11);

    }

    public void fans(String c22) {
        c2.setText(c22);

    }

    public void give(String c33) {
        c3.setText(c33);

    }


}
