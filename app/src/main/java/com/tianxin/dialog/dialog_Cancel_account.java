package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

public class dialog_Cancel_account extends BaseDialog {

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_Cancel_account dialog_cancel_account = new dialog_Cancel_account(context, paymnets);
        dialog_cancel_account.show();
    }

    public dialog_Cancel_account(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    @Override
    public int getview() {
        return R.layout.dialog_cancellation;
    }

    @Override
    @OnClick({R.id.send1, R.id.send2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                datamodule.getlogout(paymnets);
                break;
            case R.id.send2:
                break;
        }
        dismiss();
    }

}
