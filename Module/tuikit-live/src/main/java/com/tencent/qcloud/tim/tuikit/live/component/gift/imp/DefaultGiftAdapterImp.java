package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.annotation.TargetApi;
import android.os.Build;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.base.HttpGetRequest;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftData;
import com.tencent.qcloud.tim.tuikit.live.component.gift.OnGiftListQueryCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 联网获取礼物的内容列表
 */
public class DefaultGiftAdapterImp extends GiftAdapter implements HttpGetRequest.HttpListener {
    private static final String TAG = "DefaultGiftAdapterImp";
    private static final int CORE_POOL_SIZE = 5;
    private static final String GIFT_DATA_URL = "https://liteav.sdk.qcloud.com/app/res/picture/live/gift/gift_data.json";
    private static final String GIFT_DATA_URL2 = BuildConfig.HTTP_API + "/giftinfo"; //在build.gradle里配置了地址
    private GiftBeanThreadPool mGiftBeanThreadPool;
    private OnGiftListQueryCallback mOnGiftListQueryCallback;

    @Override
    public void queryGiftInfoList(final OnGiftListQueryCallback callback) {
        mOnGiftListQueryCallback = callback;
        //发起网络GET请求 获取礼物列表数据
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
        //参过接口返回请求到的数居列表
        handleResponseMessage(response);
    }

    @Override
    public void onFailed(String message) {
        if (mOnGiftListQueryCallback != null) {
            //回调接口方法
            mOnGiftListQueryCallback.onGiftListQueryFailed(message);
        }
    }

    /**
     * 处理网络求得到的数据 解析网络Json转成对像
     *
     * @param response
     */
    private void handleResponseMessage(String response) {
        if (response == null) {
            return;
        }

        GiftBean giftBean = new Gson().fromJson(response, GiftBean.class);
        final List<GiftData> giftDataList = transformGiftInfoList(giftBean);
        if (giftDataList != null) {
            if (mOnGiftListQueryCallback != null) {
                //返回一个接口
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
            super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),
                    Executors.defaultThreadFactory(), new AbortPolicy());
        }
    }
}
