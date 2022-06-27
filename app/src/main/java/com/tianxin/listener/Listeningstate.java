package com.tianxin.listener;

import com.tencent.cos.xml.transfer.TransferState;

import java.util.List;

public interface Listeningstate {

    //刷新上载状态
    void onStateChanged(TransferState state);

    //上传状态
    void onProgress(long complete, long total, long progress, String size);

    //上传成功返回数组和对像网址
    void onSuccess(List<String> list, String accessUrl);

   default void oncomplete(){}

    //上传失败
    void onFail();
}
