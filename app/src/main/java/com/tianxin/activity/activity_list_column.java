package com.tianxin.activity;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Fragment.fragment.fragmnet_coulmn;
import com.tianxin.Util.Constants;

/**
 * 课程列有
 */
public class activity_list_column extends BasActivity2 {
    Fragment chatFragment = new fragmnet_coulmn();

    @Override
    protected int getview() {
        return R.layout.activity_chat;
    }

    @Override
    public void iniview() {
        Bundle args = new Bundle();
        args.putInt(Constants.TYPE,1);
        chatFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.empty_view, chatFragment).commitAllowingStateLoss();
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }
}