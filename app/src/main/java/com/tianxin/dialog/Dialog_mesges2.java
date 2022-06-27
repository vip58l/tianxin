package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dialog_mesges2 extends Dialog {
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.kankan)
    TextView kankan;
    @BindView(R.id.exit)
    TextView exit;
    public Paymnets paymnets;

    public Dialog_mesges2(Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_mesgs2);
        ButterKnife.bind(this);
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @OnClick({R.id.kankan, R.id.exit, R.id.colas})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.kankan:
                paymnets.onSuccess();
                break;
            case R.id.exit:
                paymnets.onError();
                break;

        }
        dismiss();
    }

    public void setIcon(String icon) {
        Glide.with(getContext()).load(icon).into(this.icon);
    }
    public void setIcon(int icon) {
        Glide.with(getContext()).load(icon).into(this.icon);
    }
    public void setTv_name(String tv_name) {
        this.tv_name.setText(tv_name);
    }

    public void setTv1(String tv1) {
        this.tv1.setText(tv1);
    }

    public void setTv2(String tv2) {
        this.tv2.setText(tv2);
    }

    public void setKankan(String kankan) {
        this.kankan.setText(kankan);
    }

    public void setExit(String exit) {
        this.exit.setText(exit);
    }
}
