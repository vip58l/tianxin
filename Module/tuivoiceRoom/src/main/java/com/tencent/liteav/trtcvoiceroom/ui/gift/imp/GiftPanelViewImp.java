package com.tencent.liteav.trtcvoiceroom.ui.gift.imp;

import static com.tencent.liteav.trtcvoiceroom.ui.gift.Constant.GIFT_PANEL_TYPE_SINGLEROW;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.tencent.liteav.trtcvoiceroom.R;
import com.tencent.liteav.trtcvoiceroom.ui.gift.GiftPanelDelegate;
import com.tencent.liteav.trtcvoiceroom.ui.gift.IGiftPanelView;
import com.tencent.liteav.trtcvoiceroom.ui.gift.imp.adapter.GiftPanelAdapter;
import com.tencent.liteav.trtcvoiceroom.ui.gift.imp.adapter.GiftViewPagerAdapter;
import com.tencent.liteav.trtcvoiceroom.ui.widget.BaseBottomSheetDialog;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.info;

import java.util.ArrayList;
import java.util.List;

public class GiftPanelViewImp extends BaseBottomSheetDialog implements IGiftPanelView {
    private static final String TAG = "GiftPanelViewImp";
    private int COLUMNS = 4;
    private int ROWS = 2;
    private Context mContext;
    private List<View> mGiftViews;     //每页显示的礼物view
    private GiftController mGiftController;
    private LayoutInflater mInflater;
    private LinearLayout mDotsLayout;
    private ViewPager mViewpager;
    private GiftPanelDelegate mGiftPanelDelegate;
    private GiftInfoDataHandler mGiftInfoDataHandler;
    private String mDefalutPanelType = GIFT_PANEL_TYPE_SINGLEROW;
    private Button btn_send_gift;
    private View separate_line;
    private TextView btn_charge, jb_money;
    private Handler handler = new Handler();

    private DefaultGiftAdapter defaultGiftAdapter;
    private String mAnchorInfo;
    private String mSelfUserId;
    private info info;

    public GiftPanelViewImp(Context context) {
        super(context, R.style.TRTCKTVRoomDialogTheme);
        mContext = context;
        mGiftViews = new ArrayList<>();
        setContentView(R.layout.trtckaraoke_dialog_gift_panel);
        initView();
    }

    private void initView() {
        mInflater = LayoutInflater.from(mContext);
        mViewpager = findViewById(R.id.gift_panel_view_pager);
        mDotsLayout = findViewById(R.id.dots_container);
        btn_send_gift = findViewById(R.id.btn_send_gift);
        separate_line = findViewById(R.id.separate_line);
        btn_charge = findViewById(R.id.btn_charge);
        jb_money = findViewById(R.id.jb_money);

        if (GIFT_PANEL_TYPE_SINGLEROW.equals(mDefalutPanelType)) {
            COLUMNS = 5;
            ROWS = 1;
            btn_send_gift.setVisibility(View.GONE);
            separate_line.setVisibility(View.GONE);

            TextView textView = findViewById(R.id.tv_gift_panel_title);
            textView.setTextSize(24);
            textView.setPadding(20, 32, 0, 0);

            mDotsLayout.setVisibility(View.GONE);
            LinearLayout linearLayout = findViewById(R.id.giftLayout);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams.height *= 0.6;
            linearLayout.setLayoutParams(layoutParams);
        }
        btn_charge.setOnClickListener(v -> mGiftPanelDelegate.onChargeClick());
        btn_send_gift.setOnClickListener(v -> mGiftPanelDelegate.onChargeClick());
        setCanceledOnTouchOutside(true);
        defaultGiftAdapter = new DefaultGiftAdapter(); //初始化对像
        getMoney();
    }

    /**
     * 刷新初始化礼物面板
     */
    private void initGiftData(List<GiftInfo> giftInfoList) {
        if (mGiftController == null) {
            mGiftController = new GiftController();
        }
        mGiftController.setGiftClickListener((position, giftInfo) -> Callback_GifItem(giftInfo)); //监听点击礼物回调
        int pageSize = mGiftController.getPagerCount(giftInfoList.size(), COLUMNS, ROWS);

        // 获取页数
        for (int i = 0; i < pageSize; i++) {
            //添加布局文件
            mGiftViews.add(mGiftController.viewPagerItem(mContext, i, giftInfoList, COLUMNS, ROWS));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(10, 0, 10, 0);
            if (pageSize > 1) {
                mDotsLayout.addView(dotsItem(i), params);
            }
        }
        if (GIFT_PANEL_TYPE_SINGLEROW.equals(mDefalutPanelType) && pageSize > 1) {
            mDotsLayout.setVisibility(View.VISIBLE);
        } else {
            mDotsLayout.setVisibility(View.GONE);
        }
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
        View layout = mInflater.inflate(R.layout.trtckaraoke_layout_gift_dot, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
        iv.setId(position);
        return iv;
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
            for (int i = 0; i < mGiftViews.size(); i++) {

                //清除选中，当礼物页面切换到另一个礼物页面
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
                public void onQuerySuccess(List<GiftInfo> giftInfoList) {
                    //确保更新UI数据在主线程中执行
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            //执行成功返回
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
    public void setGiftPanelUser(String mAnchorInfo, String mSelfUserId) {
        this.mAnchorInfo =mAnchorInfo;
        this.mSelfUserId = mSelfUserId;

    }

    /**
     * 查询自己的金币
     */
    private void getMoney() {
        defaultGiftAdapter.getCallback(new DefaultGiftAdapter.callback() {
            @Override
            public void success(String response) {
                try {
                    Gson gson = new Gson();
                    Mesresult mesresult = gson.fromJson(response, Mesresult.class);
                    if (mesresult.isSuccess()) {
                        info = new Gson().fromJson(mesresult.getData(), info.class);
                    } else {
                        info = new info();
                        info.setMoney(0);
                        info.setAudio(60);
                        info.setVideo(80);
                        info.setMiniamount(50);
                    }
                    Refreshview(info.getMoney());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(String message) {
                Log.d(TAG, "onFailed: " + message);
            }
        });
    }

    /**
     * 礼物送出发起扣除金币请求
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
                                getMoney();
                                mGiftPanelDelegate.onGiftItemClick(giftInfo);
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
     * 执行回调送出礼物
     */
    private void Callback_GifItem(GiftInfo giftInfo) {
        if (giftInfo == null) {
            Toast.makeText(getContext(), getContext().getString(com.tencent.qcloud.tim.tuikit.live.R.string.tv_msglwu), Toast.LENGTH_LONG).show();
            return;
        }
        //金币不足请先冲值
        if (giftInfo.price > info.getMoney()) {
            mGiftPanelDelegate.onChargeClick();
            return;
        }
        //调起扣除礼物金币
        sendmoney(giftInfo);
    }

    /**
     * 刷新UI
     *
     * @param myoney
     */
    private void Refreshview(double myoney) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                jb_money.setText(String.format("%s 币", myoney));
            }
        });
    }

}
