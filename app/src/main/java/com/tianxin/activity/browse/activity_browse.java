package com.tianxin.activity.browse;

import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.fragment.browsefragment.Frnetbrowse;
import com.tianxin.R;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.activity_svip;

import java.util.Collections;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_browse extends BasActivity2 {
    private static final String TAG = activity_browse.class.getSimpleName();
    @BindView(R.id.tab)
    TabLayout TAB;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static void setAction(Context context) {
        context.startActivity(new Intent(context, activity_browse.class));
    }

    @Override
    protected int getview() {
        return R.layout.activity_browse;
    }

    @Override
    public void iniview() {
        findViewById(R.id.footer).setVisibility(userInfo.getVip() == 1 ? View.GONE : View.VISIBLE);

        if (fragments.size() == 0) {
            fragments.add(Frnetbrowse.gets(1));
            fragments.add(Frnetbrowse.gets(2));
        }
        Collections.reverse(fragments);
        //Collections.sort(fragments); // 顺序排列
        //Collections.reverse(fragments); // 倒序排列
        //Collections.shuffle(fragments); // 无序

        viewPager.setAdapter(adapter = new setViewPager(getSupportFragmentManager(), fragments, setViewPager.tabs3));
        TAB.setupWithViewPager(viewPager);
        TAB.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
            TabLayout.Tab tab = TAB.getTabAt(0);
            Tabtext(tab);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.back, R.id.footer})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.footer:
                activity_svip.starsetAction(context);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

}