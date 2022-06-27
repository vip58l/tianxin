package com.tencent.liteav.trtcvoiceroom.ui.gift.imp;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.tencent.liteav.trtcvoiceroom.ui.gift.GiftAdapter;
import com.tencent.liteav.trtcvoiceroom.ui.gift.GiftData;
import com.tencent.liteav.trtcvoiceroom.ui.gift.HttpGetRequest;
import com.tencent.liteav.trtcvoiceroom.ui.gift.OnGiftListQueryCallback;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 请求获取网络数据
 */
public class DefaultGiftAdapterImp extends GiftAdapter implements HttpGetRequest.HttpListener {
    private static final String TAG = "DefaultGiftAdapterImp";
    private static final int CORE_POOL_SIZE = 5;
    private static final String GIFT_DATA_URL = "https://liteav.sdk.qcloud.com/app/res/picture/live/gift/gift_data.json";
    private static final String GIFT_DATA_URL2 = BuildConfig.HTTP_API + "/giftinfo"; //在build.gradle里配置了地址

    private GiftBeanThreadPool mGiftBeanThreadPool;
    private OnGiftListQueryCallback mOnGiftListQueryCallback;

    @Override
    public void queryGiftInfoList(OnGiftListQueryCallback callback) {
        mOnGiftListQueryCallback = callback;
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(GIFT_DATA_URL2, this);
        threadPoolExecutor.execute(request);
    }

    private synchronized ThreadPoolExecutor getThreadExecutor() {
        if (mGiftBeanThreadPool == null || mGiftBeanThreadPool.isShutdown()) {
            mGiftBeanThreadPool = new GiftBeanThreadPool(CORE_POOL_SIZE);
        }
        return mGiftBeanThreadPool;
    }

    @Override
    public void success(String response) {
        handleResponseMessage(response);
    }

    @Override
    public void onFailed(String message) {
        if (mOnGiftListQueryCallback != null) {
            mOnGiftListQueryCallback.onGiftListQueryFailed(message);
        }
    }

    private void handleResponseMessage(String response) {
        if (response == null) {
            return;
        }
        Gson gson = new Gson();
        GiftBean giftBean = gson.fromJson(response, GiftBean.class);
        final List<GiftData> giftDataList = transformGiftInfoList(giftBean);
        if (giftDataList != null) {
            if (mOnGiftListQueryCallback != null) {
                mOnGiftListQueryCallback.onGiftListQuerySuccess(giftDataList);
            }
        }
    }

    private List<GiftData> transformGiftInfoList(GiftBean giftBean) {
        if (giftBean == null) {
            return null;
        }
        List<GiftBean.GiftListBean> giftBeanList = giftBean.getGiftList();
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (GiftBean.GiftListBean bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getGiftId();
            giftData.title = bean.getTitle();
            giftData.type = bean.getType();
            giftData.price = bean.getPrice();
            giftData.giftPicUrl = bean.getGiftImageUrl();
            giftData.lottieUrl = bean.getLottieUrl();
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }

    public static class GiftBeanThreadPool extends ThreadPoolExecutor {
        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        public GiftBeanThreadPool(int poolSize) {
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(), Executors.defaultThreadFactory(), new AbortPolicy());
        }
    }
}
