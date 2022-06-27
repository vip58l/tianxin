/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CusVideoView extends VideoView {
    public CusVideoView(Context context) {
        super(context);
    }

    public CusVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
