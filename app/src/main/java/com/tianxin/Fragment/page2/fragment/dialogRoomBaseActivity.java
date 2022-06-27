package com.tianxin.Fragment.page2.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.dialog.Dialog_bottom;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;

public class dialogRoomBaseActivity extends Activity {
    String TAG = dialogRoomBaseActivity.class.getSimpleName();
    private cs_alipay csAlipay;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        wl.alpha = 0f;
        window.setAttributes(wl);
        context = this;
        csAlipay = new cs_alipay(context, paymnets);
        Dialog_bottom.dialogsBottom(context, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));

                }
            });
        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);

                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            //发起支付请求
            csAlipay.Paymoney(moneylist);
        }

        @Override
        public void dismiss() {

        }
    };
}
