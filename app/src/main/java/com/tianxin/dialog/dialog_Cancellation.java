package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

public class dialog_Cancellation extends BaseDialog {

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_Cancellation dialog_cancellation = new dialog_Cancellation(context, paymnets);
        dialog_cancellation.show();

    }

    public dialog_Cancellation(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);

    }

    @Override
    public int getview() {
        return R.layout.dialog_cerper;
    }

    @Override
    @OnClick({R.id.send1,R.id.send2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                if (paymnets != null) {
                    paymnets.onFail();
                }

                break;
            case R.id.send2:
                if (paymnets != null) {
                    paymnets.onSuccess();
                }

                break;
        }
        dismiss();
    }
}
