/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/27 0027
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_sex extends Dialog {
    @BindView(R.id.tv2)
    TextView tv2;
    Paymnets paymnet;
    int sex;

    public dialog_sex(@NonNull Context context, int sex, Paymnets paymnet) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_sex);
        ButterKnife.bind(this);
        this.paymnet = paymnet;
        this.sex = sex;
        String s = sex == 1 ? "男性" : "女性";
        tv2.setText(String.format(tv2.getText().toString(), s));
        setCanceledOnTouchOutside(false);
        show();
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        //getWindow().setWindowAnimations(R.style.MyDialogStyle2);
    }

    public static dialog_sex dialogsex(Context context, int sex, Paymnets paymnet) {
        return new dialog_sex(context, sex, paymnet);
    }

    @OnClick({R.id.send1, R.id.send2, R.id.cols})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.cols:
                break;
            case R.id.send1:
                paymnet.onRefresh();
                break;
            case R.id.send2:
                paymnet.onLoadMore();
                break;
        }
        dismiss();
    }

}
