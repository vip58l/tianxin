package com.tianxin.dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasettfDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

public class dialog_shert extends BasettfDialog {

    public static void show(Context context, Paymnets paymnets) {
        dialog_shert dialog_shert = new dialog_shert(context, paymnets);
        dialog_shert.show();

    }

    public dialog_shert(@NonNull Context context, Paymnets paymnets1) {
        super(context, paymnets1);

    }

    @Override
    public int getview() {
        return R.layout.dialog_shert;
    }

    @Override
    @OnClick({R.id.wx1, R.id.wx2, R.id.wx3, R.id.wx4,})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.wx1:
                if (paymnets != null) {
                    paymnets.onItemClick(1);
                }
                break;
            case R.id.wx2:
                if (paymnets != null) {
                    paymnets.onItemClick(2);
                }
                break;
            case R.id.wx3:
                if (paymnets != null) {
                    paymnets.onItemClick(3);
                }
                break;
            case R.id.wx4:
                if (paymnets != null) {
                    paymnets.onItemClick(4);
                }
                break;
        }

    }

}
