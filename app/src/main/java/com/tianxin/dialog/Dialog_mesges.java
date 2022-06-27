package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

import butterknife.BindView;
import butterknife.OnClick;

public class Dialog_mesges extends BaseDialog {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.kankan)
    TextView kankan;
    @BindView(R.id.exit)
    TextView exit;


    public Dialog_mesges(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            GlideEngine.loadImage(icon, userInfo.getAvatar());
        }
        if (!TextUtils.isEmpty(userInfo.getName())) {
            tv_name.setText(TextUtils.isEmpty(userInfo.getGivenname()) ? userInfo.getName() : userInfo.getGivenname());
        }
        String sexname = userInfo.getSex().equals("1") ? "哥哥" : "姐姐";
        tv1.setText(String.format(tv1.getText().toString(), sexname));
    }

    @Override
    public int getview() {
        return R.layout.dialog_mesgs;
    }

    @OnClick({R.id.kankan, R.id.exit, R.id.colas})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.kankan:
            case R.id.colas:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }

                break;
            case R.id.exit:
                if (paymnets != null) {
                    paymnets.onError();
                }

                break;
        }
        dismiss();
    }

    public void setIcon(String icon) {
        Glide.with(getContext()).load(icon).into(this.icon);
    }

    public void setIcon(int icon) {
        Glide.with(getContext()).load(icon).into(this.icon);
    }

    public void setTv_name(String tv_name) {
        this.tv_name.setText(tv_name);
    }

    public void setTv1(String tv1) {
        this.tv1.setText(tv1);
    }

    public void setTv2(String tv2) {
        this.tv2.setText(tv2);
    }

    public void setKankan(String kankan) {
        this.kankan.setText(kankan);
    }

    public void setExit(String exit) {
        this.exit.setText(exit);
    }

    public static Dialog_mesges Dialogs(Context context, Paymnets paymnets) {
        Dialog_mesges dialog_mesges = new Dialog_mesges(context, paymnets);
        dialog_mesges.setCancelable(false);
        dialog_mesges.show();
        return dialog_mesges;
    }
}
