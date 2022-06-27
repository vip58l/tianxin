package com.tianxin.Fragment.page2.activity;

import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.page2.fragment.wealth;
import com.tianxin.R;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.ViewPager.setViewPager;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_page extends BasActivity2 {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.send1)
    TextView send1;
    @BindView(R.id.send2)
    TextView send2;
    @BindView(R.id.send3)
    TextView send3;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, activity_page.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity, 1);
        return R.layout.activity_page;
    }

    @Override
    public void iniview() {
        if (fragments.size() == 0) {
            fragments.add(wealth.startActivity(1));
            fragments.add(wealth.startActivity(2));
            fragments.add(wealth.startActivity(3));
        }
        setViewPager setViewPager = new setViewPager(getSupportFragmentManager(), fragments, 0);
        viewPager.setAdapter(setViewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        corss(send1);
                        break;
                    case 1:
                        corss(send2);
                        break;
                    case 2:
                        corss(send3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.send1, R.id.send2, R.id.send3, R.id.backleft})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                viewPager.setCurrentItem(0);
                corss(send1);
                break;
            case R.id.send2:
                viewPager.setCurrentItem(1);
                corss(send2);
                break;
            case R.id.send3:
                viewPager.setCurrentItem(2);
                corss(send3);
                break;
            case R.id.backleft:
                onBackPressed();
                break;

        }

    }

    @Override
    public void OnEorr() {

    }

    private void corss(TextView textView) {
        send1.setBackground(null);
        send2.setBackground(null);
        send3.setBackground(null);
        textView.setBackground(getResources().getDrawable(R.drawable.btn_pink_bg));
    }

}