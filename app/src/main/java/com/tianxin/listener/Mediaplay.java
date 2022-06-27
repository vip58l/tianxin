package com.tianxin.listener;

import android.view.View;


/**
 * 用于控制播放器处理
 */
public interface Mediaplay {

    //播放进度
    long setProgress();

    //播放视频
    void play();

    //载入播放
    void playVideo(View view);

    //清除播放
    void releaseVideo(View view);

    //翻页滑动管理器1
    default void PagerSnapHelpervideo(View view) {
    }

    //翻页滑动管理器2
    default void PagerLayoutManagervideo(View view) {
    }



}
