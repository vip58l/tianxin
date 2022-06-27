package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

/**
 * 用户不存在了 找不到用户
 */
public class dialog_del_notice extends BaseDialog {

    public dialog_del_notice(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_del_notice dialogsealprompt = new dialog_del_notice(context, paymnets);
        dialogsealprompt.setCancelable(false);
        dialogsealprompt.show();
    }

    @Override
    public int getview() {
        return R.layout.dialog_del_notice;
    }

    @Override
    @OnClick({R.id.send1})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }
                break;
        }
        dismiss();
    }


}
