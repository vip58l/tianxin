/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/19 0019
 */


package com.tianxin.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.RequiresApi;


public class MyScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener = null;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        //x为当前滑动条的横坐标，y表示当前滑动条的纵坐标，oldx为前一次滑动的横坐标，oldy表示前一次滑动的纵坐标
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            //在这里将方法暴露出去
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    public interface ScrollViewListener {
        void onScrollChanged(View scrollView, int x, int y, int oldx, int oldy);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

}
