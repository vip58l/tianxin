package com.tianxin.activity.LatestNews;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.page3.fragment.page3_1;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.widget.itembackTopbr;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 最新动态
 */
public class activity_trend extends BasActivity2 {
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    String TAG = activity_trend.class.getName();
    page3_1 showfmessage;
    List<Fragment> list = new ArrayList<>();


    /**
     * 最新动态
     *
     * @param context
     */
    public static void starsetAction(Context context) {
        context.startActivity(new Intent(context, activity_trend.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_trend;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg160));
        itemback.setsendright(getString(R.string.tv_msg191));
        itemback.sendrightbg();
        viewpager.setAdapter(adapter = new setViewPager(getSupportFragmentManager(), getfragment(), setViewPager.three));
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.sendright})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sendright:
                if (userInfo.getState() >= Constants.TENCENT3) {
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg192));
                    return;
                }
                startActivityForResult(new Intent(context, activity_news.class), Config.sussess);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    private List<Fragment> getfragment() {
        if (list.size() == 0) {
            showfmessage = page3_1.showfmessage(1, "");
            list.add(showfmessage);
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess && resultCode == Config.setResult) {
            showfmessage.loadMoreData();
        }
    }
}