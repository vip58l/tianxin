/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/12 0012
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class dialog_item_rs extends Dialog {
    private final Paymnets paymnets;
    @BindView(R.id.layout1)
    LinearLayout layout1;
    @BindView(R.id.layout2)
    LinearLayout layout2;
    @BindView(R.id.layout3)
    LinearLayout layout3;
    @BindView(R.id.layout4)
    LinearLayout layout4;
    @BindView(R.id.layout5)
    LinearLayout layout5;
    @BindView(R.id.layout6)
    LinearLayout layout6;
    @BindView(R.id.layout7)
    LinearLayout layout7;

    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;
    @BindView(R.id.tv4)
    TextView tv4;
    @BindView(R.id.tv5)
    TextView tv5;

    private final Unbinder bind;

    public void Fmessage(int i) {
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
        layout3.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);

        if (i == View.VISIBLE) {
            layout3.setVisibility(View.VISIBLE);
            tv3.setText(getContext().getString(R.string.tv_msg78));
        }
    }

    public void sethideshow(int vi) {
        layout2.setVisibility(vi);
    }

    public void sethide() {
        layout3.setVisibility(View.GONE);
        layout4.setVisibility(View.GONE);
        layout5.setVisibility(View.GONE);
        layout6.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);
    }

    public void sethide(int s) {
        layout6.setVisibility(View.GONE);
        layout7.setVisibility(View.GONE);
    }


    public dialog_item_rs(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item_rs);
        this.paymnets = paymnets;
        bind = ButterKnife.bind(this);

        //设置宽高
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public static dialog_item_rs dialogitemrs(Context context, Paymnets paymnets) {
        dialog_item_rs dialogItemRs = new dialog_item_rs(context, paymnets);
        dialogItemRs.show();
        return dialogItemRs;
    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.layout6, R.id.layout7})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                paymnets.status(1);
                break;
            case R.id.layout2:
                paymnets.status(2);
                break;
            case R.id.layout3:
                paymnets.status(3);
                break;
            case R.id.layout4:
                paymnets.status(4);
                break;
            case R.id.layout5:
                paymnets.status(5);
                break;
            case R.id.layout6:
                paymnets.status(6);
                break;
            case R.id.layout7:
                paymnets.status(7);
                break;

        }
        dismiss();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (bind != null) {
            bind.unbind();
        }
    }
}
