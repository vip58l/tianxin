package com.tianxin.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.AuthResult;
import com.alipay.sdk.pay.demo.PayResult;
import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.google.gson.Gson;
import com.tianxin.Module.api.moneylist;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.biz_content;
import com.tencent.opensource.model.zfb_wx_appid;

import java.util.Map;

/**
 * 支付宝发启VMC模式
 */
public class cs_alipay {
    private static final String TAG = "cs_alipay";
    private final Context context;
    private final module_alipay module_alipay;
    private final UserInfo userInfo;
    private Paymnets paymnets;
    /**
     * 用于支付宝支付业务的入参 app_id。
     */
    public static String APPID = "";
    /**
     * 用于支付宝账户登录授权业务的入参 pid。
     */
    public static String PID = "";
    /**
     * 用于支付宝账户登录授权业务的入参 target_id。 自定义的编号
     */
    public static String TARGET_ID = "";
    /**
     * pkcs8 格式的商户私钥。 应用私钥 PKCS8(JAVA适用)
     * <p>
     * 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个，如果两个都设置了，本 Demo 将优先
     * 使用 RSA2_PRIVATE。RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议商户使用
     * RSA2_PRIVATE。
     * <p>
     * 建议使用支付宝提供的公私钥生成工具生成和获取 RSA2_PRIVATE。
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static String RSA2_PRIVATE = "";
    public static String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private final Handler mHandler = new Mhandler();
    private static cs_alipay csAlipay;

    public static cs_alipay init(Paymnets paymnets) {
        if (csAlipay == null) {
            csAlipay = new cs_alipay(DemoApplication.instance(), paymnets);
        }
        return csAlipay;
    }

    public static cs_alipay init(Context context, Paymnets paymnets) {
        if (csAlipay == null) {
            csAlipay = new cs_alipay(context, paymnets);
        }
        return csAlipay;
    }

    public cs_alipay(Context context) {
        this.context = context;
        this.module_alipay = new module_alipay();
        this.userInfo = UserInfo.getInstance();
    }

    public cs_alipay(Context context, Paymnets paymnets) {
        this.context = context;
        this.module_alipay = new module_alipay();
        this.userInfo = UserInfo.getInstance();
        this.paymnets = paymnets;
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    /**
     * 发起支付走起
     */
    public void Paymoney(moneylist moneylist) {
        module_alipay.getdate(moneylist, userInfo, paymnetss);
    }

    /**
     * 支付宝支付业务示例
     */
    public void payV2(biz_content bizContent) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            showAlert(context, context.getString(com.alipay.sdk.pay.demo.R.string.error_missing_appid_rsa_private));
            return;
        }
        String toJson = new Gson().toJson(bizContent);
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, toJson);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("outtradeno", bizContent.getOut_trade_no());
                PayTask payTask = new PayTask((Activity) context);
                Map<String, String> result = payTask.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                msg.setData(bundle);
                mHandler.sendMessage(msg);

            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static void showAlert(Context ctx, String info) {
        showAlert(ctx, info, null);
    }

    public static void showAlert(Context ctx, String info, DialogInterface.OnDismissListener onDismiss) {
        new AlertDialog.Builder(ctx)
                .setMessage(info)
                .setPositiveButton(com.alipay.sdk.pay.demo.R.string.confirm, null)
                .setOnDismissListener(onDismiss)
                .show();
    }

    public static void showToast(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static String bundleToString(Bundle bundle) {
        if (bundle == null) {
            return "null";
        }
        final StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append(key).append("=>").append(bundle.get(key)).append("\n");
        }
        return sb.toString();
    }

    /**
     * 消息通知更新UI组件
     */
    private class Mhandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Bundle data = msg.getData();
                        String outtradeno = data.getString("outtradeno");

                        //把成功的参数发给服务器记录状态
                        module_alipay.update(outtradeno);

                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showAlert(context, context.getString(com.alipay.sdk.pay.demo.R.string.pay_success) + memo);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showAlert(context, context.getString(com.alipay.sdk.pay.demo.R.string.pay_failed) + memo);
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {

                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showAlert(context, context.getString(com.alipay.sdk.pay.demo.R.string.auth_success) + authResult);
                    } else {
                        // 其他状态值则为授权失败
                        showAlert(context, context.getString(com.alipay.sdk.pay.demo.R.string.auth_failed) + authResult);
                    }
                    break;
                }
                default:
                    break;
            }
        }


    }

    private final Paymnets paymnetss = new Paymnets() {
        @Override
        public void returnlt(Object obj) {
            zfb_wx_appid zfbWxAppid = (zfb_wx_appid) obj;
            APPID = zfbWxAppid.getAppid();
            PID = zfbWxAppid.getSecretid();
            TARGET_ID = userInfo.getUserId();
            RSA2_PRIVATE = zfbWxAppid.getSecretkey();
        }

        @Override
        public void onClick(Object object) {
            //订单预付创建成功可以发起支付请求
            payV2((biz_content) object);
        }

        @Override
        public void activity(String str) {
            if (paymnets != null) {
                paymnets.activity(str);
            }

        }

        @Override
        public void onSuccess() {
            if (paymnets != null) {
                paymnets.onSuccess();
            }
        }

        @Override
        public void onFail() {
            if (paymnets != null) {
                paymnets.onFail();
            }
        }
    };

}
