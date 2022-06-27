package com.tianxin.wxapi;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tianxin.Module.Datamodule;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.api.Share;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tianxin.wxapi.sdk.WXPlay;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.opensource.model.tpconfig;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.List;

/**
 * 微信开放平台统一管理类
 */
public class WXpayObject {
    private static final int THUMB_SIZE = 150;
    private static WXpayObject sWXpayObject;
    private String TAG = WXpayObject.class.getSimpleName();
    private tpconfig t;

    /**
     * 微信分享登录初始化
     *
     * @return
     */
    public static WXpayObject getsWXpayObject() {
        if (sWXpayObject == null) {
            sWXpayObject = new WXpayObject();
        }
        return sWXpayObject;
    }

    //APP_ID 替换为你的应用从官方网站申请到的合法appID
//    public static final String APP_ID = "wx420a80234bdc4187";
//    public static final String AppSecret = "b4044efae31dddaf38b8e4e1034cda35";

    //个体工商户 APP_ID 替换为你的应用从官方网站申请到的合法appID
    public static String APP_ID = BuildConfig.APP_ID;
    public static String AppSecret = BuildConfig.AppSecret;

    //IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    public IWXAPI getApi() {
        return api;
    }

    public WXpayObject() {
        if (!Config.isNetworkAvailable()) {
            init();
        } else {
            Datamodule.getInstance().Openweixi(new Paymnets() {

                @Override
                public void isNetworkAvailable() {


                }

                @Override
                public void onFail() {

                }

                @Override
                public void onSuccess(Object object) {
                    try {
                        t = (tpconfig) object;
                        APP_ID = t.getAppid();
                        AppSecret = t.getSecretkey();
                        init();
                    } catch (Exception e) {
                        e.printStackTrace();
                        init();

                    }
                }
            });
        }

    }

    private void init() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(DemoApplication.instance(), APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
        //建议动态监听微信启动广播进行注册到微信
        DemoApplication.instance().registerReceiver(new AppRegister(), new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }

    /**
     * 分享到微信
     *
     * @param share
     */
    public void sharewechat(Share share, int mTargetScene) {

        if (!isWeixinAvilible()) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm80));
            return;
        }
        if (share == null) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm81));
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = share.getH5Url();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = share.getTitle();
        msg.description = share.getText();
        msg.thumbData = getIconbyte(DemoApplication.instance(), share);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }


    /**
     * 分享图片到微信
     */
    public void sharewechat(Context context, Bitmap bmp) {
        //设置消息的缩略图
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        if (!isWeixinAvilible()) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm80));
            return;
        }


        try {
            //发图片到微信
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

            //发送到微信
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            Toashow.show(e.getMessage());
        }
    }

    /**
     * 分享图片到微信
     */
    public void sharewechat(Context context, Bitmap bmp, int type) {

        if (!isWeixinAvilible()) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm80));
            return;
        }

        //设置消息的缩略图
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }

        try {
            //发图片到微信
            WXImageObject imgObj = new WXImageObject(bmp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            bmp.recycle();
            msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

            //发送到微信
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = type;
//            req.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            Toashow.show(e.getMessage());
        }
    }

    /**
     * 微信授权登录login
     */
    public void logdinwx() {
        if (!isWeixinAvilible()) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm80));
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_user";
        api.sendReq(req);
    }

    /**
     * 获取用户get_token
     */
    public void gettoken() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "none";
        api.sendReq(req);
    }

    public String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 判断手机是否安装微信
     *
     * @return
     */
    public static boolean isWeixinAvilible() {
        final PackageManager packageManager = DemoApplication.instance().getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String packageName = pinfo.get(i).packageName;
                if (packageName.equals("com.tencent.mm")) {
                    return true;
                }
            }

        }
        return false;

    }

    /**
     * 这里发给服务器统一下单 服务器回调APP获取数据调起APP支付
     *
     * @param money
     */
    public void WXpay(moneylist money) {
        if (!isWeixinAvilible()) {
            Toashow.show(DemoApplication.instance().getString(R.string.tm80));
            return;
        }
        Datamodule.getInstance().Wxapppay(1, money, new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                WXPlay wxPlay = (WXPlay) object;
                PayReq req = new PayReq();
                req.appId = wxPlay.getAppid();               //应用ID
                req.partnerId = wxPlay.getPartnerid();       //微信支付分配的商户号
                req.prepayId = wxPlay.getPrepayid();         //会话ID预支付交易会话ID
                req.packageValue = wxPlay.getPackageValue(); //暂填写固定值Sign=WXPay
                req.nonceStr = wxPlay.getNoncestr();         //随机字符串
                req.timeStamp = wxPlay.getTimestamp();       //时间戳
                req.sign = wxPlay.getSign();                 //签名
                api.sendReq(req);                            // 调起微信支付页面
            }

            @Override
            public void onFail() {

            }
        });
    }

    private byte[] getIconbyte(Context context, Share share) {
        try {
            return Util.getHtmlByteArray(share.getIcon());
        } catch (Exception e) {
            return MediaMessage(context);
        }
    }

    private byte[] MediaMessage(Context context) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        byte[] bytes = Util.bmpToByteArray(thumbBmp, true);
        return bytes;
    }

}
