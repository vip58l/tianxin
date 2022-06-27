package com.tianxin.widget;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {

	public HackyViewPager(Context context) {
		super(context);
	}
	
	public HackyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		try {
			return super.onInterceptTouchEvent(ev);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
            return false;
        }
		/**
		 * 在进行图片放大缩小的时候，图片缩小出现这个异常，查询了一下发现解决方案
		 * java.lang.IllegalArgumentException: pointerIndex out of range pointerIndex=-1 pointerCount=1
		 * 解决方法就是在自己自定义的ViewPager中重写这个方法，并且捕获这个异常就可以了
		 *
		 * 重写onInterceptTouchEvent()方法来解决图片点击缩小时候的Crash问题
		 */

	}

}
