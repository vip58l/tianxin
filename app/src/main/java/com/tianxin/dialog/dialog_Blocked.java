package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

/**
 * 帐户封禁通知
 */
public class dialog_Blocked extends BaseDialog {

    public dialog_Blocked(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_Blocked dialog_Blocked = new dialog_Blocked(context, paymnets);
        dialog_Blocked.setCancelable(false);
        dialog_Blocked.show();
    }

    public static void myshow(Context context) {
        dialog_Blocked dialog_Blocked = new dialog_Blocked(context, null);
        dialog_Blocked.setCancelable(false);
        dialog_Blocked.show();
    }

    @Override
    public int getview() {
        return R.layout.dialogsealprompt;
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
