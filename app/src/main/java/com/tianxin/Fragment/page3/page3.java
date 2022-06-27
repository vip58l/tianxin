package com.tianxin.Fragment.page3;

import android.content.Intent;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Fragment.page1.fragment.one5;
import com.tianxin.Fragment.page3.fragment.page3_1;
import com.tianxin.Fragment.page3.fragment.page3_2;
import com.tianxin.Fragment.page3.fragment.page3_3;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.setViewPager;
import com.tencent.opensource.model.UserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发现 动态 陪玩
 */
public class page3 extends BasFragment {
    @BindView(R.id.tab1)
    TextView tab1;
    @BindView(R.id.tab2)
    TextView tab2;
    @BindView(R.id.tab3)
    TextView tab3;
    @BindView(R.id.tab4)
    TextView tab4;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    String TAG = page3.class.getSimpleName();
    public page3_1 page31 = page3_1.showfmessage(0, "");
    public page3_2 page32 = page3_2.showfmessage(0, "");
    public page3_3 page33 = page3_3.show();

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.page3_1, null);
    }

    @Override
    public void iniview() {
        viewpager.setAdapter(setViewPager = new setViewPager(getChildFragmentManager(), listFragmentcity(), setViewPager.oneindex));
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageTextPaint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        tab4.setVisibility(userInfo.getGame() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    @OnClick({R.id.tab1, R.id.tab2, R.id.tab3, R.id.tab4, R.id.fabudongtai})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                viewpager.setCurrentItem(0);
                break;
            case R.id.tab2:
                viewpager.setCurrentItem(1);
                break;
            case R.id.tab3:
                viewpager.setCurrentItem(2);
                break;
            case R.id.tab4:
                viewpager.setCurrentItem(3);
                break;
            case R.id.fabudongtai:
                activitynews();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            page31.loadMoreData();
        }
    }

    private void pageTextPaint(int position) {
        TextPaint paint1 = tab1.getPaint();
        TextPaint paint2 = tab2.getPaint();
        TextPaint paint3 = tab3.getPaint();
        TextPaint paint4 = tab4.getPaint();
        paint1.setFakeBoldText(false);
        paint2.setFakeBoldText(false);
        paint3.setFakeBoldText(false);
        paint4.setFakeBoldText(false);
        tab1.setTextSize(18);
        tab2.setTextSize(18);
        tab3.setTextSize(18);
        tab4.setTextSize(18);

        switch (position) {
            case 0:
                tab1.setTextSize(22);
                paint1.setFakeBoldText(true);
                break;
            case 1:
                tab2.setTextSize(22);
                paint2.setFakeBoldText(true);
                break;
            case 2:
                tab3.setTextSize(22);
                paint3.setFakeBoldText(true);
                break;
            case 3:
                tab4.setTextSize(22);
                paint4.setFakeBoldText(true);
                break;
        }
        tab4.setVisibility(userInfo.getGame() == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * 发现
     *
     * @return
     */
    public List<Fragment> listFragmentcity() {
        if (fragmentslist.size() == 0) {
            fragmentslist.add(page31);
            fragmentslist.add(page32);
            fragmentslist.add(one5.show(5));
            if (UserInfo.getInstance().getGame() == 0) {
                //陪玩 应用市场无法上架APP
                fragmentslist.add(page33);
            }
        }
        return fragmentslist;
    }

}
