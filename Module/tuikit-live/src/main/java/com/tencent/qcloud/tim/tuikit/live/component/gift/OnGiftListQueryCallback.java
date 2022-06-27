package com.tencent.qcloud.tim.tuikit.live.component.gift;

import java.util.List;

public interface OnGiftListQueryCallback {
    /**
     * 查询成功 响应结果
     * @param giftInfoList
     */
    void onGiftListQuerySuccess(List<GiftData> giftInfoList);
    /**
     * 网络请求查询失败
     */
    void onGiftListQueryFailed(String errorMsg);
}
