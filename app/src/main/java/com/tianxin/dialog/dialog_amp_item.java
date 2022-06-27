/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/16 0016
 */


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

public class dialog_amp_item extends Dialog {
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    Paymnets paymnets;

    public dialog_amp_item(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.amp_item_dialog);
        this.paymnets = paymnets;
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        show();


    }

    public static dialog_amp_item dialogampitem(Context context, Paymnets paymnets) {
        return new dialog_amp_item(context, paymnets);
    }

    @OnClick({R.id.btnDel})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.btnDel:
                paymnets.onSuccess();
                break;
        }
    }

}
