package com.tianxin.widget;

public interface OnPageSlideListener {

    //析放的资料监听
    void onPageRelease(boolean isNext, int position);

    //选中的监听以及判断是否滑动到底部
    void onPageSelected(int position, boolean isBottom);

    //布局完成监听
    void OnLayoutConplete();
}
