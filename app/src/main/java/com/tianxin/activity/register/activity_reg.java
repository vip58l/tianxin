package com.tianxin.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.SystemUtil;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.RandomName;
import com.tianxin.activity.DouYing.activity_jsonvideo;
import com.tianxin.dialog.dialog_amp_item;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.Module.api.reguserinfo;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.costransferpractice.common.base.BaseModeul;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 会员注册页
 */
public class activity_reg extends BasActivity2 {
    String TAG = activity_reg.class.getName();
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password2)
    EditText password2;
    @BindView(R.id.password3)
    EditText password3;
    @BindView(R.id.wx)
    EditText wx;
    @BindView(R.id.qq)
    EditText qq;
    @BindView(R.id.sex1)
    TextView sex1;
    @BindView(R.id.sex2)
    TextView sex2;
    @BindView(R.id.layout9)
    LinearLayout layout9;
    @BindView(R.id.mycode)
    EditText mycode;
    @BindView(R.id.codemsg)
    TextView codemsg;
    @BindView(R.id.editshow1)
    ImageView editshow1;
    @BindView(R.id.editshow2)
    ImageView editshow2;
    @BindView(R.id.editshow0)
    ImageView editshow0;
    @BindView(R.id.editshow3)
    ImageView editshow3;
    @BindView(R.id.smallLabel)
    LinearLayout smallLabel;

    private myHandler handler = new myHandler();
    private reguserinfo info = new reguserinfo();
    private String[] permissionss;
    private boolean okpermission = false;
    private int gender;

    public static void starsetAction(Activity context, int sex) {
        Intent intent = new Intent(context, activity_reg.class);
        intent.putExtra(Constants.sex, sex);
        context.startActivityForResult(intent, Config.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_reg;
    }

    @Override
    public void iniview() {
        gender = getIntent().getIntExtra(Constants.sex, -1);
        editshow0.setVisibility(View.GONE);
        editshow1.setVisibility(View.GONE);
        editshow2.setVisibility(View.GONE);
        editshow3.setVisibility(View.GONE);
        username.addTextChangedListener(new MyTextWatcher(editshow0));
        password.addTextChangedListener(new MyTextWatcher(editshow1));
        password2.addTextChangedListener(new MyTextWatcher(editshow2));
        password3.addTextChangedListener(new MyTextWatcher(editshow3));

        if (ActivityLocation.checkpermissions(activity)) {
            lbsamap.getmyLocation(callback);
        }

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.regs, R.id.codemsg, R.id.editshow1, R.id.editshow2, R.id.editshow3, R.id.editshow0})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.regs:
                PostReg();   //注册开始
                break;
            case R.id.codemsg:
                KeyboardUtil.hideSoftInput(activity); //隐藏软键盘
                myCodemsg(); //发送验证码
                break;
            case R.id.editshow0:
                username.setText(null);
                break;
            case R.id.editshow1:
                password.setText(null);
                break;
            case R.id.editshow2:
                password2.setText(null);
                break;
            case R.id.editshow3:
                password3.setText(null);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    public void PostReg() {
        KeyboardUtil.hideSoftInput(activity);
        info.setTruename(RandomName.Name());//随机名称
        info.setUsername(username.getText().toString().trim());
        info.setPassword(password.getText().toString().trim());
        info.setPassword2(password2.getText().toString().trim());
        info.setParent(password3.getText().toString().trim()); //注册邀请码
        info.setWx(wx.getText().toString().trim());
        info.setQq(qq.getText().toString().trim());
        info.setCode(mycode.getText().toString().trim());
        info.setModel(SystemUtil.showlog(context));
        info.setSex(gender);

        if (TextUtils.isEmpty(info.getUsername())) {

            Toashow.show(getString(R.string.tvusername));
            return;
        }
        if (TextUtils.isEmpty(info.getPassword())) {
            Toashow.show(getString(R.string.tvpawwroid));

            return;
        }
        if (TextUtils.isEmpty(info.getPassword2())) {

            Toashow.show(getString(R.string.tvpassword3));
            return;
        }
        if (!info.getPassword().equals(info.getPassword2())) {
            Toashow.show(getString(R.string.tvpassword4));
            return;
        }
        if (TextUtils.isEmpty(info.getCode())) {
            Toashow.show(getString(R.string.reg_tvcode1));
            return;
        }
        showdialogLoadings();

        //注册会员
        datamodule.RegUser(info, mapLocation, reguserid);
    }

    /**
     * 发送验证码
     */
    private void myCodemsg() {
        info.setUsername(username.getText().toString().trim());
        info.setPassword(password.getText().toString().trim());
        info.setPassword2(password2.getText().toString().trim());
        info.setWx(wx.getText().toString().trim());
        info.setQq(qq.getText().toString().trim());
        if (TextUtils.isEmpty(info.getUsername())) {
            Toashow.show(getString(R.string.inupphone));
            return;
        }
        if (TextUtils.isEmpty(info.getPassword())) {
            Toashow.show(getString(R.string.tvpawwroid));
            return;
        }
        if (TextUtils.isEmpty(info.getPassword2())) {
            Toashow.show(getString(R.string.tvpassword3));
            return;
        }
        if (!info.getPassword().equals(info.getPassword2())) {
            Toashow.show(getString(R.string.tvpassword4));
            return;
        }

        //时间倒计60秒
        if (truecode) {
            datamodule.postcode1(info, paymnets);
        } else {
            Toashow.show(String.format(getString(R.string.regMessage) + "", timers));
        }
    }

    private class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    timers--;
                    if (timers > 1) {
                        truecode = false;
                        codemsg.setText(String.format(getString(R.string.tv_code_trim) + "", timers));
                        handler.sendEmptyMessageDelayed(Config.sussess, 1000);
                    } else {
                        truecode = true;
                        handler.removeCallbacksAndMessages(null);
                        timers = 60;
                        codemsg.setText(R.string.tv_code_msg);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        this.permissionss = permissions;
        switch (requestCode) {
            case OPEN_SET_REQUEST_CODE:
                for (String permission : permissions) {
                    int STATE = ContextCompat.checkSelfPermission(this, permission);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //获取焦点，自动获取复制粘贴等内容

    }

    /**
     * 获取邀请码
     *
     * @return
     */
    public static String getinviteuid() {
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5
        String shareText = activity_jsonvideo.getShareText();//获取剪切版内容
        try {
            String httpWeb = BuildConfig.HTTP_WEB;
            String url = httpWeb.split("\\.")[0];
            String http = httpWeb.replace(url, "");


            if (!TextUtils.isEmpty(shareText) && shareText.contains(http)) {
                String completeUrl = activity_jsonvideo.getCompleteUrl(shareText);//获取完整的域名
                if (!TextUtils.isEmpty(completeUrl)) {
                    //String[] split = completeUrl.split("\\?");
                    //split = split[1].split("&");
                    String[] split = completeUrl.split("&");
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(split[0]);
                    String inviteuid = m.replaceAll("").trim();//解析邀请码
                    return inviteuid;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return "";
    }

    /**
     * 编辑框输入监听事件
     */
    public class MyTextWatcher implements TextWatcher {
        private final View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            view.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        }
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
            mycode.setText(msg);
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

    /**
     * 注册监听
     */
    private Paymnets reguserid = new Paymnets() {

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
            dialogLoadings();
        }

        @Override
        public void onSuccess(Object object) {
            mesresult = (Mesresult) object;
            Intent intent = new Intent();

            intent.putExtra(Constants.username, info.getUsername());
            intent.putExtra(Constants.password, info.getPassword());
            setResult(Config.sussess, intent);

            BaseModeul.clearClipboard(context); //清空剪切板
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //动态注册成功
                    dialogLoading.setLoadingtext(mesresult.getMsg());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoadings();
                            finish();
                        }
                    }, 1200);
                }
            }, 1000);
        }

        @Override
        public void onFail() {
            dialogLoadings();
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void isNetworkAvailable() {
            dialogLoadings();
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));

        }
    };



}

