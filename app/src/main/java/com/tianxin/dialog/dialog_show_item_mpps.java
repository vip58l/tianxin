package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

public class dialog_show_item_mpps extends BaseDialog {

    public static dialog_show_item_mpps myshow(Context context, Paymnets paymnets) {
        dialog_show_item_mpps dialog_show_item_mpps = new dialog_show_item_mpps(context, paymnets);
        dialog_show_item_mpps.show();
        return dialog_show_item_mpps;
    }

    public dialog_show_item_mpps(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    @Override
    public int getview() {
        return R.layout.dialog_show_item;
    }

    @OnClick({R.id.send1, R.id.send2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                paymnets.onSuccess();
                break;
            case R.id.send2:
                paymnets.onError();
                break;

        }
        dismiss();
    }
}
