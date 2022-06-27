package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tianxin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_paymnet extends Dialog {

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;


    public dialog_paymnet(Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_paymnet);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.no, R.id.yes})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.no:
                dismiss();
                break;
            case R.id.yes:
                cancel();
                break;

        }

    }

}
