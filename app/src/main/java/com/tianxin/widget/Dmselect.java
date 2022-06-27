package com.tianxin.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

/**
 * 搜索会员列表
 */
public class Dmselect extends FrameLayout {
    private EditText editselect;
    private ImageView img_del;
    private Paymnets paymnets;

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public Dmselect(@NonNull Context context) {
        super(context);
        init();
    }

    public Dmselect(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Dmselect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Dmselect(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.qrselect, this);
        findViewById(R.id.close).setOnClickListener(this::onClick);
        editselect = findViewById(R.id.editselect);
        img_del = findViewById(R.id.img_del);
        img_del.setVisibility(GONE);
        img_del.setOnClickListener(this::onClick);
        editselect.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                img_del.setVisibility(s.length() > 0 ? VISIBLE : GONE);
            }
        });
        editselect.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    paymnets.search(getEditselect());
                    editselect.setText(null);
                    return true;
                }
                return false;
            }
        });
    }

    public String getEditselect() {
        return editselect.getText().toString().trim();
    }

    public void setEditselect(String msg) {
        this.editselect.setText(msg);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_del:
                editselect.setText(null);
                break;
            case R.id.close:
                Activity activity = (Activity) getContext();
                activity.finish();
                break;
        }

    }
}
