package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.R;

public class item_viewcomter extends LinearmyLinayout {

    private String mName;
    private String mContent;
    private boolean mIsJump;
    private boolean mIsSwitch;
    private boolean mIsBottom;
    private int src;


    private TextView mNameText;
    private TextView mContentText;
    private ImageView mNavArrowView;
    private ImageView mIconlogoView;

    public item_viewcomter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineController, 0, 0);
        try {
            mName = ta.getString(R.styleable.LineController_name);
            mContent = ta.getString(R.styleable.LineController_subject);
            mIsBottom = ta.getBoolean(R.styleable.LineController_isBottom, false);
            mIsJump = ta.getBoolean(R.styleable.LineController_canNav, false);
            mIsSwitch = ta.getBoolean(R.styleable.LineController_isSwitch, false);
            src = ta.getInteger(R.styleable.LineController_src, -1);
            inidate();
        } finally {
            ta.recycle();
        }

    }

    private void inidate() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_conter, this);
        mNameText = findViewById(R.id.name);
        mNameText.setText(mName);
        mContentText = findViewById(R.id.content);
        mContentText.setText(mContent);
        mIconlogoView = findViewById(R.id.iconlogo);
        mIconlogoView.setImageResource(src);

        mNavArrowView = findViewById(R.id.rightArrow);
        mNavArrowView.setVisibility(mIsJump ? VISIBLE : GONE);
    }

    public String getmNameText() {
        return mNameText.getText().toString();
    }

    public void setmNameText(String mName) {
        this.mName = mName;
        mNameText.setText(mName);

    }

    /**
     * 设置文字内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.mContent = content;
        mContentText.setText(content);
    }

    public String getmContentText() {
        return mContentText.getText().toString();
    }
}
