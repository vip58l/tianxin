/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/18 0018
 */


package com.tianxin.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_msg_svip extends BaseDialog {
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.send11)
    TextView send1;
    @BindView(R.id.send22)
    TextView send2;

    public static void dialogmsgsvip(Context context, String conunttitle, String send1, String send2, Paymnets payments) {
        dialog_msg_svip dialogMsgSvip = new dialog_msg_svip(context, conunttitle, send1, send2, payments);
        dialogMsgSvip.show();
    }

    public dialog_msg_svip(@NonNull Context context, String msg, String msg1, String msg2, Paymnets payments) {
        super(context, payments);
        tv2.setText(msg);
        send1.setText(msg1);
        send2.setText(msg2);
    }

    @Override
    public int getview() {
        return R.layout.dialogshow;
    }

    @OnClick({R.id.send11, R.id.send22})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.send11:
                if (paymnets != null) {
                    paymnets.onRefresh();
                }
                break;
            case R.id.send22:
                if (paymnets != null) {
                    {
                        paymnets.onSuccess();
                    }
                    break;
                }

        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (paymnets != null) {
            paymnets.dismiss();
        }
    }
}
