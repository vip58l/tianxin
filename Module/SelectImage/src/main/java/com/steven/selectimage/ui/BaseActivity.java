package com.steven.selectimage.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {
    public Activity activity;
    public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        activity = this;
        context = this;
        init();
    }

    protected abstract int getLayoutId();

    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
