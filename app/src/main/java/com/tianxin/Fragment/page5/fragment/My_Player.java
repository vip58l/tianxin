/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/23 0023
 */


package com.tianxin.Fragment.page5.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.widget.DyVideoPlay;

import butterknife.BindView;

public class My_Player extends BasFragment {
    @BindView(R.id.dyvideopaly)
    DyVideoPlay dyvideopaly;
    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_frome, null);
    }


    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dyvideopaly.stopPlayback();
    }


}
