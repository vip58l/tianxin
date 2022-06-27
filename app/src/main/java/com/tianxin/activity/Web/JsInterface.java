package com.tianxin.activity.Web;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.tianxin.Module.api.moneylist;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.Memberverify.activity_livebroadcast;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.activity.Withdrawal.acitivity_settlement;
import com.tianxin.activity.Withdrawal.activity_balance;
import com.tianxin.activity.sesemys.activity_amountconfig;
import com.tianxin.activity.shareqrcode.activity_Share;
import com.tianxin.activity.shareqrcode.activity_getspreadUrl;
import com.tianxin.dialog.Dialog_fenxing;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

//Javascript调用本类方法
//html js调用需要在方法写上aa.xx()
public class JsInterface {
    private String TAG = JsInterface.class.getSimpleName();
    private Context context;

    public JsInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public static void showToast(String msg) {
        ToastUtil.toastLongMessage(msg);
    }

    @JavascriptInterface
    public static void myToashow(String msg) {
        Toashow.show(msg);
    }

    @JavascriptInterface
    public void shareqrcode() {
//        activity_shareqrcode.starAction(context);
        activity_getspreadUrl.starAction(context);

    }

    @JavascriptInterface
    public void sharestarAction(String touserid) {
        activity_amountconfig.starsetAction(context, touserid);
    }


    @JavascriptInterface
    public void shareqrcodeagent() {
        //分享代理海报
        activity_getspreadUrl.starAction(context, 1);
    }

    @JavascriptInterface
    public void Sharelist(int type) {
        switch (type) {
            case 1:

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                //查看推广好友注册
                activity_Share.starAction(context);
                break;
            case 5:
                //转到赚钱任务
                References.starAction(context);
                break;
            case 6:
                //我的钱包
                activity_balance.setAction(context);
                break;
            case 7:
                //代理海报
                activity_getspreadUrl.starAction(context, 1);
                break;
        }
    }

    @JavascriptInterface
    public void wxpay(String money) {
        //调起APP微信支付操作
        try {
            moneylist moneylist = new moneylist();
            moneylist.setMoney(money);
            moneylist.setVip(2);
            moneylist.setId(0);
            WXpayObject.getsWXpayObject().WXpay(moneylist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调起分享页
     */
    @JavascriptInterface
    public void shareToWeixin(String paramStr) {
        Dialog_fenxing.myshow(context, paramStr);
    }

    /**
     * 保存图片
     */
    @JavascriptInterface
    public void saveImage(String paramStr) {
        Log.d(TAG, "saveImage: " + paramStr);
    }


    /**
     * 保存图片
     */
    @JavascriptInterface
    public void opentype(int type) {
        switch (type) {
            case 1://实名认证
                activity_livebroadcast.setAction(context);
                break;
            case 2://获取二维码
                activity_getspreadUrl.starAction(context);
                break;
            case 3://绑定结算帐户
                acitivity_settlement.starsetAction(context);
                break;
            case 4://升级成为代理
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            default:
                Toashow.show("activity不存在 无法调起");
                break;
        }
    }

}