package com.tianxin.activity.video2.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Config;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.activity.Main.MainActivity;
import com.tianxin.activity.Searchactivity.SearchActivity;
import com.tianxin.activity.video.fragmunt.fragment;
import com.tencent.opensource.model.item;
import com.tencent.qcloud.costransferpractice.object.ObjectActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加载3不同内容的Fragment
 */
public class activityplayer extends BasActivity2 {
    private static final String TAG = activityplayer.class.getSimpleName();
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.image_back)
    ImageView image_back;

    public static void starsetAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activityplayer.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.mSystemUiVisibility(activity, true);
        Config.AsetctivityBLACK(activity);
        return R.layout.activity_viewpage2;
    }

    @Override
    public void iniview() {
        image_back.setVisibility(View.VISIBLE);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        boolean checkpermissions = ActivityLocation.checkPermissions(this);//申请定位权限
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }

        if (fragments.size() == 0) {
            //PLVideoView
//            fragments.add(framuntPage.show(1));
//            fragments.add(framuntPage.show(2));
//            fragments.add(framuntPage.show(3));
//
//            //VideoView
//            fragments.add(fragmenvideo.show(1));
//            fragments.add(fragmenvideo.show(2));
//            fragments.add(fragmenvideo.show(3));
//
//            //player
//            fragments.add(fragmenPlay.show(1));
//            fragments.add(fragmenPlay.show(2));
//            fragments.add(fragmenPlay.show(3));

            int POSITION = getIntent().getIntExtra(Constants.POSITION, 0);
            int TOTALPAGE = getIntent().getIntExtra(Constants.TOTALPAGE, 0);
            int TYPE = getIntent().getIntExtra(Constants.TYPE, 0);
            List<item> array = (List<item>) getIntent().getSerializableExtra(Constants.dadelist);

            //加载三个不同频内容的街拍视频
            fragments.add(fragment.show(POSITION, TOTALPAGE, TYPE, array));
            fragments.add(fragment.show(3));
            fragments.add(fragment.show(5));

        }
        adapter = new setViewPager(getSupportFragmentManager(), fragments, com.tianxin.ViewPager.setViewPager.oneindex);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void initData() {
        findViewById(R.id.hot).setOnClickListener(view -> viewPager.setCurrentItem(0));
        findViewById(R.id.gzt).setOnClickListener(view -> viewPager.setCurrentItem(1));
        findViewById(R.id.puse).setOnClickListener(view -> viewPager.setCurrentItem(2));
        findViewById(R.id.selectwquer).setOnClickListener(view -> SearchActivity.starsetAction(context));

        findViewById(R.id.tv1).setOnClickListener(view -> starmainActivity(1));
        findViewById(R.id.tv2).setOnClickListener(view -> starmainActivity(3));
        findViewById(R.id.lay3).setOnClickListener(view -> sUploadActivity(ObjectActivity.ACTIVITY_VIDEO));
        findViewById(R.id.tv4).setOnClickListener(view -> starmainActivity(4));
        findViewById(R.id.tv5).setOnClickListener(view -> starmainActivity(5));
    }

    @Override
    @OnClick({R.id.image_back})
    public void OnClick(View v) {
        finish();

    }

    @Override
    public void OnEorr() {

    }

    private void starmainActivity(int TYPE) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra("TYPE", TYPE);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        overridePendingTransition(R.anim.fade, R.anim.hold);

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("RESULT", TYPE);
        startActivity(intent);
        //overridePendingTransition(R.anim.fade, R.anim.hold);
    }

}
