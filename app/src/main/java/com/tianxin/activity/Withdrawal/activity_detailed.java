package com.tianxin.activity.Withdrawal;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.widget.itembackTopbr;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

import static com.tianxin.ViewPager.setViewPager.detailed;

/**
 * 收支明细
 */
public class activity_detailed extends BasActivity2 {

    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_detailed.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_detailed;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg151));
        adapter = new setViewPager(getSupportFragmentManager(), getFragment(), detailed);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
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

    public List<Fragment> getFragment() {
        return Arrays.asList(Rechargeorder.pagetype(0), activity_Recharge.pagetype(1), activity_Recharge.pagetype(2));
    }
}