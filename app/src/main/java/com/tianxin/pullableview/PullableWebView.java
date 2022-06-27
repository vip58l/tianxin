package com.tianxin.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class PullableWebView extends WebView implements Pullable
{

	public PullableWebView(Context context)
	{
		super(context);
	}

	public PullableWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableWebView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
        return getScrollY() == 0;
	}

	@Override
	public boolean canPullUp()
	{
        return getScrollY() >= getContentHeight() * getScale()
                - getMeasuredHeight();
	}
}
