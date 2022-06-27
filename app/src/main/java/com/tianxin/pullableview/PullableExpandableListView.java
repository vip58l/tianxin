package com.tianxin.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class PullableExpandableListView extends ExpandableListView implements
		Pullable
{

	public PullableExpandableListView(Context context)
	{
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
        // 滑到顶部了
        if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else return getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
            return getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(
                    getLastVisiblePosition()
                            - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight();
		}
		return false;
	}

}
