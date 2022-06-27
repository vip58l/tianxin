/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/6 0006
 */


package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.util.Log;

import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.base.HttpGetRequest;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 请求查询类
 */
public class DefaultGiftAdapter implements HttpGetRequest.HttpListener {
    String TAG = "DefaultGiftAdapter";
    private callback callback;
    private DefaultGiftAdapterImp.GiftBeanThreadPool mGiftBeanThreadPool;
    private final String GIFT_DATA_URL = BuildConfig.HTTP_API;

    /**
     * 发起请求扣除金币数据
     *
     * @param giftInfo
     * @param mSelfUserId
     * @param mAnchorInfo
     * @param callback
     */
    public void setCallback(GiftInfo giftInfo, String mSelfUserId, TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo, DefaultGiftAdapter.callback callback) {
        this.callback = callback;
        //发起网络GET请求 获取礼物列表数据
        //String mSelfUserId = V2TIMManager.getInstance().getLoginUser();
        //mSelfUserId   扣除金币
        //mAnchorUserId 增加金币
        //礼物gif id 会员mSelfUserId  扣除的金币 money 接收金币的主播mAnchorUserId
        //真实直播间4
        Map<String, String> map = new HashMap<>();
        map.put(Constants.USERID, mSelfUserId);
        map.put(Constants.TOKEN, UserInfo.getInstance().getToken());
        map.put("gif", giftInfo.giftId);
        map.put("money", String.valueOf(giftInfo.price));
        map.put("anchorid", mAnchorInfo.userId);
        map.put("type", "4");
        String path = GIFT_DATA_URL + "/jbdsgoldcoin?" + getMap(map, 3);
        queryGiftInfoList(path);
    }

    /**
     * 查询用户的金币数据
     *
     * @param callback
     */
    public void getCallback(DefaultGiftAdapter.callback callback) {
        this.callback = callback;
        //发起网络GET请求 获取礼物列表数据 用户id
        String mSelfUserId = V2TIMManager.getInstance().getLoginUser();
        UserInfo userInfo = UserInfo.getInstance();
        queryGiftInfoList(GIFT_DATA_URL + "/goldcoin?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken());
    }

    /**
     * 网络发起查询音乐
     *
     * @param callback
     */
    public void getHttpGet(String path, DefaultGiftAdapter.callback callback) {
        this.callback = callback;
      String url= GIFT_DATA_URL + path;
        Log.d(TAG, "getHttpGet: "+url);
        queryGiftInfoList(url);
    }


    private void queryGiftInfoList(String path) {
        ThreadPoolExecutor threadPoolExecutor = getThreadExecutor();
        HttpGetRequest request = new HttpGetRequest(path, this);
        threadPoolExecutor.execute(request);
    }

    private ThreadPoolExecutor getThreadExecutor() {
        if (mGiftBeanThreadPool == null || mGiftBeanThreadPool.isShutdown()) {
            mGiftBeanThreadPool = new DefaultGiftAdapterImp.GiftBeanThreadPool(5);
        }
        return mGiftBeanThreadPool;
    }

    @Override
    public void success(String response) {
        callback.success(response);
    }

    @Override
    public void onFailed(String message) {
        callback.onFailed(message);

    }

    /**
     * 字符串拼接
     *
     * @param map
     * @param TYPE
     * @return
     */
    public static String getMap(Map<String, String> map, int TYPE) {
        StringBuffer sb = new StringBuffer();
        int is = 0;
        switch (TYPE) {
            case 0:
            case 1:
                //第一种：
                for (String key : map.keySet()) {
                    if (is > 0) {
                        sb.append("&" + key + "=" + map.get(key));
                    } else {
                        sb.append(key + "=" + map.get(key));
                    }
                    is++;
                }
                break;
            case 2:
                //第二种
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
            case 3:
                //第三种：
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
        }
        return sb.toString();
    }

    /**
     * 自定义接口
     */
    public interface callback {
        //联网成功内容
        void success(String response);

        //联网失败
        void onFailed(String message);
    }
}
