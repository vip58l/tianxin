/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/5 0005
 */


package com.tianxin.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Aboutus.activity_viecode;
import com.tianxin.adapter.setAdapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.item;
import com.tianxin.Fragment.fragment.svip;
import com.tianxin.Module.api.buypaymoney;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import androidx.annotation.Nullable;


/**
 * 升级购买VIP会员
 */
public class activity_svip extends BasActivity2 {
    @BindView(R.id.item_back)
    itembackTopbr item_back;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.imgicon)
    ImageView imgicon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.send)
    TextView send;
    @BindView(R.id.tv_conter)
    TextView tv_conter;
    List<item> list = new ArrayList<>();
    buypaymoney buy;
    svip svip = new svip();
    boolean bnt = false;
    int[] grlis = {R.mipmap.ar2, R.mipmap.ar3, R.mipmap.are, R.mipmap.ar5, R.mipmap.ar6, R.mipmap.ar7, R.mipmap.ar8, R.mipmap.ari};
    String[] title;

    public cs_alipay csAlipay;
    public moneylist moneylist;
    private int TYPE = 0;
    public static final int zfb = 1;
    public static final int wx = 2;
    public static final int unknown = 3;

    public static void starsetAction(Context context) {
        context.startActivity(new Intent(context, activity_svip.class));
    }


    @Override
    protected int getview() {
        return R.layout.activity_svip;
    }

    @Override
    public void initData() {
        moneylist = new moneylist();
        csAlipay = new cs_alipay(context);
        csAlipay.setPaymnets(paymnets);
        send.setOnClickListener(v -> postdata());
        svip.setPaymnets(paymnets);

    }

    @Override
    public void iniview() {
        title = getResources().getStringArray(R.array.arrayitem9);
        item_back.settitle(getString(R.string.tv_msg131));
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            Glide.with(this).load(userInfo.getSex().equals("1") ? R.mipmap.ic_man_choose : R.mipmap.icon_woman_choose).into(imgicon);
        } else {
            Glide.with(this).load(userInfo.getAvatar()).into(imgicon);
        }
        tv_name.setText(userInfo.getName());

        gridview.setAdapter(new setAdapter(context, getList()));
        getSupportFragmentManager().beginTransaction().replace(R.id.layout3, svip).commit();
        if (!TextUtils.isEmpty(userInfo.getDuedate())) {
            tv_conter.setText(getString(R.string.tv_msg_da) + Config.stampToDate(userInfo.getDuedate()));
        }
    }

    @OnClick({R.id.layout1, R.id.lay1, R.id.lay2, R.id.pay_ok})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                activity_updateedit.starsetAction(context);
                break;
            case R.id.lay1:
                TYPE = zfb;
                OnEorr(v);
                break;
            case R.id.lay2:
                TYPE = wx;
                OnEorr(v);
                break;
            case R.id.pay_ok:
                String url = BuildConfig.HTTP_API + "/invitefriends?type=%s&userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken();
                activity_viecode.WebbookUrl(context, String.format(url, 7));
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 确认发起支付走起
     */
    public void postdata() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(context.getString(R.string.eorrfali2));
            return;
        }
        if (moneylist == null || buy == null) {
            Toashow.show(context.getString(R.string.tv_msg136));
            return;
        }
        if (TYPE == 0) {
            Toashow.show(context.getString(R.string.tv_msg137));
            return;
        }
        moneylist.setId(Integer.parseInt(buy.getId()));
        moneylist.setMoney(buy.getMoney1());
        moneylist.setMsg(buy.getTitle());
        moneylist.setVip(zfb);

        //进入发起支付数据
        switch (TYPE) {
            case zfb:
                //发起支付宝
                csAlipay.Paymoney(moneylist);
                break;
            case wx:
                //发起微信
                WXpayObject.getsWXpayObject().WXpay(moneylist);
                break;

        }
    }

    private List<item> getList() {
        for (int i = 0; i < title.length; i++) {
            item item = new item();
            bgs bgs = new bgs();
            bgs.title = title[i];
            bgs.img = grlis[i];
            item.type = setAdapter.svip;
            item.object = bgs;
            list.add(item);
        }
        return list;
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onClick(Object object) {
            paymoney(object);
        }

        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void onSuccess() {
            //付费成功刷新状态
            datamodule.getvip(null);
        }


    };

    /**
     * 提交购买请求
     *
     * @param object
     */
    public void paymoney(Object object) {
        buy = (buypaymoney) object;
        send.setBackground(getResources().getDrawable(R.drawable.acitvity04));
        send.setTextColor(getResources().getColor(R.color.white));
        send.setText(String.format(getString(R.string.tv_msg138), buy.getMoney1()));

        //月卡充值type=1
        Activity_qiaoapi.starAction(activity,buy.getTitle(), Integer.parseInt(buy.getId()), (int) Double.parseDouble(buy.getMoney1()), 1);

    }

    public class bgs {
        public int img;
        public String title;

        @Override
        public String toString() {
            return "bgs{" +
                    "img=" + img +
                    ", title='" + title + '\'' +
                    '}';
        }

        public int getImg() {
            return img;
        }

        public void setImg(int img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public void OnEorr(View v) {
        findViewById(R.id.lay1).setBackground(getDrawable(R.drawable.activity014));
        findViewById(R.id.lay2).setBackground(getDrawable(R.drawable.activity014));

        v.setBackground(getDrawable(R.drawable.diis_bg));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.sussess:
                break;
        }


    }

}
