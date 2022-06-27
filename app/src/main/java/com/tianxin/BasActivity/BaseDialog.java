package com.tianxin.BasActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialog extends Dialog {
    public Unbinder bind;
    public Paymnets paymnets;
    public UserInfo userInfo;
    public Datamodule datamodule;
    public Context context;
    public int totalPage;
    public List<Object> list = new ArrayList<>();

    public BaseDialog(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(getview());
        this.context = context;
        this.paymnets = paymnets;
        this.userInfo = UserInfo.getInstance();
        this.datamodule = new Datamodule(context, paymnets);
        this.bind = ButterKnife.bind(this);

    }

    public abstract int getview();

    public abstract void OnClick(View v);

    @Override
    public void dismiss() {
        try {
            super.dismiss();
            if (bind != null) {
                bind.unbind();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
