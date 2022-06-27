package com.tianxin.alipay;

import static com.blankj.utilcode.util.GsonUtils.fromJson;

import android.text.TextUtils;

import com.alipay.sdk.pay.demo.util.OrderInfoUtil2_0;
import com.google.gson.Gson;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.biz_content;
import com.tencent.opensource.model.zfb_wx_appid;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class module_alipay {
    private static final String TAG = module_alipay.class.getName();
    private Paymnets paymnets;
    private moneylist moneylist;
    private UserInfo userInfo;
    private biz_content bizcontent;
    private Gson gson = new Gson();

    public void getdate(moneylist moneylist, UserInfo userInfo, Paymnets paynets) {
        this.paymnets = paynets;
        this.moneylist = moneylist;
        this.userInfo = userInfo;
        Httpalipay();
    }

    /**
     * 向平台查询支付宝PID密钥相关信息
     */
    private void Httpalipay() {

        HashMap<String, String> put = new HashMap<>();
        put.put(Constants.TYPE, "1");
        put.put(Constants.USERID, UserInfo.getInstance().getUserId());
        put.put(Constants.Token, UserInfo.getInstance().getToken());
        String param = Webrowse.param(Webrowse.alipay, put);
        PostModule.getModule(param, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    zfb_wx_appid zfbWxAppid = gson.fromJson(date, zfb_wx_appid.class);
                    paymnets.returnlt(zfbWxAppid);
                    setcallback(zfbWxAppid);
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail();
                }

            }

            @Override
            public void fall(int code) {
                paymnets.onFail();
            }
        });
    }

    /**
     * 拼接支付参数并写入平台记录 待支付
     */
    public void setcallback(zfb_wx_appid zfbWxAppid) {
        bizcontent = bizcontent(moneylist.getMoney());
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("token", userInfo.getToken())
                .add("moneylist", String.valueOf(moneylist.getId()))
                .add("orderid", bizcontent.getOut_trade_no())
                .add("money", bizcontent.getTotal_amount())
                .add("paymoney", bizcontent.getTotal_amount())
                .add("channel", String.valueOf(zfbWxAppid.getId()))
                .add("msg", TextUtils.isEmpty(moneylist.getMsg()) ? DemoApplication.instance().getString(R.string.tvpaymnet_msg) : moneylist.getMsg())
                .add("vip", String.valueOf(moneylist.getVip()))
                .build();

        //向服务器写入订单号
        PostModule.postModule(Webrowse.addorderid, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onClick(bizcontent);          //写入成功回调构建支付参数
                    } else {
                        paymnets.activity(mesresult.getMsg()); //回调写入订单失败
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail(); //回调错误信息
                }
            }

            @Override
            public void onFail() {
                paymnets.onFail();//回调错误信息
            }
        });
    }

    /**
     * 构建支付参数
     *
     * @param Money
     * @return
     */
    public biz_content bizcontent(String Money) {
        biz_content biz_content = new biz_content();
        biz_content.setTimeout_express("30");
        biz_content.setProduct_code("QUICK_MSECURITY_PAY");
        biz_content.setTotal_amount(String.valueOf(Money));
        biz_content.setSubject(TextUtils.isEmpty(moneylist.getMsg()) ? DemoApplication.instance().getString(R.string.tvpaymnet_msg) : moneylist.getMsg());
        biz_content.setBody(TextUtils.isEmpty(moneylist.getMsg()) ? DemoApplication.instance().getString(R.string.tvpaymnet_msg) : moneylist.getMsg());
        biz_content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());
        return biz_content;
    }

    /**
     * Success 支付成功后向服务器提交成功数据
     */
    public void update(String orderid) {
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("token", userInfo.getToken())
                .add("orderid", orderid)
                .build();
        PostModule.postModule(Webrowse.updateorderid, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    if (mesresult.getStatus().equals("1")) {
                        paymnets.onSuccess();
                    } else {
                        paymnets.activity(mesresult.getMsg()); //回调写入订单失败
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    paymnets.onFail(); //回调错误信息
                }
            }

            @Override
            public void fall(int code) {


            }
        });

    }

}
