package com.tianxin.ViewPager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

public class YPagerAdapter2 extends cn.youngkaaa.yviewpager.YPagerAdapter {
    private Context context;
    public List<View> list;

    public YPagerAdapter2(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(list.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    /**
     * 刷新数据
     *
     * @param
     */
    public void setDataSetChanged(List<View> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}
