package com.tianxin.Fragment.page4;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.IMtencent.conversation.ContactFragment;
import com.tianxin.IMtencent.conversation.ConversationFragment;
import com.tianxin.Fragment.page4.fragment.mssegslist;
import com.tianxin.IMtencent.menu.Menu;
import com.tianxin.R;
import com.tianxin.ViewPager.setViewPager;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息列表
 */
public class page4 extends BasFragment {
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager)
    public ViewPager viewpager;
    @BindView(R.id.SearchActivity)
    public ImageView SearchActivity;
    private ConversationFragment conversationFragment = new ConversationFragment();
    private ContactFragment contactFragment = new ContactFragment();
    private Menu mMenu1, mMenu2;

    @OnClick({R.id.SearchActivity})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.SearchActivity:
                //startActivity(new Intent(context, SearchActivity.class));
                switch (type) {
                    case 0:
                        if (mMenu2.isShowing()) {
                            mMenu2.hide();
                        } else {
                            mMenu2.show();
                        }
                        break;
                    case 1:
                        if (mMenu1.isShowing()) {
                            mMenu1.hide();
                        } else {
                            mMenu1.show();
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        rootView = inflater.inflate(R.layout.fragment_four, null);
        return rootView;
    }

    @Override
    public void iniview() {
        if (BuildConfig.TYPE == 2) {
            SearchActivity.setVisibility(View.GONE);
        }
        viewpager.setAdapter(adapter = new setViewPager(getChildFragmentManager(), getsetList(), setViewPager.four));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                type = position;
                if (type > 1) {
                    type = 1;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(3);
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
        type = 0;
        //群聊菜单1
        mMenu1 = new Menu(activity, rootView, Menu.MENU_TYPE_CONTACT);
        //群聊菜单2
        mMenu2 = new Menu(activity, rootView, Menu.MENU_TYPE_CONVERSATION);
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    public List<Fragment> getsetList() {
        if (fragmentslist.size() == 0) {
            fragmentslist.add(conversationFragment);
            fragmentslist.add(contactFragment);
            fragmentslist.add(mssegslist.getfragment(4, "消息"));
        }
        return fragmentslist;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
