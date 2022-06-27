package com.tianxin.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class PullableListView extends ListView implements Pullable {

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        // 滑到ListView的顶部了
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else {
            try {
                return getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0;
            } catch (Exception e) {
                e.getMessage();

            }

            return false;
        }
    }

    @Override
    public boolean canPullUp() {
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            return getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight();
        }
        return false;
    }


}
