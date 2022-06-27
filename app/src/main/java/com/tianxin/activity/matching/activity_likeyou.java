/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.activity.matching;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_show;
import com.tianxin.BasActivity.BasActivity;
import com.tianxin.Module.api.rmbers;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.tencent.liteav.model.ITRTCAVCall.TYPE_AUDIO_CALL;
import static com.tencent.liteav.model.ITRTCAVCall.TYPE_VIDEO_CALL;

/**
 * 她想和你聊天哟
 */
public class activity_likeyou extends BasActivity {
    @BindView(R.id.imags)
    ImageView imags;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.circleImageView)
    ImageView circleImageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mssge)
    TextView mssge;
    String TAG = activity_likeyou.class.getSimpleName();
    private cs_alipay csAlipay;

    /**
     * 跳跳转到成功页
     *
     * @param json
     */
    public static void startsetAction(Context context, String json, int type) {
        Intent intent = new Intent(context, activity_likeyou.class);
        intent.putExtra(Constants.JSON, json);
        intent.putExtra(Constants.TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.video_speed;
    }

    @Override
    public void iniview() {
        csAlipay = new cs_alipay(context, paymnets2);
        title.setText(String.format(getString(R.string.sex_tv_msg), userInfo.getSex().equals("1") ? getString(R.string.tv_sex2) : getString(R.string.sex1_ttv)));
        String json = getIntent().getStringExtra(Constants.JSON);
        TYPE = getIntent().getIntExtra(Constants.TYPE, -1);
        if (!TextUtils.isEmpty(json)) {
            rmbers rmbers = new Gson().fromJson(json, rmbers.class);
            member = rmbers.getMember();
            if (member != null) {
                name.setText(TextUtils.isEmpty(member.getTvname()) ? member.getTruename() : member.getTvname());
                mssge.setText(Config.getName());
                if (!TextUtils.isEmpty(member.getPicture())) {
                    String path = member.getTencent() == 1 ? DemoApplication.presignedURL(member.getPicture()) : member.getPicture();
                    Glideload.loadImage(circleImageView, path);
                    Glideload(imags, path, 25, 10);
                }
            }
        }
        datamodule.getbalance(paymnets2); //获取用户自己的金额
        datamodule.getallcharge(paymnets1);//视频或语音扣费金币
    }

    /**
     * 搜索手机号
     *
     * @param phoneNumber
     * @param type
     */
    private void searchContactsByPhone(String phoneNumber, int type) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        ProfileManager.getInstance().getUserInfoByUserId(phoneNumber, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                mSearchModel = model;
                //这里可以给对方设置头像名称有于替换腾讯默认的信息
                if (!TextUtils.isEmpty(member.getTruename())) {
                    mSearchModel.userName = member.getTruename();
                }
                if (!TextUtils.isEmpty(member.getPicture())) {
                    mSearchModel.userAvatar = member.getPicture();
                }
                startCallSomeone(type);//开始呼叫对方
            }

            @Override
            public void onFailed(int code, String msg) {
                Toashow.show("无法发起，用户已离线" + code + msg);
            }
        });
    }

    /**
     * 开始呼叫某人
     */
    private void startCallSomeone(int mType) {
        //获取自己的资料
        mSelfModel = ProfileManager.getInstance().getUserModel();
        UserModel selfInfo = new UserModel();
        selfInfo.userId = mSelfModel.userId;
        selfInfo.userAvatar = mSelfModel.userAvatar;
        selfInfo.userName = mSelfModel.userName;
        selfInfo.Gender = Integer.parseInt(userInfo.getSex());
        selfInfo.token = userInfo.getToken();
        selfInfo.phone = userInfo.getPhone();
        selfInfo.tRole = 1;                  //角色 0接收 1拨打
        selfInfo.Level = userInfo.getLevel();//级别
        selfInfo.AllowType = 0;              //允许类型
        //自己的资料设置到一个静态变量中
        ProfileManager.getInstance().setUserModel(selfInfo);


        //创建对方的资料
        List<UserModel> models = new ArrayList<>();
        UserModel callUserInfo = new UserModel();
        callUserInfo.userId = mSearchModel.userId;
        callUserInfo.userAvatar = mSearchModel.userAvatar;
        callUserInfo.userName = mSearchModel.userName;
        selfInfo.Gender = member.getSex();
        selfInfo.Level = member.getLevel();
        selfInfo.AllowType = 0;
        selfInfo.tRole = 0;
        models.add(callUserInfo);

        if (mType == TYPE_VIDEO_CALL) {
            ToastUtils.showShort(context.getString(R.string.tv_msg168) + callUserInfo.userName);
            TRTCVideoCallActivity.startCallSomeone(context, models);

            //声网视频
            //CallVideo.startCallSomeone(context, models);
        } else {
            ToastUtils.showShort(context.getString(R.string.tv_msg169) + callUserInfo.userName);
            TRTCAudioCallActivity.startCallSomeone(context, models);
        }
    }

    private boolean getres() {
        if (info == null) {
            ToastUtil.toastShortMessage(getResources().getString(R.string.eorrfali2));
            return false;
        }

        //金币小于50个币 无法拨打视频或语音通话
        if (userInfo.getJinbi() < (TYPE == 1 ? allcharge.getVideo() : allcharge.getAudio()) && userInfo.gettRole() == 0) {
            dialog_show.dialogshow(context, member, paymnets3); //提示用户充值
            return false;
        }

        return true;

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.cols, R.id.senbnt, R.id.back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.senbnt:
                if (getres()) {
                    //搜索对方ID号，判断是否存在腾讯IM
                    searchContactsByPhone(String.valueOf(member.getId()), TYPE == 1 ? TYPE_VIDEO_CALL : TYPE_AUDIO_CALL);
                }
                break;
            case R.id.cols:
            case R.id.back:
                onBackPressed();
                break;

        }

    }

    @Override
    public void OnEorr() {

    }

    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
            Log.d(TAG, "onSuccess: " + allcharge.toString());
        }

        @Override
        public void ToKen(String msg) {
            Toashow.show(msg);
            BaseActivity.logout(context);
        }
    };

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void onSuccess(Object object) {
            info = (com.tencent.opensource.model.info) object;
            userInfo.setJinbi(info.getMoney());
            Log.d(TAG, "onSuccess: " + info.toString());
        }

        @Override
        public void ToKen(String msg) {
            Toashow.show(msg);
            BaseActivity.logout(context);
        }
    };

    /**
     * 转到支付宝提示用户充值
     */
    private Paymnets paymnets3 = new Paymnets() {
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
        public void payonItemClick(moneylist moneylist, int TYPE) {

            switch (TYPE) {
                case 1:
                    //发起支付宝请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case 2:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }

        }
    };

}
