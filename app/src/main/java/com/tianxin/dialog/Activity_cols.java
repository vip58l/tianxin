package com.tianxin.dialog;

import android.app.Activity;
import android.view.View;

/**
 * 关闭Activity
 */
public class Activity_cols implements View.OnClickListener {

    private final Activity activity;
    public Activity_cols(Activity activity) {
        this.activity=activity;
    }

    @Override
    public void onClick(View v) {
        activity.finish();

    }

}
