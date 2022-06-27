package com.savglotti.activity;

import android.content.Context;
import android.content.Intent;

import com.airbnb.lottie.LottieAnimationView;
import com.savglotti.BaseActivity.BaseActivity;
import com.savglotti.R;

public class ActivityLoggi extends BaseActivity {
    LottieAnimationView animation_view1;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ActivityLoggi.class);
        context.startActivity(intent);
    }

    @Override
    protected int contentview() {
        return R.layout.activity_loggi;
    }

    @Override
    protected void iniview() {
        animation_view1 = findViewById(R.id.animation_view1);

    }

    @Override
    protected void inidate() {

    }


}