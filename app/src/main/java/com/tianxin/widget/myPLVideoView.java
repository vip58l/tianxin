/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/20 0020
 */


package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;

import com.pili.pldroid.player.widget.PLVideoView;

public class myPLVideoView extends PLVideoView {
    public myPLVideoView(Context context) {
        super(context);
    }

    public myPLVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public myPLVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        setMeasuredDimension(width, height);
    }
}
