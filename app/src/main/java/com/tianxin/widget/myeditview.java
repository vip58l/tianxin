/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/2 0002
 */


package com.tianxin.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class myeditview extends FrameLayout implements View.OnClickListener {

    public EditText nickname;
    public ImageView myclose;

    public myeditview(@NonNull Context context) {
        super(context);
        init();
    }

    public myeditview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public myeditview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public myeditview(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_edit_mickname, this);
        nickname = findViewById(R.id.nickname);
        myclose = findViewById(R.id.myclose);
        myclose.setOnClickListener(this);
        myclose.setVisibility(GONE);
        nickname.setTextSize(18);
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                myclose.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }
        });

    }

    @Override
    public void onClick(View v) {
        nickname.setText(null);
    }

    public String getNickname() {
        return nickname.getText().toString().trim();
    }

    public void setNickname(String msg) {
        this.nickname.setText(msg);
    }
}
