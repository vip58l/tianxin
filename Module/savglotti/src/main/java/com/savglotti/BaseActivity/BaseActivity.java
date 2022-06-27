package com.savglotti.BaseActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    public Activity activity;
    public Context context;
    public Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(contentview());
        bind = ButterKnife.bind(this);
        activity = this;
        context = this;
        iniview();
        inidate();
    }

    protected abstract void iniview();

    protected abstract void inidate();

    protected abstract int contentview();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

}
