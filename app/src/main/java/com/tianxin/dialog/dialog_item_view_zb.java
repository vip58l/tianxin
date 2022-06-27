package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_item_view_zb extends Dialog {
    Paymnets paymnets;
    @BindView(R.id.mssge)
    TextView mssge;

    public dialog_item_view_zb(@NonNull Context context, Paymnets p) {
        super(context, R.style.fei_style_dialog);
        this.paymnets = p;
        setContentView(R.layout.dialog_item_view_zb);
        ButterKnife.bind(this);
        mssge.setText(String.format(context.getString(R.string.tv_msg135),context.getString(R.string.app_name)));

    }

    @OnClick({R.id.exit, R.id.endroot})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                break;
            case R.id.endroot:
                paymnets.onSuccess();
                break;
        }
        dismiss();
    }


    public static void b(Context context, Paymnets p) {
        dialog_item_view_zb d = new dialog_item_view_zb(context, p);
        d.show();

    }
}
