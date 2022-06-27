package com.tencent.qcloud.tim.tuikit.live.component.gift;

import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;

public interface GiftPanelDelegate {
    /**
     * 礼物送出点击事件
     */
    void onGiftItemClick(GiftInfo giftInfo);

    /**
     * 充值点击事件
     */
    void onChargeClick();


    /**
     * 可用余额回调
     */
    default void myoney(Object obj) {
    }
}
