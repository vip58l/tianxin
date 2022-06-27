package com.tianxin.activity.register;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.tencent.opensource.model.Mesresult;
import com.tencent.qcloud.costransferpractice.common.base.BaseModeul;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.reguserinfo;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.sesemys.activity_sesemys;
import com.tianxin.dialog.dialog_upgrade;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_sex;
import com.tianxin.widget.Backtitle;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册性别导航页
 */
public class activity_register extends BasActivity2 {
    private static final String TAG = activity_register.class.getName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.sex1)
    ImageView sex1;
    @BindView(R.id.sex2)
    ImageView sex2;

    @Override
    protected int getview() {
        return R.layout.activity_register;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tv_reg_msg1));
    }

    @Override
    public void initData() {
        if (ActivityLocation.checkpermissions(activity)) {
            lbsamap.getmyLocation(callback);
        }
    }

    @OnClick({R.id.sex1, R.id.sex2, R.id.login})
    public void OnClick(View v) {
        ImageResource();
        switch (v.getId()) {
            case R.id.sex1:
                sex = 1;
                sex1.setImageResource(R.mipmap.ic_man_choose);
                dialog_sex.dialogsex(context, sex, new Paymnets() {
                    @Override
                    public void onRefresh() {

                    }

                    @Override
                    public void onLoadMore() {
                        //男性自动注册
                        PostReg(sex);
                    }
                });
                break;
            case R.id.sex2:
                sex = 2;
                sex2.setImageResource(R.mipmap.icon_woman_choose);
                dialog_sex.dialogsex(context, sex, new Paymnets() {
                    @Override
                    public void onRefresh() {

                    }

                    @Override
                    public void onLoadMore() {
                        //女性自动注册
                        PostReg(sex);
                    }
                });
                break;
            case R.id.login:
                startLogin2();
                break;

        }
    }

    @Override
    public void OnEorr() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess && data != null) {
            setResult(resultCode, data);
            finish();
        }

    }

    protected void ImageResource() {
        sex1.setImageResource(R.mipmap.icon_man);
        sex2.setImageResource(R.mipmap.icon_woman);
    }

    /**
     * 会员注册
     *
     * @param gender
     */
    public void PostReg(int gender) {
        KeyboardUtil.hideSoftInput(activity);
        reguserinfo info = new reguserinfo();
        info.setTruename("");
        info.setUsername("");
        info.setPassword("");
        info.setPassword2("");
        info.setParent("");
        info.setWx("");
        info.setQq("");
        info.setCode("");
        info.setModel(SystemUtil.showlog(context));
        info.setSex(gender);
        showdialogLoadings();
        datamodule.RegUser2(info, mapLocation, new Paymnets() {

            @Override
            public void onSuccess(String msg) {
                Toashow.show(msg);
                dialogLoadings();
            }

            @Override
            public void onSuccess(Object object) {
                mesresult = (Mesresult) object;
                BaseModeul.clearClipboard(context); //清空剪切板
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //注册成功
                        dialogLoading.setLoadingtext(mesresult.getMsg());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialogLoadings();
                                //本地加签名
                                //loginIm(mesresult.getUsreid(), GenerateTestUserSig.genTestUserSig(mesresult.getUsreid()));
                                //登录腾讯IM
                                TUIKit.login(mesresult.getUsreid(), mesresult.getUserSig(), new IUIKitCallBack() {
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
                                        //进入主页
                                        activity_sesemys.user_save_update_Profile(mesresult);
                                        MainActivity.setAction(context);
                                    }
                                });
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
        });
    }

}