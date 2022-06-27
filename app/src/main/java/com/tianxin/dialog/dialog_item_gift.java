/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/31 0031
 */


package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tianxin.Module.api.present;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.ViewPager.pageadapter;
import com.tianxin.activity.Withdrawal.Detailedlist;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.adapter.GiftController;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.info;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftData;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftBean;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.GiftInfo;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.adapter.GiftPanelAdapter;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * 送礼物面板
 */
public class dialog_item_gift extends BottomSheetDialog {
    String TAG = dialog_item_gift.class.getName();
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.dots_container)
    LinearLayout dots_container;
    @BindView(R.id.money)
    TextView money;
    int columns = 4;
    int rows = 2;
    info info;
    private GiftController mGiftController;
    private Paymnets paymnets;
    private Unbinder bind;
    private Datamodule datamodule;
    private GiftPanelDelegate giftPanelDelegate;
    private present present;


    public static void dialogitemgift(Context context, present present, GiftPanelDelegate giftPanelDelegate, Paymnets paymnets) {
        dialog_item_gift dialogItemGift = new dialog_item_gift(context);
        dialogItemGift.setGiftPanelDelegate(giftPanelDelegate);
        dialogItemGift.setPaymnets(paymnets);
        dialogItemGift.setPresent(present);
        dialogItemGift.show();
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;

    }

    public void setGiftPanelDelegate(GiftPanelDelegate giftPanelDelegate) {
        this.giftPanelDelegate = giftPanelDelegate;
    }

    public void setPresent(present present) {
        this.present = present;
    }

    public dialog_item_gift(Context context) {
        super(context, R.style.BottomDialog);
        setContentView(R.layout.dialog_item_actiity);
        bind = ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        mGiftController = new GiftController();

        datamodule = new Datamodule(context, paymnets1);
        datamodule.getbalance(paymnets1);  //查询金币
        datamodule.giftlist();             //礼物列表
    }

    private void inidateView(String response) {
        GiftBean giftBean = new Gson().fromJson(response, GiftBean.class);
        List<GiftData> giftData = transformGiftInfoList(giftBean);
        //每页显示的礼物view
        List<View> list = new ArrayList<>();
        List<GiftInfo> giftInfoList = new ArrayList<>();
        for (GiftData giftDatum : giftData) {
            GiftInfo giftInfo = new GiftInfo();
            // 礼物id
            giftInfo.giftId = giftDatum.giftId;
            //礼物图片对应的url
            giftInfo.giftPicUrl = giftDatum.giftPicUrl;
            //礼物全屏动画url
            giftInfo.lottieUrl = giftDatum.lottieUrl;
            //礼物的名称
            giftInfo.title = giftDatum.title;
            //礼物价格
            giftInfo.price = giftDatum.price;
            //礼物类型 0为普通礼物， 1为播放全屏动画
            giftInfo.type = giftDatum.type;
            //礼物的选中状态
            giftInfo.isSelected = false;
            //礼物发送方名称
            giftInfo.sendUser = UserInfo.getInstance().getName();
            //礼物发送方头像
            giftInfo.sendUserHeadIcon = UserInfo.getInstance().getAvatar();
            //保存对像集合中list
            giftInfoList.add(giftInfo);
        }

        //计算出多少页 每页多少条数据 listSize=10条|columns=4列|rows=2行
        int pageSize = mGiftController.getPagerCount(giftInfoList.size(), columns, rows);
        for (int i = 0; i < pageSize; i++) {
            View view = mGiftController.viewPagerItem(getContext(), i, giftInfoList, columns, rows); //返回布局
            list.add(view);
            if (pageSize > 1) {
                //增加小点点
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(16, 16);
                params1.setMargins(20, 0, 20, 0);
                if (dots_container != null) {
                    dots_container.addView(dotsItem(i), params1);
                }
            }
        }
        dots_container.setVisibility(pageSize > 1 ? View.VISIBLE : View.GONE);
        viewpager.setAdapter(new pageadapter(getContext(), list));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots_container.getChildCount(); i++) {
                    dots_container.getChildAt(i).setSelected(false);
                }
                dots_container.getChildAt(position).setSelected(true);

                for (int i = 0; i < list.size(); i++) {
                    //清除选中，当礼物页面切换到另一个礼物页面
                    RecyclerView view = (RecyclerView) list.get(i);
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
        });
    }

    @OnClick({R.id.menyeyok, R.id.btn_charge, R.id.linfoot})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.menyeyok:
            case R.id.btn_charge:
                //执行送礼物操作
                bantgiftInfo();
                break;
            case R.id.linfoot:
                //点击充值金币
                //dialog_Config.dialogBottom(getContext(), paymnets);
                Detailedlist.starsetAction(getContext());
                dismiss();
                break;
        }
    }

    /**
     * 礼物页切换时，底部小圆点
     *
     * @param position
     * @return
     */
    private ImageView dotsItem(int position) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.live_layout_gift_dot, null);
        ImageView image = view.findViewById(R.id.face_dot);
        image.setId(position);
        return image;
    }

    private List<GiftData> transformGiftInfoList(GiftBean giftBean) {
        if (giftBean == null) {
            return null;
        }
        List<GiftBean.GiftListBean> giftBeanList = giftBean.getGiftList();
        if (giftBeanList == null) {
            return null;
        }
        List<GiftData> giftInfoList = new ArrayList<>();
        for (GiftBean.GiftListBean bean : giftBeanList) {
            GiftData giftData = new GiftData();
            giftData.giftId = bean.getGiftId();
            giftData.title = bean.getTitle();
            giftData.type = bean.getType();
            giftData.price = bean.getPrice();
            giftData.giftPicUrl = bean.getGiftImageUrl();
            giftData.lottieUrl = bean.getLottieUrl();
            giftInfoList.add(giftData);
        }
        return giftInfoList;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (bind != null) {
            bind.unbind();
        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    /**
     * 监听回调数据
     */
    private final Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }


        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));

        }

        @Override
        public void onSuccess(Object object) {
            info = (com.tencent.opensource.model.info) object;
            money.setText(String.valueOf(info.getMoney()));
            UserInfo.getInstance().setJinbi(info.getMoney());
        }

        @Override
        public void activity(String str) {
            inidateView(str);
        }

        @Override
        public void success(String date) {
            money.setText(date);
        }

        @Override
        public void msg(String msg) {
            ToastUtil.toastLongMessage(msg);
        }


    };

    /**
     * 送出提交礼物
     */
    private void bantgiftInfo() {

        //数据回调给初始页去
        GiftInfo giftInfo = mGiftController.getSelectGiftInfo();
        if (giftInfo == null) {
            ToastUtil.toastShortMessage(getString(R.string.showgetcon));
            return;
        }

        //金币是否足够 判断是否需要充值
        if (info.getMoney() < giftInfo.price) {
            //dialog_Config.dialogBottom(getContext(), paymnets); //弹出充值界面

            //转到金币充值
            Detailedlist.starsetAction(getContext());

            return;
        }

        //发起礼物送出 扣除金币
        if (giftInfo != null) {
            giftInfo.name = present.getName(); //接收方金币方名称
            datamodule.getpost(present, giftInfo, giftPanelDelegate);
        }
    }

}
