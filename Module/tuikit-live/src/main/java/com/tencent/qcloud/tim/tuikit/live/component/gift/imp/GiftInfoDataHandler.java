package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftData;
import com.tencent.qcloud.tim.tuikit.live.component.gift.OnGiftListQueryCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 礼物处理
 */
public class GiftInfoDataHandler {
    private static final String TAG = "GiftInfoManager";
    private GiftAdapter mGiftAdapter;
    private final Map<String, GiftInfo> mGiftInfoMap = new HashMap<>();

    public void setGiftAdapter(GiftAdapter adapter) {
        this.mGiftAdapter = adapter;
        //查询礼物列表数据
        queryGiftInfoList(null);
    }

    /**
     * 实现接口回调查询获取礼物列表数据
     * @param callback
     */
    public void queryGiftInfoList(final GiftQueryCallback callback) {
        if (mGiftAdapter != null) {
            mGiftAdapter.queryGiftInfoList(new OnGiftListQueryCallback() {
                @Override
                public void onGiftListQuerySuccess(List<GiftData> giftDataList) {
                    //这里拿到礼物列表数据
                    List<GiftInfo> giftInfoList = transformGiftInfoList(giftDataList);
                    if (callback != null) {
                        callback.onQuerySuccess(giftInfoList);
                    }
                }

                @Override
                public void onGiftListQueryFailed(String errorMessage) {
                    if (callback != null) {
                        callback.onQueryFailed(errorMessage);
                    }
                }
            });
        }
    }

    private List<GiftInfo> transformGiftInfoList(List<GiftData> giftDataList) {
        List<GiftInfo> giftInfoList = new ArrayList<>();
        mGiftInfoMap.clear();
        if (giftDataList != null) {
            for (GiftData giftData : giftDataList) {
                GiftInfo giftInfo = new GiftInfo();
                giftInfo.giftId = giftData.giftId;
                giftInfo.title = giftData.title;
                giftInfo.type = giftData.type;
                giftInfo.price = giftData.price;
                giftInfo.giftPicUrl = giftData.giftPicUrl;
                giftInfo.lottieUrl = giftData.lottieUrl;
                giftInfoList.add(giftInfo.copy());
                mGiftInfoMap.put(giftInfo.giftId, giftInfo);
            }
        }
        return giftInfoList;
    }

    public GiftInfo getGiftInfo(String giftId) {
        return mGiftInfoMap.get(giftId);
    }

    public interface GiftQueryCallback {
        void onQuerySuccess(List<GiftInfo> giftInfoList);

        void onQueryFailed(String errorMsg);
    }

}
