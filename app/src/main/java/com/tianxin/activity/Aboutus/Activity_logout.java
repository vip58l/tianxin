package com.tianxin.activity.Aboutus;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.Util.log;
import com.tianxin.app.DemoApplication;
import com.tianxin.widget.Backtitle;
import com.tencent.qcloud.tim.uikit.TUIKit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注销帐号
 */
public class Activity_logout extends BasActivity2 {
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    String TAG = Activity_logout.class.getSimpleName();

    public static void starAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Activity_logout.class);
        context.startActivity(intent);
    }
    @Override
    protected int getview() {
        return R.layout.activity_logout;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tv_msg_tm2));
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.buttion, R.id.img_back})
    public void OnClick(View v) {
        DeLeteallback();
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(TAG, "onBackPressed: ");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_MENU) {
            log.d("KEYCODE_MENU键");
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            log.d("KeyEvent.KEYCODE_BACK键");
            DeLeteallback();
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            log.d("KeyEvent.KEYCODE_HOME键");
        }
        return super.onKeyDown(keyCode, event);
    }


    private void DeLeteallback() {
        Config.DeLeteall(DemoApplication.instance());
        Config.DeLeteUserinfo(DemoApplication.instance());
        BaseActivity.logout(DemoApplication.instance());
        Config.DeLeteUserinfo(DemoApplication.instance(), "per_user_model");
        TUIKit.unInit();//释放一些资源等，一般可以在退出登录时调用
    }
}