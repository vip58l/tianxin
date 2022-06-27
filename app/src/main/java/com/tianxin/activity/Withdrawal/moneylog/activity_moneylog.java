package com.tianxin.activity.Withdrawal.moneylog;

import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.Withdrawal.moneylog.Fromunt.one_money;
import com.tianxin.activity.Withdrawal.moneylog.Fromunt.two_money;

import butterknife.BindView;

public class activity_moneylog extends BasActivity2 {
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private one_money one_money = new one_money();
    private two_money two_money = new two_money();
    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_moneylog.class);
        context.startActivity(intent);
    }
    @Override
    protected int getview() {
        return R.layout.activity_moneylog;
    }

    @Override
    public void iniview() {
        if (fragments.size() == 0) {
            fragments.add(one_money);
            fragments.add(two_money);
        }
        viewPager.setAdapter(new setViewPager(getSupportFragmentManager(), fragments, setViewPager.tabs4));
        tab_layout.setupWithViewPager(viewPager);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Tabtext(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setCustomView(null);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (fragments.size() > 0) {
            TabLayout.Tab tab = tab_layout.getTabAt(0);
            Tabtext(tab);
        }
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