/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/24 0024
 */


package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_delete_img extends BottomSheetDialog {
    private final Paymnets paymnet;

    public dialog_delete_img(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_images);
        this.paymnet = paymnets;
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        show();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3})
    public void OnClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.layout1:
                paymnet.onSuccess();
                break;
            case R.id.layout2:
                paymnet.onRefresh();
                break;
            case R.id.layout3:
                paymnet.onLoadMore();
                break;

        }
    }

    public static dialog_delete_img dialogdeleteimg(Context context, Paymnets paymnets) {
        return new dialog_delete_img(context, paymnets);
    }
}
