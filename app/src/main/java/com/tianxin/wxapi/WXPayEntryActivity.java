package com.tianxin.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = WXPayEntryActivity.class.getSimpleName();
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXpayObject.getsWXpayObject().getApi();
        try {
            Intent intent = getIntent();
            WXpayObject.getsWXpayObject().getApi().handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.d(TAG, "onReq: " + baseReq.toString());
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    Toashow.show(getString(R.string.pay1));
                    //发送广播关闭支付页面
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    Toashow.show(getString(R.string.pay2));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    Toashow.show(getString(R.string.pay3));
                    break;
            }
            finish();
        }

    }
}
