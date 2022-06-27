package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_item_img extends BaseDialog {
    @BindView(R.id.title)
    TextView title;

    public dialog_item_img(Context context, int count, Paymnets paymnet) {
        super(context, paymnet);

        title.setText(String.format(title.getText().toString(), count));
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    public int getview() {
        return R.layout.dialog_item_img;
    }

    @OnClick({R.id.goosvip})
    public void OnClick(View v) {
        if (paymnets!=null){
            paymnets.onClick();
        }

        dismiss();
    }

    public static void dialogitemvip(Context context, int count, Paymnets paymnet) {
        dialog_item_img dialog_item_img = new dialog_item_img(context, count, paymnet);
        dialog_item_img.setCanceledOnTouchOutside(false);
        dialog_item_img.show();
    }

}
