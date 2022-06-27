package com.tianxin.activity.picenter.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.R;

public class widgetactivity extends RelativeLayout {
    TextView title1, title2;
    Context mcontext;
    AttributeSet attrs;

    public widgetactivity(Context context) {
        this(context, null);

    }

    public widgetactivity(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public widgetactivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mcontext = context;
        this.attrs = attrs;
        inflate(getContext(), R.layout.widgetactivity, this);
        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.widgetactivity);
        String name = a.getString(R.styleable.widgetactivity_text_name);
        String title = a.getString(R.styleable.widgetactivity_text_title);
        title1.setText(name);
        title2.setText(title);
        a.recycle();
    }

    public void setText(int name) {
        title1.setText(String.valueOf(name));
    }

}
