package com.tencent.liteav.trtcvoiceroom.ui.widget;

import android.content.Context;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tencent.liteav.trtcvoiceroom.R;
import com.tencent.opensource.model.UserInfo;

public class BaseBottomSheetDialog extends BottomSheetDialog {
    public UserInfo userInfo;
    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        userInfo = UserInfo.getInstance();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public BaseBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, R.style.fei_style_dialog);
        userInfo = UserInfo.getInstance();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    protected BaseBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
