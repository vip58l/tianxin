package com.tianxin.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Login.UserInfoActivity;
import com.tianxin.activity.Login.UserLoginActivity;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_Cancellation;
import com.tianxin.dialog.dialog_upgrade;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessView;
import com.tencent.mm.opensdk.modelbiz.WXOpenBusinessWebview;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.opensdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

/**
 * 微信回调数据
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "WXEntryActivity";
    private MyHandler handler;
    private IWXAPI api;
    private boolean finishs = false;
    private Activity activity;
    private Context context;
    private String openId, accessToken, refreshToken, scope, unionid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        handler = new MyHandler(this);
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
        finish();
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) baseReq);
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onResp: " + resp.errCode);
        int result = 0;
        //ErrCodeERR_OK = 0(用户同意) ERR_AUTH_DENIED = -4（用户拒绝授权） ERR_USER_CANCEL = -2（用户取消
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        if (resp.getType() == ConstantsAPI.COMMAND_SUBSCRIBE_MESSAGE) {
            SubscribeMessage.Resp subscribeMsgResp = (SubscribeMessage.Resp) resp;
            String text = String.format("openid=%s\ntemplate_id=%s\nscene=%d\naction=%s\nreserved=%s",
                    subscribeMsgResp.openId, subscribeMsgResp.templateID, subscribeMsgResp.scene, subscribeMsgResp.action, subscribeMsgResp.reserved);

            Log.d(TAG, "COMMAND_SUBSCRIBE_MESSAGE: " + text);
        }

        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProgramResp = (WXLaunchMiniProgram.Resp) resp;
            String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s",
                    launchMiniProgramResp.openId, launchMiniProgramResp.extMsg, launchMiniProgramResp.errStr);

            Log.d(TAG, "COMMAND_LAUNCH_WX_MINIPROGRAM: " + text);
        }

        if (resp.getType() == ConstantsAPI.COMMAND_OPEN_BUSINESS_VIEW) {
            WXOpenBusinessView.Resp launchMiniProgramResp = (WXOpenBusinessView.Resp) resp;
            String text = String.format("openid=%s\nextMsg=%s\nerrStr=%s\nbusinessType=%s",
                    launchMiniProgramResp.openId, launchMiniProgramResp.extMsg, launchMiniProgramResp.errStr, launchMiniProgramResp.businessType);

            Log.d(TAG, "COMMAND_OPEN_BUSINESS_VIEW: " + text);
        }

        if (resp.getType() == ConstantsAPI.COMMAND_OPEN_BUSINESS_WEBVIEW) {
            WXOpenBusinessWebview.Resp response = (WXOpenBusinessWebview.Resp) resp;
            String text = String.format("businessType=%d\nresultInfo=%s\nret=%d", response.businessType, response.resultInfo, response.errCode);
            Log.d(TAG, "COMMAND_OPEN_BUSINESS_WEBVIEW: " + text);
        }

        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //继续完成分享
            //ToastUtil.toastLongMessage("分享成功");
            Log.d(TAG, "分享成功: ");
        }

        /**
         * 授权登录 第二步：通过 code 获取 access_token
         * 微信登录功能 /授权后接口调用（UnionID）  //获取TOKEY
         */
        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            if (!TextUtils.isEmpty(authResp.code)) {
                String code = authResp.code;
                String url = String.format(Webrowse.wxapi + "/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                        WXpayObject.APP_ID,
                        WXpayObject.AppSecret,
                        code);
                //请求登录结果 返回到Handler 处理请求结果
                NetworkUtil.sendWxAPI(handler, url, NetworkUtil.GET_TOKEN);
                finishs = true; //Activity 暂时不要关闭
            } else {
                ToastUtil.toastShortMessage(getString(R.string.login_fail));
                finishs = false;//登录签名失败
            }

        }

        /**
         * 4、支付结果回调
         */
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("支付成功");
                    builder.setMessage(String.format("支付成功了%s", resp.errCode));
                    builder.show();
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("签名错误");
                    builder1.setMessage("未注册APPID、注册的APPID与设置的不匹配、其他异常等");
                    builder1.show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle("用户取消 无需处理");
                    builder2.show();
                    break;
            }
        }

        if (!finishs) {
            finish();
        }

    }

    private void goToGetMsg() {
        Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer();
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("title", wxMsg.title);
        intent.putExtra("SMessage", msg.toString());
        intent.putExtra("BAThumbData", wxMsg.thumbData);
        startActivity(intent);
        finish();
    }

    /**
     * 获取授权结果回调
     */
    private class MyHandler extends Handler {
        private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

        public MyHandler(WXEntryActivity wxEntryActivity) {
            wxEntryActivityWeakReference = new WeakReference<WXEntryActivity>(wxEntryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            int tag = msg.what;
            Bundle data = msg.getData();
            JSONObject json = null;
            switch (tag) {
                //正确的返回 获取GET_TOKEN
                case NetworkUtil.GET_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        openId = json.getString("openid");
                        accessToken = json.getString("access_token");
                        refreshToken = json.getString("refresh_token");
                        scope = json.getString("scope");
                        unionid = json.getString("unionid");

                        //提交给服务器请求登录并查询是否已存在此帐号
                        wxlogin(0);

                        //检验授权凭证（access_token）是否有效
                        //NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s", accessToken, openId), NetworkUtil.CHECK_TOKEN);
                        //NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", accessToken, openId), NetworkUtil.GET_INFO);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }
                /**
                 * 获取到个人详情资料
                 */
                case NetworkUtil.GET_INFO: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        final String nickname, sex, province, city, country, headimgurl;
                        headimgurl = json.getString("headimgurl");
                        NetworkUtil.getImage(handler, headimgurl, NetworkUtil.GET_IMG);
                        String encode;
                        encode = getcode(json.getString("nickname"));
                        nickname = "nickname: " + new String(json.getString("nickname").getBytes(encode), "utf-8");
                        sex = "sex: " + json.getString("sex");
                        province = "province: " + json.getString("province");
                        city = "city: " + json.getString("city");
                        country = "country: " + json.getString("country");

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }
                //获取个人头像回调
                case NetworkUtil.GET_IMG: {
                    byte[] imgdata = data.getByteArray("imgdata");
                    final Bitmap bitmap;
                    if (imgdata != null) {
                        bitmap = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
                        Log.d(TAG, "handleMessage:头像图片获取成功 ");
                    } else {
                        bitmap = null;
                        Log.d(TAG, "handleMessage: 头像图片获取失败");
                    }
                    break;
                }
                //检验授权凭证（access_token）是否有效
                case NetworkUtil.CHECK_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        int errcode = json.getInt("errcode");
                        String openId, accessToken, refreshToken, scope, unionid;
                        openId = json.getString("openid");
                        accessToken = json.getString("access_token");
                        refreshToken = json.getString("refresh_token");
                        scope = json.getString("scope");
                        unionid = json.getString("unionid");
                        if (errcode == 0) {
                            //获取到个人详情资料
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", accessToken, openId), NetworkUtil.GET_INFO);
                        } else {
                            //刷新验证
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s", WXpayObject.APP_ID, refreshToken), NetworkUtil.REFRESH_TOKEN);
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }

                case NetworkUtil.GET_REG: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        Log.d(TAG, "handleMessage: " + json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private static String getcode(String str) {
        String[] encodelist = {"GB2312", "ISO-8859-1", "UTF-8", "GBK", "Big5", "UTF-16LE", "Shift_JIS", "EUC-JP"};
        for (int i = 0; i < encodelist.length; i++) {
            try {
                if (str.equals(new String(str.getBytes(encodelist[i]), encodelist[i]))) {
                    return encodelist[i];
                }
            } catch (Exception e) {

            } finally {

            }
        }
        return "";
    }

    private Paymnets dialogpaylog = new Paymnets() {
        @Override
        public void onFail() {
            finish();
        }

        @Override
        public void onSuccess() {
            //撤消息注销记录
            wxlogin(1);
            finish();
        }
    };

    /**
     * 登录app官网
     *
     * @param TYPE
     */
    private void wxlogin(int TYPE) {
        Datamodule.getInstance().openwxinlogin(openId, accessToken, TYPE, new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                Mesresult mesresult = (Mesresult) object;
                TUIKit.login(mesresult.getUsreid(), mesresult.getUserSig(), new IUIKitCallBack() {
                    @Override
                    public void onError(String module, final int code, final String desc) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //登录IM失败提示用户更新APP 用户随时--->跳转-->官网-->更新
                                dialog_upgrade.updada_Msg(UserLoginActivity.userLoginActivity);
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(Object data) {
                        activity_sesemys.user_save_update_Profile(mesresult);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent, Config.sussess);
                            }
                        });
                    }
                });
            }

            @Override
            public void onFail() {
                //获取到用户授权成功跳转到获取个人资料 用户提交注册
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("openId", openId);
                intent.putExtra("accessToken", accessToken);
                intent.putExtra("refreshToken", refreshToken);
                intent.putExtra("scope", scope);
                intent.putExtra("unionid", unionid);
                startActivityForResult(intent, Config.sussess);
            }

            @Override
            public void onRefresh() {
                //注销申请存在
                Log.d(TAG, "onRefresh:注销申请存在 ");
                dialog_Cancellation.myshow(context, dialogpaylog);
            }

            @Override
            public void onError() {
                //封号了
                Log.d(TAG, "onError: 封号了");
                dialog_Blocked.myshow(UserLoginActivity.userLoginActivity, null);  //封号提示
                finish();

            }

            @Override
            public void onFail(String msg) {
                Toashow.show(msg);
                Log.d(TAG, "onFail: " + msg);
                finish();
            }


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }

}
