package com.tianxin.BasActivity;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.Module.Datamodule;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.item;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseBottomSheetDialog extends BottomSheetDialog {
    public final List<item> list = new ArrayList<>();
    public List<com.tianxin.Module.api.moneylist> vlist;
    public boolean ok = false;
    public com.tianxin.adapter.setAdapter setAdapter;
    public Paymnets paymnets;
    public moneylist moneylist;
    public int TYPE = 1;
    public Datamodule datamodule;
    public Context context;
    public Activity activity;
    public Unbinder bind;
    public List<Object> objectList = new ArrayList<>();

    public BaseBottomSheetDialog(@NonNull Context context) {
        super(context, R.style.BottomDialog);
        setContentView(layoutview());
        bind = ButterKnife.bind(this);
        this.context = getContext();
        datamodule = new Datamodule(context, paymnets);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);


    }

    protected abstract int layoutview();

    public abstract void onClick(View view);

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (bind != null) {
            bind.unbind();
        }
    }
}
