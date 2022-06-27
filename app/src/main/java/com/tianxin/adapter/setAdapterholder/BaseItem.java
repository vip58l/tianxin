package com.tianxin.adapter.setAdapterholder;

import android.view.View;

import butterknife.ButterKnife;

public abstract class BaseItem {
    public BaseItem(View itemview) {
        ButterKnife.bind(this, itemview);
    }
    public abstract void bind(Object object);
}
