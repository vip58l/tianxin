package com.tianxin.activity.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.activity.register.activity_rpwd;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_Cancellation;
import com.tianxin.dialog.dialog_upgrade;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.activity.register.activity_register;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.R;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.Util.DB;
import com.tianxin.Util.Utils;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * 普通手机登录
 */
public class UserLoginActivity extends BasActivity2 {
    @BindView(R.id.loginactivity)
    LoginActivity loginactivity;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.check_login)
    CheckBox check_login;
    public static UserLoginActivity userLoginActivity;
    String TAG = UserLoginActivity.class.getSimpleName();
    private String user_openId, accessToken, refreshToken, scope;


    public static void starAction(Activity context, String username, String password) {
        Intent intent = new Intent();
        intent.setClass(context, UserLoginActivity.class);
        intent.putExtra(Constants.username, username);
        intent.putExtra(Constants.password, password);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        return R.layout.activity_login;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        user_openId = intent.getStringExtra("openId");
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        scope = intent.getStringExtra("scope");
    }

    @Override
    public void iniview() {
        userLoginActivity = this;
        interceptHyperLink(findViewById(R.id.tv_privacy)); //点击转换并打开连接

        //自动注册成功转登录页
        String user = getIntent().getStringExtra(Constants.username);
        String pwd = getIntent().getStringExtra(Constants.password);
        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(pwd)) {
            username.setText(user);
            password.setText(pwd);
        }

    }

    @Override
    public void initData() {
        boolean loginisChecked = userInfo.isLoginisChecked();
        check_login.setChecked(loginisChecked);
        loginactivity.setPaymnets(new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void loginwx() {
                dialogshow(getString(R.string.tm101));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoadings();
                    }
                }, 5000);
            }
        });
    }

    @OnClick({R.id.login, R.id.regs, R.id.regs1, R.id.check_login})
    public void OnClick(View view) {
        boolean checked = check_login.isChecked();
        KeyboardUtil.hideSoftInput(activity);
        switch (view.getId()) {
            case R.id.login:
                //登录
                if (!checked) {
                    ToastUtil.toastLongMessage(context.getString(R.string.tv_msg281));
                } else {
                    login();
                }
                break;
            case R.id.regs:
                //注册
                if (!checked) {
                    ToastUtil.toastLongMessage(context.getString(R.string.tv_msg281));
                } else {
                    startActivityForResult(new Intent(context, activity_register.class), Constants.sussess);
                }
                break;
            case R.id.regs1:
                //找回密码
                activity_rpwd.starAction(activity);
                break;
            case R.id.check_login:
                if (checked) {
                    userInfo.setLoginisChecked(true);
                } else {
                    userInfo.setLoginisChecked(false);
                }
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    /**
     * 登录处理
     */
    public void login() {
        String user = username.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            ToastUtil.toastShortMessage(getString(R.string.tvusername));
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.toastShortMessage(getString(R.string.tvpawwroid));
            return;
        }
        dialogshow();
        datamodule.login(user, pwd, 0, paymnets);
    }

    /**
     * 登录腾讯IM即时通信
     *
     * @param userid
     * @param getusersig
     */
    private void loginIm(final String userid, String getusersig) {
        TUIKit.login(userid, getusersig, new IUIKitCallBack() {
            @Override
            public void onError(String module, final int code, final String desc) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoadings();
                        //登录IM失败提示用户更新APP 用户随时--->跳转-->官网-->更新
                        dialog_upgrade.updada_Msg(context);
                    }
                });
            }

            @Override
            public void onSuccess(Object data) {
                activity_sesemys.user_save_update_Profile(mesresult);
                Loadlogin();
            }
        });
    }

    /**
     * 成功跳转
     */
    private void Loadlogin() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogLoadings();
                DB.saveputString(context, username, password, Config.sussess);
                //进入主页
                MainActivity.setAction(context);
            }
        }, 1000);
    }

    /**
     * 用于跳转隐私协议
     * 拦截超链接
     *
     * @param tv
     */
    private void interceptHyperLink(TextView tv) {
        //添加这句话，否则点击不生效
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
            //NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();
            //spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            // 循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                if (url.indexOf("https://") == 0 || url.indexOf("http://") == 0) {
                    Log.d(TAG, "interceptHyperLink: " + url);
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(context, url);
                    //给链接设置样式等，例如链接处的下划线，字体颜色等，及其单击事件的添加
                    ssb.setSpan(customUrlSpan, spannable.getSpanStart(uri), spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                    //设置文字颜色
                    //ForegroundColorSpan span1 = new ForegroundColorSpan(Color.parseColor("#ff0033"));
                    //ForegroundColorSpan span2 = new ForegroundColorSpan(Color.parseColor("#ff0033"));
                    //ssb.setSpan(span1, 8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    //ssb.setSpan(span2, 13, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb.removeSpan(uri);//解决方法 不然ClickableSpan会无效无法拦截
                }
            }
            tv.setText(ssb);
        }
    }

    /**
     * // 在这里可以做任何自己想要的处理
     */
    public class CustomUrlSpan extends ClickableSpan {
        private final Context context;
        private final String url;

        public CustomUrlSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 在这里可以做任何自己想要的处理
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(Uri.parse(url));
            //context.startActivity(intent);

            int intIndex1 = url.indexOf("abc1");
            int intIndex2 = url.indexOf("abc2");
            int type = 0;
            if (intIndex1 > 0) {
                type = 3;
            }
            if (intIndex2 > 0) {
                type = 4;
            }

            String format = String.format(Webrowse.invitefriends + "?type=%s&userid=%s", type, userInfo.getUserId());
            Intent intent = new Intent(context, DyWebActivity.class);
            intent.putExtra("videoUrl", format);
            startActivity(intent);
        }
    }

    /**
     * 去除超链接的下划线
     */
    public class NoUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess && data != null) {
            username.setText(data.getStringExtra(Constants.username));
            password.setText(data.getStringExtra(Constants.password));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (loginactivity.getVisibility() == View.VISIBLE) {
                loginactivity.setVisibility(View.GONE);
                return true;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 系统请求权限回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utils.REQ_PERMISSION_CODE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.toastLongMessage("未全部授权，部分功能可能无法使用！");
                }
                break;

        }
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     * @Author JPH
     */
    public boolean syncCookie(String url, String cookie) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
        String newCookie = cookieManager.getCookie(url);
        return !TextUtils.isEmpty(newCookie);
    }

    /**
     * 登录平台回调
     */
    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            dialogLoadings();
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(Object object) {
            mesresult = (Mesresult) object;
            //本地加签名
            //loginIm(mesresult.getUsreid(), GenerateTestUserSig.genTestUserSig(mesresult.getUsreid()));
            //服务器签名
            loginIm(mesresult.getUsreid(), mesresult.getUserSig());
        }

        @Override
        public void onError() {
            dialogLoadings();
            dialog_Blocked.myshow(context, null);  //封号提示
        }

        @Override
        public void onFail(String msg) {
            dialogLoadings();
            ToastUtil.toastShortMessage(msg);
        }

        @Override
        public void onFail() {
            dialogLoadings();
            ToastUtil.toastShortMessage(getString(R.string.eorrfali3));
        }

        @Override
        public void onRefresh() {
            //是否撤回注销申请弹窗
            dialogLoadings();
            dialog_Cancellation.myshow(context, dialogpaylog);
        }

    };

    private Paymnets dialogpaylog = new Paymnets() {
        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess() {
            dialogshow();
            String user = username.getText().toString().trim();
            String pwd = password.getText().toString().trim();
            datamodule.login(user, pwd, 1, paymnets);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialogLoadings();
    }
}