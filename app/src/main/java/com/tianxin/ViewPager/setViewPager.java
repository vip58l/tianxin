package com.tianxin.ViewPager;

import android.content.res.Resources;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.R;

import java.util.List;

import static com.tencent.liteav.demo.beauty.utils.ResourceUtils.getResources;

/**
 * 适配器Fragment
 */
public class setViewPager extends FragmentPagerAdapter {
    String TAG = setViewPager.class.getSimpleName();
    private final List<Fragment> list;
    private final int TYPE;
    public final static int oneindex = 101;
    public final static int two = 102;
    public final static int three = 103;
    public final static int four = 104;
    public final static int picenter = 105;
    public final static int detailed = 106;
    public final static int tabs3 = 107;
    public final static int tabs4 = 108;
    public final static int tabs5 = 109;
    public final static int tabs6 = 110;
    private static String[] mytitle;
    private String[] citys = null;
    private Resources res = getResources();

    public setViewPager(FragmentManager fm, List<Fragment> list, int TYPE) {
        super(fm);
        this.list = list;
        this.TYPE = TYPE;
    }

    public setViewPager(FragmentManager fm, List<Fragment> list, String[] mlist, int TYPE) {
        super(fm);
        this.list = list;
        this.TYPE = TYPE;
        mytitle = mlist;
    }

    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (TYPE) {
            case oneindex:
                citys = BuildConfig.TYPE == 1 ? res.getStringArray(R.array.onindex2) : res.getStringArray(R.array.onindex);
                break;
            case four:
                citys = res.getStringArray(R.array.msgindex);
                break;
            case three:
                citys = mytitle == null ? res.getStringArray(R.array.four) : mytitle;
                break;
            case picenter:
                citys = res.getStringArray(R.array.arrayitem7);
                break;
            case detailed:
                citys = res.getStringArray(R.array.arrayitem14);
                break;
            case tabs3:
                citys = res.getStringArray(R.array.tabs3);
                break;
            case tabs4:
                citys = res.getStringArray(R.array.tabs6);
                break;
            case two:
                citys = res.getStringArray(R.array.two);
                break;
            case tabs5:
                citys = res.getStringArray(R.array.tabs8);
                break;
                case tabs6:
                citys = res.getStringArray(R.array.tabs11);
                break;
            default:
                citys = res.getStringArray(R.array.two);
                break;

        }
        return citys[position];
    }
}
