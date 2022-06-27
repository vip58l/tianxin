package com.tencent.qcloud.tim.tuikit.live.component.gift;

import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfoDataHandler;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;

public interface IGiftPanelView {
    /**
     * 面板通用接口
     */
    void init(GiftInfoDataHandler giftInfoDataHandler);

    /**
     * 打开礼物面板
     */
    void show();

    /**
     * 关闭礼物面板
     */
    void hide();

    //订阅礼物面板事件
    void setGiftPanelDelegate(GiftPanelDelegate delegate);

    //传递主播和观众ID用于扣除金币
    void setGiftPanelUser(TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo, String mSelfUserId);




}
