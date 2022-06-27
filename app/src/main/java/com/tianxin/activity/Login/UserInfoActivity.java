package com.tianxin.activity.Login;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.dialog.dialog_amp_item;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tianxin.wxapi.NetworkUtil;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 微信极权登录
 */
public class UserInfoActivity extends BasActivity2 {
    private MyHandler handler;
    private String refreshToken, openId, accessToken, scope, unionid, tnickname, theadimgurl, username, qrcode;
    private String TAG = UserInfoActivity.class.getSimpleName();
    @BindView(R.id.username)
    EditText user;
    @BindView(R.id.code)
    EditText mycode;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.codemsg)
    TextView codemsg;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    private int sex;
    private String[] permissionss;
    private boolean okpermission = false;
    Handler handlertime = new Handlertime();

    /**
     * 时间倒计时
     */
    private class Handlertime extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    timers--;
                    if (timers > 1) {
                        truecode = false;
                        codemsg.setText(String.format(getString(R.string.tm90) + "", timers));
                        handlertime.sendEmptyMessageDelayed(Config.sussess, 1000);
                    } else {
                        truecode = true;
                        handlertime.removeCallbacksAndMessages(null);
                        timers = 60;
                        codemsg.setText(R.string.tv_ss_a4);
                    }
                    break;
            }
        }
    }

    private class MyHandler extends Handler {
        private final WeakReference<UserInfoActivity> userInfoActivityWR;

        public MyHandler(UserInfoActivity userInfoActivity) {
            userInfoActivityWR = new WeakReference<UserInfoActivity>(userInfoActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            int tag = msg.what;
            Bundle data = msg.getData();
            JSONObject json = null;
            switch (tag) {

                //检验授权凭证（access_token）是否有效
                case NetworkUtil.CHECK_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        int errcode = json.getInt("errcode");
                        if (errcode == 0) {
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                                    accessToken, openId), NetworkUtil.GET_INFO);
                        } else {
                            //刷新个人资料
                            NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s", WXpayObject.APP_ID, refreshToken),
                                    NetworkUtil.REFRESH_TOKEN);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    break;
                }

                //刷新回调REFRESH_TOKEN
                case NetworkUtil.REFRESH_TOKEN: {
                    try {
                        json = new JSONObject(data.getString("result"));
                        openId = json.getString("openid");
                        accessToken = json.getString("access_token");
                        refreshToken = json.getString("refresh_token");
                        scope = json.getString("scope");
                        NetworkUtil.sendWxAPI(handler, String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s", accessToken, openId), NetworkUtil.GET_INFO);
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
                        nickname = new String(json.getString("nickname").getBytes(encode), "utf-8");

                        tnickname = nickname;
                        theadimgurl = headimgurl;
                        //微信接升级v6.8已不存返回性别及定位地址
                        sex = "sex: " + json.getString("sex");
                        province = "province: " + json.getString("province");
                        city = "city: " + json.getString("city");
                        country = "country: " + json.getString("country");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Glideload.loadImage(image, headimgurl, 6);
                                name.setText(nickname);
                            }
                        });

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //image.setImageBitmap(bitmap);
                            }
                        });
                    } else {
                        bitmap = null;
                        Log.d(TAG, "handleMessage: 头像图片获取失败");
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

    @Override
    protected int getview() {
        return R.layout.activity_userinfoactivity;
    }

    @Override
    public void iniview() {
        defaultimv();
        if (ActivityLocation.checkpermissions(activity)) {
            lbsamap.getmyLocation(callback);
        }
    }

    @Override
    public void initData() {
        Intent data = getIntent();
        openId = data.getStringExtra("openId");
        accessToken = data.getStringExtra("accessToken");
        refreshToken = data.getStringExtra("refreshToken");
        scope = "scope: " + data.getStringExtra("scope");
        unionid = data.getStringExtra("unionid");
        //检验授权凭证（access_token）是否有效
        handler = new MyHandler(this);
        NetworkUtil.sendWxAPI(handler, String.format(Webrowse.wxapi + "/sns/auth?access_token=%s&openid=%s", accessToken, openId), NetworkUtil.CHECK_TOKEN);
    }

    @Override
    @OnClick({R.id.send, R.id.codemsg, R.id.l1, R.id.l2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.l1:
                defaultimv();
                sex = 1;
                image1.setImageResource(R.mipmap.a4b);
                break;
            case R.id.l2:
                defaultimv();
                sex = 2;
                image2.setImageResource(R.mipmap.a4a);
                break;
            case R.id.send:
                wxreg();
                break;
            case R.id.codemsg:
                //时间倒计60秒
                getuserconde();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlertime.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionss = permissions;
        switch (requestCode) {
            case OPEN_SET_REQUEST_CODE:
                for (String permission : permissions) {
                    int STATE = ContextCompat.checkSelfPermission(context, permission);
                    if (STATE != PackageManager.PERMISSION_GRANTED) {
                        if (!okpermission) {
                            dialogampitem();
                        }
                        return;
                    }
                }
                lbsamap.getmyLocation(callback);
                break;
        }

    }

    /**
     * 未获取授权转入申请授权
     */
    private void dialogampitem() {
        dialog_amp_item.dialogampitem(context, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess() {
            okpermission = true;
            ActivityCompat.requestPermissions(activity, permissionss, OPEN_SET_REQUEST_CODE);
        }

        @Override
        public void onSuccess(Object object) {
            handler.sendEmptyMessageDelayed(Config.sussess, 1000);
        }

        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));

        }
    };

    private Paymnets Callback = new Paymnets() {
        @Override
        public void onFail() {
            dialogLoadings();
        }


        @Override
        public void onSuccess(Object object) {
            Mesresult mesresult = (Mesresult) object;
            //本地加签名
            //loginIm(mesresult.getUsreid(), GenerateTestUserSig.genTestUserSig(mesresult.getUsreid()));

            TUIKit.login(mesresult.getUsreid(), mesresult.getUserSig(), new IUIKitCallBack() {
                @Override
                public void onError(String module, final int code, final String desc) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoadings();
                            Toashow.show(getString(R.string.tm84));
                        }
                    });
                }

                @Override
                public void onSuccess(Object data) {
                    activity_sesemys.user_save_update_Profile(mesresult);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoadings();
                            //进入主页
                            MainActivity.setAction(context);
                        }
                    }, 1000);
                }
            });
        }
    };

    /**
     * 提交注册
     */
    private void wxreg() {
        if (sex == 0) {
            Toashow.show(getString(R.string.tm78));
            return;
        }
        if (TextUtils.isEmpty(user.getText().toString().trim())) {
            Toashow.show(getString(R.string.tm77));
            return;
        }
        username = user.getText().toString().trim();
        qrcode = mycode.getText().toString().trim();

        //向平台提交注册信息
        showdialogLoadings();
        datamodule.openwexin(openId, username, tnickname, theadimgurl, qrcode, sex, mapLocation, Callback);
    }

    /**
     * 点击请求发送验证码
     */
    private void getuserconde() {
        if (sex == 0) {
            Toashow.show(getString(R.string.tm78));
            return;
        }

        if (truecode) {
            KeyboardUtil.hideSoftInput(activity); //隐藏软键盘
            username = user.getText().toString().trim();
            datamodule.postcode1(username, getCallbackcode);
        } else {
            Toashow.show(String.format(getString(R.string.regMessage) + "", timers));
        }
    }

    /**
     * 获取验证码
     */
    private Paymnets getCallbackcode = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onError() {
            Toashow.show(getString(R.string.tv_ss_phome));
        }

        @Override
        public void onSuccess(String msg) {
            mycode.setText(msg);
        }

        @Override
        public void onSuccess(Object o) {
            Mesresult mesresult = (Mesresult) o;
            handlertime.sendEmptyMessageDelayed(Config.sussess, 1000);
        }

        @Override
        public void cancellation() {
            Toashow.show(getString(R.string.eorrfali));
        }
    };

    private void defaultimv() {
        image1.setImageResource(R.mipmap.a44b);
        image2.setImageResource(R.mipmap.a44a);
    }

}
