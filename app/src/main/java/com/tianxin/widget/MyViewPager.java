/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/22 0022
 */


package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager {
    private float startX;
    private float startY;
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指按下记录xy的位置
                startX=ev.getX();
                startY=ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                //我们希望如果横向滑动距离大于纵向滑动距离，肯定是要操作viewpager
                //所以此处要告诉父控件不要拦截事件,否则事件交给父控件来处理
                getParent().requestDisallowInterceptTouchEvent(Math.abs(ev.getX() - startX) > Math.abs(ev.getY() - startY));
                break;
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

