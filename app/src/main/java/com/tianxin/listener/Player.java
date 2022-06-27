package com.tianxin.listener;

import android.media.MediaPlayer;

import com.tianxin.Module.api.misc;


//定义一个接口用于反回前台绑定调用使用
public interface Player {

    default void setPath(String path) {
    }

    default void seekTo(int i) {
    }

    default void start() {
    }

    default void pause() {
    }

    default void stop() {
    }

    default void release() {
    }

    default void reset() {
    }

    default void setLooping(boolean b) {
    }

    default MediaPlayer getMediaPlayer() {
        return null;
    }

    void setmisc(misc misc);

    misc getmisc();

    boolean isPlaying();

    int getcurrentposition();

    int getmyDuration();


}
