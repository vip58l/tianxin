/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/17 0017
 */


package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.BasActivity.BaseDialog;
import com.tencent.opensource.model.member;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.info;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发起视频或语音
 */
public class dialog_videocall extends BaseDialog{
    String TAG = "dialog_videocall";
    public static final int TYPE_AUDIO_CALL = 1;
    public static final int TYPE_VIDEO_CALL = 2;
    private UserModel mSelfModel;    //表示当前用户的UserModel
    private UserModel mSearchModel;  //表示当前对方的usermodel
    private member member;
    private info info;
    private Allcharge allcharge;
    @BindView(R.id.audiocall)
    TextView audiocall;
    @BindView(R.id.videocall)
    TextView videocall;

    /**
     * 打电话或语音
     *
     * @param context
     * @param member
     */
    public static void videocall(Context context, member member, Paymnets paymnets) {
        dialog_videocall videocall = new dialog_videocall(context, member, paymnets);
        videocall.show();
    }

    public dialog_videocall(@NonNull Context context, member member, Paymnets paymnets) {
        super(context, paymnets);
        this.member = member;
        this.datamodule.getallcharge(String.valueOf(member.getId()), llcharge); //获取打电话收费配置
        this.datamodule.getbalance(balance);    //获取本人金币余额
        this.allcharge = allcharge.getInstance();

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    public int getview() {
        return R.layout.dialog_video;
    }


    @Override
    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3})
    public void OnClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.layout1:
                if (allcharge == null) {
                    Toashow.show(getContext().getString(R.string.eorrfali));
                    return;
                }
                if (checkSel(allcharge.getVideo())) {
                    return;
                }

                //视频呼叫
                if (mygetUserId(getContext().getString(R.string.tv_msg231))) {
                    return;
                }
                searchContactsByPhone(String.valueOf(member.getId()), TYPE_VIDEO_CALL);
                break;
            case R.id.layout2:
                if (allcharge == null) {
                    Toashow.show(getContext().getString(R.string.eorrfali));
                    return;
                }
                if (checkSel(allcharge.getAudio())) {
                    return;
                }
                //语音呼叫
                if (mygetUserId(getContext().getString(R.string.tv_msg230))) {
                    return;
                }
                searchContactsByPhone(String.valueOf(member.getId()), TYPE_AUDIO_CALL);
                break;
            case R.id.layout3:
                break;
        }
    }

    /**
     * 检查金币和用户资料
     *
     * @param Money
     * @return
     */
    private boolean checkSel(int Money) {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getContext().getResources().getString(R.string.eorrfali2));
            return true;
        }
        //视频金币<最低金币或不是又非 【Role主播1】
        if (userInfo.getJinbi() < Money && userInfo.gettRole() == 0) {
            dialog_show.dialogshow(context, member, paymnets);
            return true;
        }
        return false;
    }

    /**
     * 判断是否为自己
     *
     * @param msg
     * @return
     */
    private boolean mygetUserId(String msg) {
        if (member == null) {
            ToastUtil.toastShortMessage(String.format(getContext().getString(R.string.tv_msg265) + "", msg));
            return true;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(String.format(getContext().getString(R.string.tv_msg232) + "", msg));
            return true;
        }
        return false;
    }

    /**
     * 搜索查查询对方帐号是否存在
     *
     * @param phoneNumber
     * @param type
     */
    private void searchContactsByPhone(String phoneNumber, int type) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return;
        }
        /**
         * 搜索ID号码
         */
        ProfileManager.getInstance().getUserInfoByUserId(phoneNumber, new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                //拿到查询回调的数据
                mSearchModel = model;
                if (!TextUtils.isEmpty(member.getTruename())) {
                    mSearchModel.userName = member.getTruename();
                }
                if (!TextUtils.isEmpty(member.getPicture())) {
                    mSearchModel.userAvatar = member.getPicture();
                }
                mSearchModel.Gender = member.getSex();    //性别
                mSearchModel.tRole = 0;                   //角色 0接收 1拨打
                mSearchModel.Level = member.getLevel();   //级别
                mSearchModel.AllowType = 0;               //允许类型
                mSearchModel.phone = member.getMobile();  //电话
                startCallSomeone(type);                   //开始呼叫对方
            }

            @Override
            public void onFailed(int code, String msg) {
                Toashow.show(getContext(), getContext().getString(R.string.tv_msg167));
            }

        });
    }

    /**
     * 开始呼叫某人
     */
    private void startCallSomeone(int mType) {
        //呼叫方（即自己）的资料
        mSelfModel = ProfileManager.getInstance().getUserModel();
        UserModel selfInfo = new UserModel();
        selfInfo.userId = !TextUtils.isEmpty(mSelfModel.userId) ? mSelfModel.userId : userInfo.getUserId();
        selfInfo.userAvatar = !TextUtils.isEmpty(mSelfModel.userAvatar) ? mSelfModel.userAvatar : userInfo.getAvatar();
        selfInfo.userName = !TextUtils.isEmpty(mSelfModel.userName) ? mSelfModel.userName : userInfo.getName();
        selfInfo.Gender = Integer.parseInt(userInfo.getSex());
        selfInfo.token = userInfo.getToken();
        selfInfo.phone = userInfo.getPhone();
        selfInfo.tRole = 1;                  //角色 0接收 1拨打
        selfInfo.Level = userInfo.getLevel();//级别
        selfInfo.AllowType = 0;              //允许类型
        //自己的资料设置到一个静态变量中
        ProfileManager.getInstance().setUserModel(selfInfo);

        //接收方(对方)的资料
        List<UserModel> models = new ArrayList<>();
        UserModel callUserInfo = new UserModel();
        callUserInfo.userId = mSearchModel.userId;
        callUserInfo.userAvatar = mSearchModel.userAvatar;
        callUserInfo.userName = mSearchModel.userName;
        callUserInfo.Gender = mSearchModel.Gender;
        callUserInfo.tRole = mSearchModel.tRole;
        callUserInfo.Level = mSearchModel.Level;
        callUserInfo.AllowType = mSearchModel.AllowType;
        callUserInfo.phone = mSearchModel.phone;
        models.add(callUserInfo);

        if (mType == TYPE_VIDEO_CALL) {
            ToastUtils.showShort(getContext().getString(R.string.tv_msg168) + callUserInfo.userName);
            TRTCVideoCallActivity.startCallSomeone(context, models);

            //声网视频
            //CallVideo.startCallSomeone(context, models);
        } else {
            ToastUtils.showShort(getContext().getString(R.string.tv_msg169) + callUserInfo.userName);
            TRTCAudioCallActivity.startCallSomeone(context, models);
        }

    }

    /**
     * 设置Ui更新
     *
     * @param call
     */
    private void setTypeAudioCall(Allcharge call) {
        if (userInfo.gettRole() == 0) {
            audiocall.setText(call.getAudio() + "" + getContext().getString(R.string.tv_msg225));
            videocall.setText(call.getVideo() + "" + getContext().getString(R.string.tv_msg226));
        } else {
            audiocall.setText(R.string.tm105);
            videocall.setText(R.string.tm106);
        }
    }

    /**
     * 请求网络回调处理
     */
    private Paymnets llcharge = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
            //更新数据 音视频通话基本配置
            setTypeAudioCall(allcharge);
            paymnets.returnltonItemClick(object, 2);
        }

        @Override
        public void onSuccess(String msg) {

        }

        @Override
        public void fall(int code) {
            paymnets.fall(code);
        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }
    };

    /**
     * 请求网络回调处理
     */
    private Paymnets balance = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            info = (com.tencent.opensource.model.info) object; //用户金币余额
            userInfo.setJinbi(info.getMoney());
            paymnets.returnltonItemClick(object, 1);
        }

        @Override
        public void fall(int code) {
            paymnets.fall(code);

        }

        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }
    };

}
