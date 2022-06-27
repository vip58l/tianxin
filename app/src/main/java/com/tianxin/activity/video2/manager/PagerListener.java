package com.tianxin.activity.video2.manager;

import android.view.View;

/**
 * 播放器监听接口
 */
public interface PagerListener {
    /**
     * 初始化 开始播放
     */
    void onInitComplete(View view);

    /**
     * 释放停止播放
     */
    void onPageRelease(boolean isNext, int position, View view);

    /**
     * 选中 开始播放
     */
    void onPageSelected(int position, boolean isBottom, View view);
}