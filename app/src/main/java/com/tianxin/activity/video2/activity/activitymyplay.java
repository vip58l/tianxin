package com.tianxin.activity.video2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Config;
import com.tianxin.activity.video2.fragment.framuntmyPage;
import com.tencent.opensource.model.videolist;

import java.io.Serializable;
import java.util.List;

import butterknife.OnClick;

/**
 * 加载1Fragment内容
 */
public class activitymyplay extends BasActivity2 {
    static String TAG = activitymyplay.class.getSimpleName();

    public static void starsetAction(Context context, List<videolist> mlist, int POSITION, int totalPage, int TYPE) {
        Intent intent = new Intent(context, activitymyplay.class);
        intent.putExtra(Constants.POSITION, POSITION);
        intent.putExtra(Constants.JSON, (Serializable) mlist);
        intent.putExtra(Constants.TOTALPAGE, totalPage);
        intent.putExtra(Constants.TYPE, TYPE);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.mSystemUiVisibility(activity, true);
        Config.AsetctivityBLACK(activity);
        return R.layout.activitymyplay;
    }

    @Override
    public void iniview() {
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void initData() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.POSITION, getIntent().getIntExtra(Constants.POSITION, 0));
        bundle.putInt(Constants.TYPE, getIntent().getIntExtra(Constants.TYPE, 0));
        bundle.putInt(Constants.TOTALPAGE, getIntent().getIntExtra(Constants.TOTALPAGE, 0));
        bundle.putSerializable(Constants.JSON, getIntent().getSerializableExtra(Constants.JSON));
        Fragment fragment = framuntmyPage.show(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    @OnClick(R.id.back)
    public void OnClick(View v) {
        finish();
    }

    @Override
    public void OnEorr() {

    }

}
