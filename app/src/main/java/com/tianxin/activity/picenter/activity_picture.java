package com.tianxin.activity.picenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tianxin.activity.picenter.fragment.per5;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tencent.opensource.model.item;
import com.tianxin.ViewPager.setViewPagerIMage;
import com.tencent.opensource.model.imglist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_delete_img;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人详情图片
 */
public class activity_picture extends BasActivity2 {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.pages)
    TextView pages;
    @BindView(R.id.title)
    TextView title;
    List<Fragment> list;
    List<item> itemList;
    String TAG = activity_picture.class.getName();


    public static void setAction(Activity context, int position, String name, String img) {
        List<item> itemList = new ArrayList<>();
        imglist imglist = new imglist();
        imglist.setTitle(name);
        imglist.setBgpic(img);
        imglist.setPic(img);

        item item = new item();
        item.object = imglist;
        itemList.add(item);

        Intent intent = new Intent(context, activity_picture.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.dadelist, (Serializable) itemList);
        context.startActivityForResult(intent, Config.sussess);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.activity_viewpages;
    }

    @Override
    public void iniview() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    @Override
    public void initData() {
        int position = getIntent().getIntExtra(Constants.POSITION, -1);
        itemList = (List<item>) getIntent().getSerializableExtra(Constants.dadelist);
        viewPager.setAdapter(new setViewPagerIMage(getSupportFragmentManager(), getListimg(itemList)));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPlayingPosition = position;
                settitle(mPlayingPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(position);
        settitle(position);
    }

    @Override
    public void OnEorr() {

    }


    /**
     * 初始化读取数据
     *
     * @param itemList
     * @return
     */
    private List<Fragment> getListimg(List<item> itemList) {
        list = new ArrayList<>();
        for (item item : itemList) {
            imglist perimg = (imglist) item.object;
            String presignedURL = perimg.getPic();
            try {
                presignedURL = perimg.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(perimg.getPic()) : perimg.getPic();
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(per5.seturl(presignedURL));
        }
        return list;
    }

    private void settitle(int pos) {
        if (itemList != null) {
            item item = itemList.get(pos);
            imglist perimg = (imglist) item.object;
            title.setVisibility(!TextUtils.isEmpty(perimg.getTitle()) ? View.VISIBLE : View.GONE);
            title.setText(perimg.getTitle());
        }
        pages.setText(String.format("%s/%s", pos + 1, list.size()));
    }

    private void deletevideo() {
        dialog_game dialogGame = dialog_Config.dialog_game(this);
        dialogGame.setTitle(getString(R.string.ts_tss_ok));
        dialogGame.setKankan(getString(R.string.tv_msg113));
        dialogGame.setTextColor(getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(new Paymnets() {
            @Override
            public void activity() {
                Intent date = new Intent();
                date.putExtra(Constants.PATHVIDEO, mPlayingPosition);
                setResult(Config.on, date);
                finish();
            }
        });
    }

    @OnClick({R.id.back, R.id.djibao})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.djibao:
                dialog_delete_img.dialogdeleteimg(this, new Paymnets() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: ");
                        deletevideo();
                    }

                    @Override
                    public void onRefresh() {
                        Log.d(TAG, "onRefresh: ");
                        Toashow.show("暂不支持编辑图片");
                    }

                    @Override
                    public void onLoadMore() {
                        Log.d(TAG, "onLoadMore: ");
                    }
                });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
        itemList.clear();
    }
}