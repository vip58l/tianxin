package com.tianxin.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.activity.loadmatching.activity_Loadmatching;
import com.tianxin.listener.Paymnets;
import com.tencent.liteav.login.ProfileManager;
import com.tencent.liteav.login.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.opensource.model.personal;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 自动匹配弹窗
 */
public class dialog_activity_Megsse extends BaseDialog {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.file_name)
    TextView file_name;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.address2)
    TextView address2;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_bottom)
    Button btn_bottom;
    private int iisdelay = 5;
    public static final int sendempt = 200;
    public static final int delay = 1000;
    private Handler handler = new Ttime();
    private com.tencent.opensource.model.member member;

    private UserModel mSelfModel;    //表示当前用户的UserModel
    private UserModel mSearchModel;  //表示当前对方的usermodel
    public static final int TYPE_AUDIO_CALL = 1;
    public static final int TYPE_VIDEO_CALL = 2;


    public static dialog_activity_Megsse ationstar(Context context, Object s1, Object s2) {
        dialog_activity_Megsse dialog_activity_megsse = new dialog_activity_Megsse(context, s1, s2);
        dialog_activity_megsse.setCanceledOnTouchOutside(false);
        dialog_activity_megsse.show();
        return dialog_activity_megsse;
    }

    public dialog_activity_Megsse(@NonNull Context context, Object s1, Object s2) {
        super(context, ((Paymnets) s2));
        member = (com.tencent.opensource.model.member) s1;
        personal personal = member.getPersonal();
        if (personal != null) {
            age.setText(personal.getAge() > 1 ? String.valueOf(personal.getAge()) : "20");
            if (!TextUtils.isEmpty(personal.getPesigntext())) {
                title.setText(personal.getPesigntext());
            }

        }
        file_name.setText(member.getTruename());
        address.setText(TextUtils.isEmpty(member.getProvince()) ? context.getString(R.string.tm85) + "" : member.getProvince());
        address2.setText(TextUtils.isEmpty(member.getCity()) ? context.getString(R.string.tm85) + "" : member.getCity());
        if (!TextUtils.isEmpty(member.getPicture())) {
            ImageLoadHelper.glideShowCornerImageWithUrl(context,member.getPicture(),image);
        } else {
            Glideload.loadImage(image, member.getSex() == 1 ? R.mipmap.boy_on : R.mipmap.girl_on);
        }
        handler.sendEmptyMessageDelayed(sendempt, delay);

    }

    @Override
    public int getview() {
        return R.layout.dialog_activity_megsse;
    }

    @Override
    @OnClick({R.id.cancel, R.id.btn_bottom})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                paymnets.cancellation();
                dismiss();
                break;
            case R.id.btn_bottom:
                //金币得大于视频金币或是主播会员可拨打视频
                if (userInfo.getJinbi() >= (Allcharge.getInstance().getVideo() + 10) || userInfo.gettRole() == 1) {
                    if (activity_Loadmatching.checkSel()) {
                        searchContactsByPhone(String.valueOf(member.getId()), TYPE_VIDEO_CALL);
                        dismiss();
                    }
                } else {
                    dialog_show.dialogshow(context, member, paymnets);
                }
                break;
        }
    }

    private class Ttime extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case sendempt:
                    iisdelay--;
                    btn_bottom.setText(String.format(context.getString(R.string.tm83) + "", iisdelay));
                    if (iisdelay <= 0) {
                        if (isShowing()) {
                            dismiss();
                        }
                        paymnets.cancellation();
                    } else {
                        sendEmptyMessageDelayed(sendempt, delay);
                    }

                    break;
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeMessages(sendempt);
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (isShowing()) {
            paymnets.cancellation();
        }
        return super.onKeyDown(keyCode, event);

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
                //拿到对方的内容数据
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


}
