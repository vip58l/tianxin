package com.tianxin.listener;

import com.amap.api.location.AMapLocation;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public interface Callback<T> {

    default void onSuccess() {
    }

    default void onFall() {
    }

    default void onError() {
    }

    default void onSuccess(AMapLocation amapLocation) {
    }

    default void OnClickListener(int position) {

    }

    default void onSuccess(boolean boole) {
    }

    default void isNetworkAvailable() {
    }

    default void onSuccess(String msg) {
    }

    default void LongClickListener(int position) {
    }

    default void OndeleteListener(int position) {
    }


    default void Blockedaccount() {
    }

    default void onCompletion(IMediaPlayer iMediaPlayer) {
    }

    default void onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
    }
}
