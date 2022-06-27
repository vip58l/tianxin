package com.tianxin.BasActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BasettfDialog extends BottomSheetDialog {
    public Unbinder bind;
    public Paymnets paymnets;

    public BasettfDialog(@NonNull Context context, Paymnets paymnets1) {
        super(context, R.style.fei_style_dialog);
        setContentView(getview());
        paymnets = paymnets1;
        bind = ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public abstract int getview();

    public abstract void OnClick(View v);

    @Override
    public void dismiss() {
        super.dismiss();
        if (bind != null) {
            bind.unbind();
        }
    }
}

