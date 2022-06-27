package com.tianxin.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class AppRegister extends BroadcastReceiver {
    private IWXAPI api;
    private String TAG = AppRegister.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        api = WXAPIFactory.createWXAPI(context, WXpayObject.APP_ID, false);
        api.registerApp(WXpayObject.APP_ID);
        Log.d(TAG, "onReceive: 微信广播");
    }
}
