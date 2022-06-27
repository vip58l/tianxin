package com.tencent.qcloud.costransferpractice.dialog;

public interface Callback {

    void onSuccess();

    default void onappeal() {
    }

    void onFall();
}
