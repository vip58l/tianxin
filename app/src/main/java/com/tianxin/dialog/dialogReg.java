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

public class dialogReg extends Dialog {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.kankan)
    TextView kankan;
    @BindView(R.id.exit)
    TextView exit;
    @BindView(R.id.content)
    ImageView content;
    public Paymnets paymnets;

    public dialogReg(Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialogreg);
        this.paymnets = paymnets;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.exit, R.id.kankan})
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

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setKankan(String kankan) {
        this.kankan.setText(kankan);
    }

    public void setExit(String exit) {
        this.exit.setText(exit);
    }

    public void setContent(String content) {
        Glide.with(getContext()).load(content).into(this.content);
    }

    public void setContent(int content) {
        Glide.with(getContext()).load(content).into(this.content);
    }

    public static void getdialogReg(Context context, int Sex, Paymnets paymnets) {
        dialogReg dialogReg = new dialogReg(context, paymnets);
        dialogReg.setTitle("性别选定后将不可修改你确定吗？");
        dialogReg.setContent(Sex == 1 ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose);
        dialogReg.show();
    }
}
