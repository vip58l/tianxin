package com.tianxin.activity.edit;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.Module.api.reguserinfo;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Loading;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * 修改密码
 */
public class activity_edit extends BasActivity2 {
    @BindView(R.id.password1)
    EditText password1;
    @BindView(R.id.password2)
    EditText password2;
    @BindView(R.id.password3)
    EditText password3;
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    Dialog_Loading dialogLoading;
    reguserinfo reg;

    public static void starsetAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_edit.class);
        context.startActivity(intent);
    }
    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.transparent));
        return R.layout.activity_edit;
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        itemback.settitle(getString(R.string.tv_msg111));
        itemback.setHaidtopBackgroundColor(true);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.senbnt})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.senbnt:
                postlogin();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    public void postlogin() {
        String pwd1 = password1.getText().toString().trim();
        String pwd2 = password2.getText().toString().trim();
        String pwd3 = password3.getText().toString().trim();
        reg = new reguserinfo();
        reg.setUsername(userInfo.getPhone());
        reg.setUserid(userInfo.getUserId());
        reg.setPassword(pwd1);
        reg.setPassword2(pwd2);

        if (TextUtils.isEmpty(pwd1)) {
            Toashow.show(getString(R.string.tv_msg106));
            return;
        }
        if (TextUtils.isEmpty(pwd2)) {
            Toashow.show(getString(R.string.tv_msg108));

            return;
        }
        if (TextUtils.isEmpty(pwd3)) {

            Toashow.show(getString(R.string.tv_msg109));
            return;
        }
        if (!pwd2.equals(pwd3)) {
            Toashow.show(getString(R.string.tv_msg110));
            return;
        }

        dialogLoading = new Dialog_Loading(this, getString(R.string.tv_msg107));
        dialogLoading.setCanceledOnTouchOutside(false);
        dialogLoading.show();
        String path = Config.api + "/editpassword";
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", reg.getUserid())
                .add("username", reg.getUsername())
                .add("password", reg.getPassword())
                .add("newpawrd", reg.getPassword2())
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(path, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        dialogLoading.setLoadingtext(mesresult.getMsg());
                        password1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialogLoading.dismiss();
                                BaseActivity.logout(DemoApplication.instance());
                                ToastUtil.toastLongMessage(mesresult.getMsg());
                            }
                        }, 1000);

                    } else {
                        ToastUtil.toastLongMessage(mesresult.getMsg());
                        dialogLoading.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void fall(int code) {
                ToastUtil.toastLongMessage(getString(R.string.onfall));
                dialogLoading.dismiss();
            }
        });
    }

}