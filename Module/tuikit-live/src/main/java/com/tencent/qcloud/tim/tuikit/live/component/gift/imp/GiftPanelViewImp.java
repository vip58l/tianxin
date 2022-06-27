package com.tencent.qcloud.tim.tuikit.live.component.gift.imp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.info;
import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.IGiftPanelView;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftPanelAdapter;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftViewPagerAdapter;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始弹窗礼物窗口 及帐号余额情况
 */
public class GiftPanelViewImp extends BottomSheetDialog implements IGiftPanelView, View.OnClickListener {
    private static final String TAG = "GiftPanelViewImp";
    private final int COLUMNS = 4;
    private final int ROWS = 2;
    private final Context mContext;
    private List<View> mGiftViews;                               //每页显示的礼物view
    private GiftController mGiftController;
    private LayoutInflater mInflater;
    private LinearLayout mDotsLayout;
    private ViewPager mViewpager;
    private GiftPanelDelegate mGiftPanelDelegate;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private DefaultGiftAdapter defaultGiftAdapter;                 //获取查询类
    private com.tencent.opensource.model.info info;                //获取会员帐户对像
    private TextView smoney, btn_charge;
    private LinearLayout senbntpost;
    private TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo;               //主播相关资料
    private String mSelfUserId;                                       //观众的userid
    private Handler handler = new Handler();

    public GiftPanelViewImp(Context context) {
        super(context, R.style.live_action_sheet_theme);
        setContentView(R.layout.live_dialog_gift_panel);
        mContext = context;
        initView();
    }

    /**
     * 初始化内容
     */
    private void initView() {
        mGiftViews = new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
        mViewpager = findViewById(R.id.gift_panel_view_pager);
        mDotsLayout = findViewById(R.id.dots_container);
        smoney = findViewById(R.id.smoney);
        senbntpost = findViewById(R.id.senbntpost);
        btn_charge = findViewById(R.id.btn_charge);
        setCanceledOnTouchOutside(true);
        senbntpost.setOnClickListener(v -> mGiftPanelDelegate.onChargeClick()); //点击充值回调
        btn_charge.setOnClickListener(this::onClick);                           //确认赠送礼物
        defaultGiftAdapter = new DefaultGiftAdapter();                          //初始化对像
        getMoney();                                                             //初始联网查询余额
    }

    /**
     * 用户查询金额
     */
    private void getMoney() {

        //******************* get获取数据 **************************
        defaultGiftAdapter.getCallback(new DefaultGiftAdapter.callback() {
            @Override
            public void success(String response) {
                Gson gson = new Gson();
                try {
                    Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        info = gson.fromJson(mesresult.getData(), info.class);
                    } else {
                        info = new info();
                        info.setMoney(0);
                        info.setAudio(60);
                        info.setVideo(80);
                        info.setMiniamount(50);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        smoney.setText(String.valueOf(info.getMoney()));
                        if (mGiftPanelDelegate != null) {
                            mGiftPanelDelegate.myoney(info.getMoney());
                        }
                    }
                });
            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "onFailed: " + message);
            }
        });
        //******************* get获取数据 **************************
    }

    /**
     * 发起扣除金币请求
     */
    private void sendmoney(GiftInfo giftInfo) {
        defaultGiftAdapter.setCallback(giftInfo, mSelfUserId, mAnchorInfo, new DefaultGiftAdapter.callback() {
            @Override
            public void success(String response) {
                try {
                    Mesresult mesresult = new Gson().fromJson(response, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mGiftPanelDelegate.onGiftItemClick(giftInfo);
                                getMoney();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), mesresult.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(final String message) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * 初始化礼物面板
     */
    private void initGiftData(List<GiftInfo> giftInfoList) {
        if (mGiftController == null) {
            mGiftController = new GiftController();
        }
        int pageSize = mGiftController.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);
        // 获取页数
        for (int i = 0; i < pageSize; i++) {
            //每一页的数据装载
            View view = mGiftController.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS);
            mGiftViews.add(view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            if (pageSize > 1) {
                mDotsLayout.addView(dotsItem(i), params);
            }
        }

        if (pageSize > 1) {
            mDotsLayout.setVisibility(View.VISIBLE);
        } else {
            mDotsLayout.setVisibility(View.GONE);
        }

        //Viewpager适配器
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(mGiftViews);
        mViewpager.setAdapter(giftViewPagerAdapter);
        mViewpager.addOnPageChangeListener(new PageChangeListener());
        mViewpager.setCurrentItem(0);
        if (pageSize > 1) {
            mDotsLayout.getChildAt(0).setSelected(true);
        }

    }

    /**
     * 礼物页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View layout = mInflater.inflate(R.layout.live_layout_gift_dot, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
    }

    @Override
    public void onClick(View v) {
        if (btn_charge.equals(v)) {
            if (mGiftController != null) {
                GiftInfo giftInfo = mGiftController.getSelectGiftInfo();
                if (giftInfo == null) {
                    Toast.makeText(getContext(), getContext().getString(R.string.tv_msglwu), Toast.LENGTH_LONG).show();
                    return;
                }
                //金币不足请先冲值
                if (giftInfo.price > info.getMoney()) {
                    Toast.makeText(getContext(), getContext().getString(R.string.tv_msglw), Toast.LENGTH_LONG).show();
                    mGiftPanelDelegate.onChargeClick();
                    return;
                }
                //调起扣除礼物金币
                sendmoney(giftInfo);

            }
        }

    }

    /**
     * 礼物页改变时，dots效果也要跟着改变
     */
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
                mDotsLayout.getChildAt(i).setSelected(false);
            }
            mDotsLayout.getChildAt(position).setSelected(true);
            for (int i = 0; i < mGiftViews.size(); i++) {//清除选中，当礼物页面切换到另一个礼物页面
                RecyclerView view = (RecyclerView) mGiftViews.get(i);
                GiftPanelAdapter adapter = (GiftPanelAdapter) view.getAdapter();
                if (mGiftController != null) {
                    int selectPageIndex = mGiftController.getSelectPageIndex();
                    adapter.clearSelection(selectPageIndex);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void init(GiftInfoDataHandler giftInfoDataHandler) {
        mGiftInfoDataHandler = giftInfoDataHandler;
    }

    @Override
    public void show() {
        super.show();
        if (mGiftInfoDataHandler != null) {
            mGiftInfoDataHandler.queryGiftInfoList(new GiftInfoDataHandler.GiftQueryCallback() {
                @Override
                public void onQuerySuccess(final List<GiftInfo> giftInfoList) {
                    //确保更新UI数据在主线程中执行
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            initGiftData(giftInfoList);
                        }
                    });
                }

                @Override
                public void onQueryFailed(String errorMsg) {
                    Log.d(TAG, "request data failed, the message:" + errorMsg);
                }
            });
        }
    }

    @Override
    public void hide() {
        dismiss();
    }

    @Override
    public void setGiftPanelDelegate(GiftPanelDelegate delegate) {
        mGiftPanelDelegate = delegate;
    }

    @Override
    public void setGiftPanelUser(TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo, String mSelfUserId) {
        this.mSelfUserId = mSelfUserId;
        this.mAnchorInfo = mAnchorInfo;
    }

}