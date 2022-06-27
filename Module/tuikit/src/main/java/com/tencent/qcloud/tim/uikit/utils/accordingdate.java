package com.tencent.qcloud.tim.uikit.utils;

import static com.blankj.utilcode.util.CrashUtils.init;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.liteav.login.UserModel;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.utilsdialog.Postdeduction;

import java.util.HashMap;
import java.util.Map;

public class accordingdate {
    private static final String TAG = accordingdate.class.getSimpleName();
    private String getvip = "/getvip";
    private UserInfo userInfo;
    private Gson gson;
    //获取用户总金币
    private double momey;
    //判断可以便用时长
    private double TimeCount;
    //计算出每秒收费情况
    private double secondjibi;
    //每分钟收费标准 N金币/分钟
    private double charge;
    //呼叫类型
    private int TYPE;

    private UserModel userModel;

    public double getCharge() {
        return charge;
    }

    /**
     * @param momey  //获取用户总金币
     * @param charge //每分钟收费标准
     * @param TYPE   //呼叫类型
     */
    public accordingdate(int momey, int charge, int TYPE) {
        this.momey = momey;
        this.charge = charge;
        this.TYPE = TYPE;
        this.userInfo = UserInfo.getInstance();
        this.gson = new Gson();

    }

    /**
     * 初始化用户金币
     */
    public void dateinit(UserModel userModel) {
        this.userModel = userModel;
        Map<String, String> params = new HashMap<>();
        params.put(Constants.USERID, userInfo.getUserId());
        params.put(Constants.TOKEN, userInfo.getToken());
        //查询余额//对方的ID号
        Postdeduction.goldcoin(userInfo.getUserId(), userInfo.getToken(), userModel.userId, callback);
        //查询VIP
        Postdeduction.getjson(params, getvip, callback2);

    }

    /**
     * 可通话总时长
     *
     * @return
     */
    public double getTimeCount() {
        TimeCount = momey / charge;
        return TimeCount;
    }


    public void setMomey(double momey) {
        this.momey = momey;
    }

    /**
     * 每秒收费标准
     *
     * @return
     */
    public double getSecondjibi() {
        secondjibi = charge / 60;
        return secondjibi;
    }

    /**
     * 通话时间转换
     *
     * @param count
     * @return
     */
    public String getShowTime(int count) {
        return String.format("%02d:%02d", count / 60, count % 60);
    }

    /**
     * 通话时间小于2分钟
     *
     * @param count
     * @return
     */
    public boolean getbootTime(int count) {
        double timeCount = getTimeCount();
        double mm = count / 60;
        double mms = timeCount - mm;
        return mms <= 2 ? true : false;
    }

    /**
     * 通话时间到
     *
     * @param count
     * @return
     */
    public boolean timeout(int count) {
        double timeCount = getTimeCount() * 60;
        return count >= timeCount ? true : false;
    }

    /**
     * 通话结束应该收费
     *
     * @param count
     * @return
     */
    public double kfmoney(int count) {
        double secondjibi = getSecondjibi();
        double kfmoney = secondjibi * count;
        return kfmoney;
    }

    /**
     * 通话时间倒计时
     *
     * @param count
     * @return
     */
    public String PshowTime(int count) {
        int mm = (int) (getTimeCount() * 60 - count);
        return getShowTime(mm);

    }

    /**
     * 获取用户金币余额
     */
    public void goldcoincallback(Callback mycallback) {
        this.mycallback = mycallback;
        Postdeduction.goldcoin(userInfo.getUserId(), userInfo.getToken(), "", callback);
    }


    /**
     * 查询金币回调结果
     * 和收费表准
     *
     * @return
     */
    private Postdeduction.Callback callback = new Postdeduction.Callback() {
        @Override
        public void onSuccess(String msg) {
            try {
                Mesresult mesresult = gson.fromJson(msg, Mesresult.class);
                if (mesresult.isSuccess()) {
                    com.tencent.qcloud.tim.uikit.utils.goldcoin goldcoin1 = gson.fromJson(mesresult.getData(), goldcoin.class);
                    momey = goldcoin1.getMoney();
                    userInfo.setJinbi(momey);
                    //视频或音频每N金币/分钟
                    charge = TYPE == 1 ? goldcoin1.getAudio() : goldcoin1.getVideo();
                    Log.d(TAG, "onSuccess: " + charge);

                } else {
                    momey = 0;
                    userInfo.setJinbi(momey);
                }
                if (mycallback != null) {
                    mycallback.OnSuccess();
                }
            } catch (Exception e) {
                ToastUtil.toastLongMessage(e.getMessage());
                if (mycallback != null) {
                    mycallback.onFall();
                }
            }

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFall() {

        }

        @Override
        public void onFailed() {

        }
    };

    /**
     * 查询VIP状态
     */
    private Postdeduction.Callback callback2 = new Postdeduction.Callback() {
        @Override
        public void onSuccess(String msg) {
            try {
                Mesresult mesresult = gson.fromJson(msg, Mesresult.class);
                if (mesresult.isSuccess()) {
                    getvip g = gson.fromJson(mesresult.getData(), getvip.class);
                    userInfo.setSex(String.valueOf(g.getSex()));
                    userInfo.setState(g.getStatus());
                    userInfo.setVip(g.getVip());
                } else {
                    userInfo.setVip(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFall() {

        }

        @Override
        public void onFailed() {

        }
    };

    public accordingdate.Callback mycallback;

    public interface Callback {

        void OnSuccess();

        void onFall();
    }

}
