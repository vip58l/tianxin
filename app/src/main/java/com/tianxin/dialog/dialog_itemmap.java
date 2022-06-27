package com.tianxin.dialog;


import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BasettfDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.OnClick;

/**
 * 打开导航功能
 */
public class dialog_itemmap extends BasettfDialog {

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_itemmap dialog_bottion = new dialog_itemmap(context, paymnets);
        dialog_bottion.show();
    }

    public dialog_itemmap(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
    }

    @Override
    public int getview() {
        return R.layout.dialog_item_map;
    }

    @Override
    @OnClick({R.id.send1, R.id.send2, R.id.send3, R.id.send4, R.id.image})
    public void OnClick(View v) {

        switch (v.getId()) {
            case R.id.send1:
                paymnets.status(1);
                break;
            case R.id.send2:
                paymnets.status(2);
                break;
            case R.id.send3:
                paymnets.status(3);
                break;
            case R.id.send4:
            case R.id.image:
                dismiss();
                break;
        }

    }

}
