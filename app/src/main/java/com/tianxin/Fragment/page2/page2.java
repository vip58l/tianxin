package com.tianxin.Fragment.page2;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Fragment.page2.activity.activity_page;
import com.tianxin.Fragment.page2.fragment.LiveTuivoiceRoom;
import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.Memberverify.activity_Namecenter;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.listener.Paymnets;
import com.tianxin.ViewPager.setViewPager;
import com.tianxin.dialog.dialog_item_view_zb;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

/**
 * 直播
 */
public class page2 extends BasFragment {
    @BindView(R.id.tab_layout)
    TabLayout tab_layout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.starzb)
    ImageView starzb;
    @BindView(R.id.gpsdw)
    LinearLayout gpsdw;
    @BindView(R.id.paihanbang)
    ImageView paihanbang;
    private LiveTuivoiceRoom liveTuivoiceRoom;
    public String TAG = page2.class.getSimpleName();

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_two, null);
    }

    @OnClick({R.id.starzb, R.id.back, R.id.gpsdw, R.id.paihanbang})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.starzb:
                if (mCurrentItem == 2) {
                    createRoom();
                } else {
                    tostartActivity();
                }
                break;
            case R.id.back:
                activity.finish();
                break;
            case R.id.gpsdw:
                checkpermissions(OPEN_SET_REQUEST_CODE);
                break;
            case R.id.paihanbang:
                activity_page.actionStart(context);
                break;
        }
    }

    /**
     * 音频直播
     */
    private void createRoom() {
        if (liveTuivoiceRoom != null) {
            switch (userInfo.getState()) {
                case 2:
                    liveTuivoiceRoom.createRoom();
                    break;
                case 3:
                    dialog_Blocked.myshow(context);
                    break;
                default:
                    dialog_item_view_zb.b(context, paymnets);
                    break;
            }

        }
    }

    @Override
    public void iniview() {
        paihanbang.setVisibility(View.VISIBLE);
        setViewPager = new setViewPager(getChildFragmentManager(), ListFragment(), setViewPager.two);
        viewpager.setAdapter(setViewPager);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
                starzb.setImageResource(mCurrentItem == 2 ? R.mipmap.aa11 : R.mipmap.ar7);
                if (position == 2 && liveTuivoiceRoom == null) {
                    liveTuivoiceRoom = (LiveTuivoiceRoom) fragmentslist.get(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setOffscreenPageLimit(3);
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
        if (list.size() > 0) {
            TabLayout.Tab tab = tab_layout.getTabAt(0);
            Tabtext(tab);
        }
        if (checkpermissions()) {
            gpsdw.setVisibility(View.GONE);
        }
        datamodule.loadpage2(paymnets);
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void tostartActivity() {
        switch (userInfo.getState()) {
            case 2:
                //转到视频开播界面
                LiveRoomAnchorActivity.start(context, "");
                break;
            case 3:
                dialog_Blocked.myshow(context);
                break;
            default:
                dialog_item_view_zb.b(context, paymnets);
                break;
        }
    }

    /**
     * 接口回调数据监听
     */
    private Paymnets paymnets = new Paymnets() {

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object obj) {
            Config_Msg configmsg = (Config_Msg) obj;
        }

        @Override
        public void onSuccess() {
            //要求实名认证
            startActivity(new Intent(context, activity_Namecenter.class));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_SET_REQUEST_CODE) {
            if (checkpermissions()) {
                gpsdw.setVisibility(View.GONE);
            }
            permissionsto.setPgs(1);
        }

    }
}

