/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/31 0031
 */


package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class MyGridview extends GridView {
    public MyGridview(Context context) {
        super(context);
    }

    public MyGridview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyGridview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
    }
}
