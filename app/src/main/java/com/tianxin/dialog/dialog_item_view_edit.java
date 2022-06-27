package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_item_view_edit extends BottomSheetDialog {
    @BindView(R.id.editview)
    EditText editview;
    Paymnets paymnets;

    public void setEditview(String msg) {
        editview.setText(msg);
    }

    public dialog_item_view_edit(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item_view_edit);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        ButterKnife.bind(this);
        editview.setText(null);

    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public static void myedit(Context context, String msg, Paymnets paymnets) {
        dialog_item_view_edit dialogItemViewEdit = new dialog_item_view_edit(context);
        dialogItemViewEdit.setPaymnets(paymnets);
        dialogItemViewEdit.setEditview(msg);
        dialogItemViewEdit.show();
    }

    @OnClick({R.id.senbnt, R.id.close})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.senbnt:
                paymnets.activity(editview.getText().toString());
                dismiss();
                break;
            case R.id.close:
                dismiss();
                break;
        }
    }
}
