package com.tianxin.Fragment.page1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Fragment.page1.fragment.one2;
import com.tianxin.Fragment.page1.fragment.one4;
import com.tianxin.activity.Searchactivity.activity_select;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.ViewPager.setViewPager;


import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

/**
 * 首页
 */
public class page1 extends BasFragment {
    private static final String TAG = page1.class.getSimpleName();
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.starzb)
    ImageView starzb;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.gpsdw)
    LinearLayout gpsdw;

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_two, null);
    }

    @OnClick({R.id.starzb, R.id.back, R.id.gpsdw})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.starzb:
                if (BuildConfig.TYPE == Constants.TENCENT) {
                    //交友应用
                    activity_select.setAction(context);
                } else {
                    //心里咨询
                    tostartActivity2();
                }
                break;
            case R.id.back:
                activity.finish();
                break;
            case R.id.gpsdw:
                if (checkpermissions(OPEN_SET_REQUEST_CODE)) {
                    gpsdw.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    public void iniview() {
        if (BuildConfig.TYPE == Constants.TENCENT2) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = starzb.getLayoutParams();
        layoutParams.width = 80;
        layoutParams.height = 80;
        starzb.setLayoutParams(layoutParams);
        starzb.setPadding(0, 0, 10, 0);
        starzb.setImageResource(R.mipmap.icon_search);
        //首次申请定位提示
        if (!userInfo.isPermission()) {
            checkpermissions(OPEN_SET_REQUEST_CODE);
        }
        //获取定位权限后隐藏提示申请定位功能
        if (checkpermissions()) {
            gpsdw.setVisibility(View.GONE);
        }

        if (fragmentslist.size() == 0) {
            fragmentslist = BuildConfig.TYPE == Constants.TENCENT ? jyshow() : xlzyshow();
        }
    }

    @Override
    public void initData() {
        setViewPager = new setViewPager(getChildFragmentManager(), fragmentslist, setViewPager.oneindex);
        viewpager.setAdapter(setViewPager);
        viewpager.setOffscreenPageLimit(5);// 设置缓存数量为4 避免销毁重建
        tab_layout.setupWithViewPager(viewpager);
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
        if (fragmentslist.size() > 0) {
            TabLayout.Tab tab = tab_layout.getTabAt(0);
            Tabtext(tab);
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_SET_REQUEST_CODE) {
            if (checkpermissions()) {
                gpsdw.setVisibility(View.GONE);
                for (int i = 0; i < fragmentslist.size(); i++) {
                    if (fragmentslist.get(i) instanceof one2) {
                        one2 one2 = (one2) fragmentslist.get(i);
                        one2.GPSAMapLocation();
                    }
                    if (fragmentslist.get(i) instanceof one4) {
                        one4 one4 = (one4) fragmentslist.get(i);
                        one4.GPSAMapLocation();
                    }
                }

            }
            //申请不管成失都记录申请过了
            userInfo.setPermission(true);
            permissionsto.setPgs(1);
        }

    }

}

