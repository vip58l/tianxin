package com.tianxin.activity.ZYservices;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.serviceaccount;
import com.tianxin.Module.api.servicetitle;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.Dialog_Loading;
import com.tianxin.getHandler.PostModule;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class activity_serviceaccount extends BasActivity2 {
    private static final String TAG = "activity_serviceaccount";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.edittext)
    EditText edittext;
    servicetitle servicetitle;
    boolean isbolean = false;
    private activity_serviceaccount activity;

    @Override
    protected int getview() {
        return R.layout.activity_serviceaccount;
    }

    @Override
    public void iniview() {
        activity = this;
        userInfo = UserInfo.getInstance();
        String stringExtra = getIntent().getStringExtra(Constants.ACCOUNT);
        servicetitle = new Gson().fromJson(stringExtra.replace("servicetitle", ""), servicetitle.class);
        itemback.righttext.setText(R.string.btn_ok);
        itemback.righttext.setTextColor(getResources().getColor(R.color.homeback));
        itemback.tvback.setTextColor(getResources().getColor(R.color.homeback));
        itemback.righttext.setTextSize(18);
        itemback.tvback.setTextSize(18);
        if (servicetitle != null) {
            title.setText(servicetitle.getTitle());
        }
        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isbolean = true;
            }
        });
    }

    @Override
    public void initData() {
        if (servicetitle == null || userInfo == null) {
            return;
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
    PostModule.getModule(BuildConfig.HTTP_API + "/serviceafind?userid=" + userInfo.getUserId() + "&typetitle=" + servicetitle.getId()+"&token="+userInfo.getToken(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    serviceaccount serviceaccount = new Gson().fromJson(date, serviceaccount.class);
                    edittext.setText(serviceaccount.getContent());
                    isbolean = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    public void initDatat() {
        String content = edittext.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toashow.show(getString(R.string.Toas_msg1));
            edittext.setFocusable(true);
            edittext.setFocusableInTouchMode(true);
            edittext.requestFocus();
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo == null || servicetitle == null) {
            Toashow.show("获取数居有误");
            return;
        }
        Dialog_Loading dialogLoading = Dialog_Loading.dialogLoading(this, getString(R.string.dialog_msg1));
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("content", content)
                .add("type", String.valueOf(servicetitle.getType()))
                .add("typetitle", String.valueOf(servicetitle.getId()))
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(BuildConfig.HTTP_API + "/serviceaccountadd", requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);

                    if (mesresult.getStatus().equals("1")) {
                        //Toashow.show(getResources().getString(R.string.serview));
                        edittext.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialogLoading.dismiss();
                                Toashow.show(mesresult.getMsg());
                                finish();
                            }
                        }, 1000);
                    } else {
                        dialogLoading.dismiss();
                        Toashow.show(mesresult.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    dialogLoading.dismiss();
                }


            }

            @Override
            public void fall(int code) {
                dialogLoading.dismiss();
            }
        });
    }

    @OnClick({R.id.tv3title, R.id.layoutback})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv3title:
                initDatat();
                break;
            case R.id.layoutback:
                if (isbolean && !TextUtils.isEmpty(edittext.getText().toString().trim())) {
                    staralertDialog();
                } else {
                    finish();
                }
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isbolean && keyCode == KeyEvent.KEYCODE_BACK && !TextUtils.isEmpty(edittext.getText().toString().trim())) {
            staralertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    /**
     * 弹窗提示消息
     */
    public void staralertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.alertDialog_title);
        alertDialog.setMessage(R.string.dialog_setMessage);
        alertDialog.setCancelable(true);
        alertDialog.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setNegativeButton(R.string.AlertDialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //监听事件
        AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Log.e(TAG, "对话框显示了");
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e(TAG, "对话框消失了");
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return false;
            }
        });
        dialog.show();


    }
}