package com.tianxin.activity.register;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 找回密码
 */
public class activity_rpwd extends BasActivity2 {
    String TAG = activity_rpwd.class.getName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.password2)
    EditText password2;
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
    @BindView(R.id.smallLabel)
    LinearLayout smallLabel;
    @BindView(R.id.passwnewwd)
    com.tianxin.widget.passwnewwd passwnewwd;
    Handler handlertime = new Handlertime();

    public static void starAction(Activity context) {
        Intent intent = new Intent(context, activity_rpwd.class);
        context.startActivityForResult(intent, Constants.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_rpwd;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.ts_tv_mc));
        editshow0.setVisibility(View.GONE);
        editshow1.setVisibility(View.GONE);
        editshow2.setVisibility(View.GONE);
        username.addTextChangedListener(new MyTextWatcher(editshow0));
        password.addTextChangedListener(new MyTextWatcher(editshow1));
        password2.addTextChangedListener(new MyTextWatcher(editshow2));

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.regs, R.id.codemsg, R.id.editshow0, R.id.editshow1, R.id.editshow2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.regs:
                //找回密码
                datamodule.Retrievepassword(username.getText().toString(), mycode.getText().toString(), regspwd);
                break;
            case R.id.codemsg:
                //时间倒计60秒
                if (truecode) {
                    KeyboardUtil.hideSoftInput(activity); //隐藏软键盘
                    datamodule.postcode(username.getText().toString(), getcode);
                } else {
                    Toashow.show(String.format(getString(R.string.regMessage) + "", timers));
                }
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
        }

    }

    @Override
    public void OnEorr() {

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

    /**
     * 找回密码
     */
    private Paymnets regspwd = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onError() {
            Toashow.show(getString(R.string.tv_ss_phome));
        }

        @Override
        public void onClick() {
            Toashow.show(getString(R.string.tv_ss_a3));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void cancellation() {
            Toashow.show(getString(R.string.eorrfali));
        }

        @Override
        public void onSuccess(Object object) {
            mesresult = (Mesresult) object;
            //显示找回密码面板
            passwnewwd.setVisibility(View.VISIBLE);
            passwnewwd.setinfo(username.getText().toString(), mesresult.getToken(), callpwd);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }
    };

    /**
     * 获取验证码
     */
    private Paymnets getcode = new Paymnets() {
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
        public void onSuccess() {
            handlertime.sendEmptyMessageDelayed(Config.sussess, 1000);
        }

        @Override
        public void cancellation() {
            Toashow.show(getString(R.string.eorrfali));
        }
    };

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
                        codemsg.setText(String.format("%s秒", timers));
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

    /**
     * 悠改密码
     */
    private Paymnets callpwd = new Paymnets() {
        @Override
        public void onSuccess() {
            handlertime.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.username, passwnewwd.getUsername());
                    intent.putExtra(Constants.password, passwnewwd.getPassword().trim());
                    setResult(Constants.sussess, intent);
                    finish();
                }
            }, 1000);
        }

        @Override
        public void onSuccess(String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    Toashow.show(msg);
                }
            });
        }

        @Override
        public void isNetworkAvailable() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    Toashow.show(getString(R.string.eorrfali2));
                }
            });

        }

        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    Toashow.show(getString(R.string.eorrfali3));
                }
            });
        }

        @Override
        public void fall(int code) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    switch (code) {
                        case 1:
                            Toashow.show(getString(R.string.input_number));
                            break;
                        case 2:
                            Toashow.show(getString(R.string.tvpawwroid));
                            break;
                        case 3:
                            Toashow.show(getString(R.string.tvpassword3));
                            break;
                        case 4:
                            Toashow.show(getString(R.string.tvpassword4));
                            break;
                        case 5:
                            Toashow.show(getString(R.string.onfall));
                            break;
                    }
                }
            });

        }

        @Override
        public void dismiss() {
            showDialog();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handlertime.removeCallbacksAndMessages(null);
    }
}
