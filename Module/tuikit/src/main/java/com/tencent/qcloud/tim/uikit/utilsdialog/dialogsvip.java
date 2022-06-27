/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utilsdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tencent.liteav.login.UserModel;
import com.tencent.opensource.AES.Resultinfo;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.base.DialogcallBack;
import com.tencent.qcloud.tim.uikit.utils.Glideloads;
import com.tencent.qcloud.tim.uikit.utils.HttpUtils;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 弹出是否购买VIP服务
 */
public class dialogsvip extends Dialog implements View.OnClickListener {
    private String TAG = dialogsvip.class.getSimpleName();
    UserInfo userInfo;
    TextView tv_close, truename, pcity, send_btn;
    ImageView img_close, icon;
    member member;
    UserModel mSponsorUserModel;
    DialogcallBack dialogcallBack;
    Activity activity;

    public static void getdialogsvip(Context context, UserModel mSponsorUserModel, DialogcallBack dialogcallBack) {
        dialogsvip dialogsvip = new dialogsvip(context, mSponsorUserModel, dialogcallBack);
        dialogsvip.show();
    }

    public dialogsvip(Context context, UserModel mSponsorUserModel, DialogcallBack dialogcallBack) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item_vip_msg);
        userInfo = UserInfo.getInstance();
        this.mSponsorUserModel = mSponsorUserModel;
        this.dialogcallBack = dialogcallBack;
        this.activity = (Activity) context;

        img_close = findViewById(R.id.img_close);
        send_btn = findViewById(R.id.send_btn);
        icon = findViewById(R.id.icon);
        tv_close = findViewById(R.id.tv_close);
        truename = findViewById(R.id.truename);
        pcity = findViewById(R.id.pcity);
        img_close.setOnClickListener(this::onClick);
        tv_close.setOnClickListener(this::onClick);
        send_btn.setOnClickListener(this::onClick);
        String trim = tv_close.getText().toString().trim();
        tv_close.setText(String.format(trim, userInfo.getSex().equals("1") ? "她" : "他"));
        inidate();

    }

    private void inidate() {
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());
        params.put(Constants.TOUSERID, mSponsorUserModel.userId);
        params.put(Constants.TOKEN, userInfo.getToken());
        HttpUtils.RequestPost(com.tencent.qcloud.tim.tuikit.live.BuildConfig.HTTP_API + "/posmember", params, new HttpUtils.HttpListener() {
            @Override
            public void success(String response) {
                getmember(response);
            }

            @Override
            public void onFailed(String message) {
                ToastUtil.toastShortMessage(message);
                dismiss();

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_btn) {
            Intent intent = new Intent();
            intent.setAction("activity_svip");
            getContext().startActivity(intent);
            dismiss();
            return;
        }
        if (id == R.id.img_close) {
            dismiss();
            return;
        }
        if (id == R.id.tv_close) {
            dismiss();
            if (dialogcallBack != null) {
                dialogcallBack.onSuccess();
            }
            return;
        }

    }

    private void getmember(String response) {
        Gson gson = new Gson();
        Mesresult mesresult = gson.fromJson(response, Mesresult.class);
        if (mesresult.isSuccess()) {
            String decrypt = Resultinfo.decrypt(mesresult.getData());
            member = gson.fromJson(decrypt, member.class);
            mSponsorUserModel.province = member.getProvince();
            mSponsorUserModel.city = member.getCity();
        }

        //返回主线程更新UI
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(mSponsorUserModel.userAvatar)) {
                    Glideloads.loadImage(icon, mSponsorUserModel.userAvatar, 6);
                }
                truename.setText(mSponsorUserModel.userName);
                if (!TextUtils.isEmpty(mSponsorUserModel.province)) {
                    pcity.setText(String.format("%s.%s", mSponsorUserModel.province, mSponsorUserModel.city));
                }
            }
        });

    }


}
