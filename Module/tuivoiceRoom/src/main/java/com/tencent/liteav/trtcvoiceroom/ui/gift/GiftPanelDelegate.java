package com.tencent.liteav.trtcvoiceroom.ui.gift;


import com.tencent.liteav.trtcvoiceroom.ui.gift.imp.GiftInfo;

public interface GiftPanelDelegate {
    /**
     * 礼物点击事件
     */
    void onGiftItemClick(GiftInfo giftInfo);

    /**
     * 充值点击事件
     */
    void onChargeClick();
}
