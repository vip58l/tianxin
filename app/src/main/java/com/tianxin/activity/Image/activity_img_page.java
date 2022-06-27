package com.tianxin.activity.Image;

import static com.blankj.utilcode.util.StringUtils.getString;

import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.itembackTopbr;
import com.steven.selectimage.GlideApp;
import com.tencent.opensource.model.tupianzj;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_img_page extends BasActivity2 {
    String TAG = activity_img_page.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.viewpager2)
    ViewPager2 viewpager2;
    Radapter radapter;
    tupianzj tupianzj;

    public static void starAction(Context context, String json) {
        Intent intent = new Intent();
        intent.setClass(context, activity_img_page.class);
        intent.putExtra(Constants.TUPIANZJ, json);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.activity_img_page;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg268));
        itemback.setHaidtopBackgroundColor(getResources().getColor(R.color.transparent));
        itemback.settvsetTextColor(getResources().getColor(R.color.white));
        radapter = new Radapter(context, list, Radapter.activity_img_page);
        viewpager2.setAdapter(radapter);
        viewpager2.setCurrentItem(0);
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.d(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                itemback.settitle(String.format("%s(%s/%s)", getString(R.string.tv_msg268), (position + 1), radapter.getItemCount()));
                if (position == list.size() - 1) {
                    getperimgpic();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.d(TAG, "onPageScrollStateChanged: " + state);
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @Override
    public void initData() {
        try {
            Intent intent = getIntent();
            String json = intent.getStringExtra(Constants.TUPIANZJ);
            tupianzj = gson.fromJson(json, tupianzj.class);
            GlideApp.get(context).clearMemory();
            getperimgpic();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadMoreData() {
        list.clear();
        radapter.notifyDataSetChanged();
        totalPage = 0;
        initData();
    }

    @Override
    @OnClick({R.id.tv3title})
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {


    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            List<tupianzj> data = (List<com.tencent.opensource.model.tupianzj>) object;
            if (data.size() == 0) {
                totalPage--;
                if (totalPage > 0) {
                    Toashow.show(getString(R.string.eorrtext));
                }
                OnEorr();
                return;
            }
            list.addAll(data);
            radapter.notifyDataSetChanged();
            if (itemback != null) {
                itemback.settitle(String.format("%s(%s)", getString(R.string.tv_msg268), radapter.getItemCount()));
            }
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }
    };

    private void getperimgpic() {
        totalPage++;
        datamodule.imgpage(totalPage, tupianzj, paymnets);
    }


}