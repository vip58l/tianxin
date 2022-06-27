package com.tianxin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class party_weide extends FrameLayout {

    public TextView mtitle, mtag;

    public party_weide(@NonNull Context context) {
        this(context, null);
    }

    public party_weide(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public party_weide(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.party_weide);
        String title_name = a.getString(R.styleable.party_weide_patry_name);
        String title_msg = a.getString(R.styleable.party_weide_patry_tag);
        int visibility = a.getInteger(R.styleable.party_weide_patry_visibility, 0);
        a.recycle();

        inflate(context, R.layout.activity_party_weide, this);
        mtitle = findViewById(R.id.title);
        mtag = findViewById(R.id.tag);
        ImageView backright = findViewById(R.id.backright);
        switch (visibility) {
            case 0:
                backright.setVisibility(VISIBLE);
                break;
            case 1:
                backright.setVisibility(INVISIBLE);
                break;
            case 2:
                backright.setVisibility(GONE);
                break;
        }
        mtitle.setText(title_name);
        mtag.setText(title_msg);
    }

    public String getMtitle() {
        return mtitle.getText().toString().trim();
    }

    public void setMtitle(String mtitle) {
        this.mtitle.setText(mtitle);
    }

    public void setMtag(String mtag) {
        this.mtag.setText(mtag);
    }

    public String getMtag() {
        return mtag.getText().toString().trim();
    }
}
