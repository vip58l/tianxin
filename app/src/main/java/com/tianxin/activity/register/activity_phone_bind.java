package com.tianxin.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ConcatAdapter;

import com.tencent.opensource.model.UserInfo;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

/**
 * 绑定手机号
 */
public class activity_phone_bind extends BasActivity2 {
    private EditText username, mycode;
    private TextView codemsg;
    private ImageView editshow0;
    private myHandler myHandler = new myHandler();

    public static void starAction(Activity context) {
        Intent intent = new Intent(context, activity_phone_bind.class);
        context.startActivityForResult(intent, Constants.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_phone_bind;
    }

    @Override
    public void iniview() {
        username = findViewById(R.id.username);
        editshow0 = findViewById(R.id.editshow0);
        mycode = findViewById(R.id.mycode);
        codemsg = findViewById(R.id.codemsg);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editshow0.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });


    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.sendcontext, R.id.codemsg, R.id.editshow0})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sendcontext: {
                //绑定手机号
                String mobile = username.getText().toString().trim();
                String code = mycode.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toashow.toastMessage(getString(R.string.toast03));
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toashow.toastMessage(getString(R.string.toast04));
                    return;
                }
                datamodule.BindPhone(mobile, code, new Paymnets() {
                    @Override
                    public void onSuccess() {
                        UserInfo.getInstance().setMobile(mobile);
                        UserInfo.getInstance().setPhone(mobile);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra(Constants.mobile,mobile);
                                setResult(1000,intent);
                                finish();
                            }
                        }, 1000);
                    }

                    @Override
                    public void isNetworkAvailable() {
                        Toashow.toastMessage(getString(R.string.eorrfali2));
                    }

                    @Override
                    public void onFail() {
                        Toashow.toastMessage(getString(R.string.eorrfali3));
                    }
                });
                break;
            }
            case R.id.codemsg: {
                //获取验证码
                String mobile = username.getText().toString().trim();
                if (TextUtils.isEmpty(mobile)) {
                    Toashow.toastMessage(getString(R.string.toast03));
                    return;
                }

                //时间倒计60秒
                if (truecode) {
                    KeyboardUtil.hideSoftInput(activity); //隐藏软键盘
                    datamodule.postcode1(mobile, new Paymnets() {
                        @Override
                        public void isNetworkAvailable() {
                            Toashow.toastMessage(getString(R.string.eorrfali2));
                        }

                        @Override
                        public void onSuccess(String code) {
                            mycode.setText(code);
                        }

                        @Override
                        public void onSuccess(Object obj) {
                            myHandler.sendEmptyMessage(Config.sussess);
                        }

                    });
                } else {
                    Toashow.show(String.format(getString(R.string.regMessage) + "", timers));
                }

                break;
            }
            case R.id.editshow0:
                username.setText(null);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }


    class myHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            //称除未处理完的消息
            if (hasMessages(Config.sussess)) {
                removeMessages(Config.sussess);
            }
            switch (msg.what) {
                case Config.sussess:
                    timers--;
                    if (timers > 1) {
                        truecode = false;
                        codemsg.setText(String.format("%s秒", timers));
                        sendEmptyMessageDelayed(Config.sussess, 1000);
                    } else {
                        removeCallbacksAndMessages(null);
                        truecode = true;
                        timers = 60;
                        codemsg.setText(R.string.tv_ss_a4);
                    }

                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myHandler != null) {
            myHandler.removeMessages(Config.sussess);
        }
    }
}
