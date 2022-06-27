/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/2 0002
 */


package com.tianxin.activity.video.live;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author 刘洋巴金
 * @date 2017-5-3
 * <p>
 * 基类
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    Unbinder bind;
    public Context context;
    public Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        context=this;
        activity=this;
        initBase();
        initView();
        initData();
        initListener();
    }

    /**
     * 设置子类getLayoutId
     */
    public abstract int getLayoutId();

    /**
     * 基类初始化
     */
    public void initBase() {
    }

    /**
     * 子类初始化View
     */
    public abstract void initView();

    /**
     * 子类初始化数据
     */
    public void initData() {
    }

    /**
     * 子类初始化监听
     */
    public void initListener() {
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
