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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_delete_video extends BottomSheetDialog {

    @BindView(R.id.toptext)
    TextView toptext;

    private final Paymnets paymnet;
    private boolean top;

    public dialog_delete_video(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_video1);
        this.paymnet = paymnets;
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        show();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public dialog_delete_video(@NonNull Context context, boolean b, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_video1);
        this.paymnet = paymnets;
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        show();
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        this.top = b;
        toptext.setText(b ? context.getString(R.string.top1) + "" : context.getString(R.string.top2) + "");
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout6, R.id.layout3})
    public void OnClick(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.layout1:
                if (paymnet != null) {
                    paymnet.onSuccess();
                }

                break;
            case R.id.layout2:
                if (paymnet != null) {
                    paymnet.onRefresh();
                }

                break;
            case R.id.layout3:
                if (paymnet != null) {
                    paymnet.onLoadMore();
                }

                break;
            case R.id.layout6:
                if (paymnet != null) {
                    paymnet.fall(top ? 0 : 1);
                }

                break;

        }
    }

    public static dialog_delete_video video(Context context, int top, Paymnets paymnets) {
        return new dialog_delete_video(context, top == 0 ? false : true, paymnets);
    }
}
