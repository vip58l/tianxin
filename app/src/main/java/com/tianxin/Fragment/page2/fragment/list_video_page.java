package com.tianxin.Fragment.page2.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.api.videotitle;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 街拍短视频分类列表
 */
public class list_video_page extends BasFragment {
    private final String TAG = list_video_page.class.getSimpleName();
    @BindView(R.id.tab_layout)
    public TabLayout tabLayout;
    @BindView(R.id.viewpager)
    public ViewPager viewpager;
    @BindView(R.id.SearchActivity)
    ImageView SearchActivity;
    @BindView(R.id.back)
    ImageView back;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.frnamg_video, null);
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
            layoutParams.width = 60;
            layoutParams.height = 60;
            SearchActivity.setLayoutParams(layoutParams);
        }
        getviewpager();
    }

    @Override
    public void initData() {
        back.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }


    /**
     * 保存到本地
     */
    public void setUserInfo(List<videotitle> data) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString("three", new Gson().toJson(data));
        editor.commit();
    }

    /**
     * 读取本地的数据
     *
     * @return
     */
    public synchronized List<videotitle> getdata() {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        String json = shareInfo.getString("three", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return JsonUitl.stringToList(json, videotitle.class);
    }

    /**
     * 返回fragment TAB
     *
     * @param data
     * @return
     */
    private List<Fragment> setList(List<videotitle> data) {
        if (fragmentslist.size() == 0) {
            //循环创建标题和page对像
            for (videotitle datum : data) {
                mlist.add(datum.getTitle());
                fragmentslist.add(fragment_jp.show(datum.getId()));
            }
            mlist.add(getString(R.string.play_6vs));
            fragmentslist.add(fragment_dy.show(1));
        }
        return fragmentslist;
    }

    /**
     * 读取本地内容
     */
    public void getviewpager() {
        List<videotitle> getdata = getdata();  //读取分类内容
        if (getdata == null) {
            datamodule.videotitle(paymnets);
            return;
        }
        fragmentslist = setList(getdata);
        adapter = new setViewPager(getChildFragmentManager(), fragmentslist, mlist.toArray(new String[mlist.size()]), setViewPager.three);
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(8);
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

        if (list.size() > 0) {
            // 默认选中文字状态
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            Tabtext(tab);
        }

    }

    /**
     * 回调联网数据
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            try {
                List<videotitle> videotitles = (List<videotitle>) object;
                setUserInfo(videotitles);
                getviewpager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            list_video_page.super.paymnets.ToKen(msg);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }
}
