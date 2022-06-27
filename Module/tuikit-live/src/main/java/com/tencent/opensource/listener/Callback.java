package com.tencent.opensource.listener;

public interface Callback {

    default void onSuccess() {
    }

    default void onFall() {
    }

    default void OnClickListener(int position) {
    }

    default void isNetworkAvailable() {
    }

    default void onSuccess(String msg) {
    }

    default void onSuccess(int msg) {
    }

    default void onSuccess(Object obj) {
    }

    default void onSuccess(double d) {
    }

    default void onSuccess(float msg) {
    }

    default void onSuccess(Object obj, int type) {
    }

}
