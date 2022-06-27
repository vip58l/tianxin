package com.tianxin.activity.game;

import androidx.viewpager.widget.ViewPager;

import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.page3.fragment.page3_4;
import com.tianxin.R;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.widget.Backtitle;

import butterknife.BindView;

/**
 * 游戏分类VIEWPAGE
 */
public class Game_aActivity extends BasActivity2 {
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private setViewPager adapager;

    @Override
    protected int getview() {
        return R.layout.activity_game_aactivity;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tm49));
        if (fragments.size() == 0) {
            fragments.add(page3_4.show(1));
            fragments.add(page3_4.show(2));
        }
        adapager = new setViewPager(getSupportFragmentManager(), fragments, setViewPager.tabs5);
        viewPager.setAdapter(adapager);
        tabLayout.setupWithViewPager(viewPager);
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