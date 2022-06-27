package com.tianxin.Test.MVP;

import android.util.Log;

import com.tianxin.Test.MDEMO.Account;
import com.tianxin.Test.MDEMO.Callback;
import com.tianxin.Test.MVC.model;

/**
 * MVP模式
 * 中间层 这种好处是改变或维护不会影响ACTIVITY
 * 有利于ACTIVITY视图层代码维护
 */
public class Persenter {
    private static final String TAG = "Persenter";
    private final model model;
    private Callback mview;

    public Persenter() {
        this.model = new model();
    }

    public void setMview(Callback mview) {
        this.mview = mview;
    }

    public void getdate(String name) {
        Log.d(TAG, "getdate: " + name);

        this.model.getAccountDate(name, new Callback() {
            @Override
            public void onSuccess(Account account) {
                mview.onSuccess(account);
            }

            @Override
            public void onFailed() {
                mview.onFailed();

            }
        });
    }

}
