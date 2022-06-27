package com.tianxin.Fragment.page3.fragment;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.ViewPager.setViewPager;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 朋友圈动态
 */
public class find extends BasFragment {
    private final String TAG = "three";
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager)
    public ViewPager viewpager;
    @BindView(R.id.SearchActivity)
    ImageView SearchActivity;
    private List<Fragment> list = new ArrayList<>();

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_three, null);
    }

    @OnClick({R.id.SearchActivity, R.id.back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.SearchActivity:
                startActivity(new Intent(context, SearchActivity.class));
                break;
            case R.id.back:
                activity.finish();
                break;
        }
    }

    @Override
    public void iniview() {
        if (BuildConfig.TYPE == 1) {
            SearchActivity.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams layoutParams = SearchActivity.getLayoutParams();
            layoutParams.width = 80;
            layoutParams.height = 80;
            SearchActivity.setLayoutParams(layoutParams);
        }
        if (list.size() == 0) {
            mlist.add("朋友圈动态");
            list.add(page3_1.showfmessage(0, ""));
        }
        viewpager.setAdapter(adapter = new setViewPager(getChildFragmentManager(), list, mlist.toArray(new String[mlist.size()]), setViewPager.three));
        viewpager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        if (list.size() > 0) {
            // 默认选择文字状态
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            Tabtext(tab);
        }
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

    /**
     * 朋友圈动态
     */
    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {

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
    };
}
