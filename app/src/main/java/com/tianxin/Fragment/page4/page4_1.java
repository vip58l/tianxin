package com.tianxin.Fragment.page4;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.IMtencent.conversation.ConversationFragment;
import com.tianxin.Fragment.page4.fragment.mssegslist;
import com.tianxin.R;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.ViewPager.setViewPager;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息列表
 */
public class page4_1 extends BasFragment {
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager)
    public ViewPager viewpager;
    @BindView(R.id.SearchActivity)
    public ImageView SearchActivity;
    private List<Fragment> list;
    private final ConversationFragment contactFragment = new ConversationFragment();

    @Override
    public View getview(LayoutInflater inflater) {
        return rootView = inflater.inflate(R.layout.fragment_four, null);
    }

    @OnClick({R.id.SearchActivity})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.SearchActivity:
                startActivity(new Intent(context, SearchActivity.class));
                break;
        }
    }

    @Override
    public void iniview() {
        if (BuildConfig.TYPE == 2) {
            SearchActivity.setVisibility(View.GONE);
        }
        viewpager.setAdapter(adapter = new setViewPager(getChildFragmentManager(), getsetList(), setViewPager.four));
        viewpager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewpager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
        TabLayout.Tab tab = tabLayout.getTabAt(0);
        if (tab != null) {
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

    private List<Fragment> getsetList() {
        if (list == null) {
            list = new ArrayList<>();
            list.add(contactFragment);
            list.add(mssegslist.getfragment(1, "消息"));
            //list.add(mssegslist.getfragment(2, "消息"));
        }

        return list;
    }
}
