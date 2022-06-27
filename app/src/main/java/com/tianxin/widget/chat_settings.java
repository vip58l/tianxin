package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianxin.R;

public class chat_settings extends LinearLayout {
    TextView title;
    TextView money;

    public chat_settings(Context context) {
        this(context, null);
    }

    public chat_settings(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public chat_settings(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.chat_settings);
        String title_name = a.getString(R.styleable.chat_settings_chat_name);
        String title_msg = a.getString(R.styleable.chat_settings_chat_msg);
        a.recycle();

        inflate(context, R.layout.chat_item_sttings, this);
        title = findViewById(R.id.title);
        money = findViewById(R.id.money);
        title.setText(title_name);
        money.setText(title_msg);

    }

    public void setMoney(String parne){
        money.setText(parne);
    }
}
