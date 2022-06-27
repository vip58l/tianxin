package com.tianxin.activity.sesemys;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Module.api.reguserinfo;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Utils;
import com.tianxin.activity.Aboutus.activity_viecode;
import com.tianxin.utils.MySocket;
import com.tianxin.activity.Withdrawal.acitivity_settlement;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.activity.edit.activity_edit;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.Util.DataCleanManager;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.Dialog_Exit;
import com.tianxin.dialog.dialog_load;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.callService;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.TUILiveRequestCallback;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class activity_sesemys extends BasActivity2 {
    @BindView(R.id.VersionName)
    TextView VersionName;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.anchor)
    LinearLayout anchor;
    private static final String TAG = activity_sesemys.class.getSimpleName();
    UserInfo userInfo;
    @BindView(R.id.s_v)
    Switch aSwitch;


    @Override
    protected int getview() {

        return R.layout.activity_sesemys;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg105));
        StatusBarUtil.setStatusBar(activity, Color.TRANSPARENT);
        VersionName.setText(Config.getVersionName(context));
        delete.setText(DataCleanManager.getTotalCacheSize());
        userInfo = UserInfo.getInstance();
    }

    @Override
    public void initData() {
        //是主播 要求性别 为女生 已实名
        anchor.setVisibility(userInfo.gettRole() == 1 && userInfo.getSex().equals("2") && userInfo.getState() == 2 ? View.VISIBLE : View.GONE);
        aSwitch.setChecked(Config_Msg.getInstance().isIsswitch());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (Utils.checkPermission(activity)) {
                    //控制开关字体颜色
                    if (isChecked) {
                        aSwitch.setSwitchTextAppearance(context, R.style.s_true);
                        Config_Msg.getInstance().setIsswitch(true);
                        //启动服务检查是否有在线用户
                        callService.startCall(null);
                    } else {
                        aSwitch.setSwitchTextAppearance(context, R.style.s_false);
                        Config_Msg.getInstance().setIsswitch(false);
                        //停止服务检查是否有在线用户
                        callService.callstopService();
                    }
                }
            }

        });
    }

    @OnClick({R.id.layout0, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.exit, R.id.layout01, R.id.layout02})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout0: {
                //绑定结算帐号
                acitivity_settlement.starsetAction(context);
                break;
            }
            case R.id.layout1: {
                activity_updateedit.starsetAction(context);
                break;
            }
            case R.id.layout2: {
                activity_edit.starsetAction(context);
                break;
            }
            case R.id.layout3: {
                dialog_load dialogLoad = new dialog_load(context);
                dialogLoad.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogLoad.dismiss();
                        DataCleanManager.clearAllCache(activity_sesemys.this);
                        delete.setText(DataCleanManager.getTotalCacheSize());
                        ToastUtils.showShort(getString(R.string.text22));
                    }
                }, 1000);
                break;
            }
            case R.id.layout4: {//关于我们
                activity_viecode.starsetAction(context);
                break;
            }
            case R.id.exit: {
                Dialog_Exit dialogExit = new Dialog_Exit(context);
                dialogExit.setdialogTitle(getString(R.string.tvmsgexit));
                dialogExit.show();
                dialogExit.setPaymnets(new Paymnets() {
                    @Override
                    public void activity() {

                    }

                    @Override
                    public void payens() {
                        Config.DeLeteall(DemoApplication.instance());
                        Config.DeLeteUserinfo(DemoApplication.instance());
                        BaseActivity.logout(DemoApplication.instance());
                        //config.DeLeteUserinfo(DemoApplication.instance(), "per_user_model");
                        TUIKit.unInit();//释放一些资源等，一般可以在退出登录时调用

                        //线程标记检查用户是否在线
                        MySocket.unInit();
                    }
                });
                break;
            }
            case R.id.layout01: {
                //消息提醒
                actrivity_sesemy_notice.starsetAction(context);
                break;
            }
            case R.id.layout02: {
                //隐私设置
                activity_privacy_settings.starsetAction(context);
                break;
            }
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 保存更新个人资料
     *
     * @param mesresult
     */
    public static void user_save_update_Profile(Mesresult mesresult) {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setUserId(mesresult.getUsreid().trim());              //会员UseID
        userInfo.setAutoLogin(true);                                   //成功标识
        userInfo.setUsername(mesresult.getUsername().trim());          //登录帐号
        userInfo.setPhone(TextUtils.isEmpty(mesresult.getMobile()) ? "" : mesresult.getMobile().trim());             //帐号或手机
        userInfo.setMobile(TextUtils.isEmpty(mesresult.getMobile()) ? "" : mesresult.getMobile().trim());              //手机号码
        userInfo.setPwd(TextUtils.isEmpty(mesresult.getPassword()) ? "" : mesresult.getPassword().trim());//登录密码
        userInfo.setToken(mesresult.getToken().trim());                //访问令牌
        userInfo.setRefresh(mesresult.getRefresh().trim());            //刷新令牌
        userInfo.setAvatar(mesresult.getPicture().trim());             //普通头像 用于IM刷新资料
        userInfo.setPicture(mesresult.getAvatar().trim());             //微信头像
        userInfo.setState(mesresult.getMember());                      //实名状态
        userInfo.setVip(mesresult.getVip());                           //VIP会员标记
        userInfo.setSex(mesresult.getSex());                           //性别
        userInfo.setName(mesresult.getAlias().trim());                 //名称
        userInfo.setGivenname(mesresult.getName().trim());             //姓名
        userInfo.setUserSig(mesresult.getUserSig().trim());            //安全密钥
        userInfo.setLive(mesresult.getLive());                         //直播开关
        userInfo.setLvideo(mesresult.getLvideo());                     //短视频开关
        userInfo.setRemarks1(mesresult.getRemarks1().trim());          //备注说明
        userInfo.setAllow(mesresult.getAllow());                       //发布权限
        userInfo.setReale(mesresult.getReale());                       //是否需要认证
        userInfo.settRole(mesresult.gettRole());                       //角色【1主播】
        userInfo.setInreview(mesresult.getInreview());                 //头像审核中
        updateProfile(userInfo);                                       //IM更新资料
    }

    /**
     * 更新本人字段信息
     *
     * @param mesresult
     */
    public static void user_save_update(Mesresult mesresult) {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setVip(mesresult.getVip());
        userInfo.setState(Integer.parseInt(mesresult.getStatus()));
        userInfo.setSex(mesresult.getSex());
        userInfo.setUsername(mesresult.getUsername());
        userInfo.setPhone(mesresult.getMobile());
        userInfo.setMobile(mesresult.getMobile());
        userInfo.setVideotype(mesresult.getVideotype());
        userInfo.setDuedate(mesresult.getDuedate());
        userInfo.setLive(mesresult.getLive());
        userInfo.setLevel(mesresult.getLevel());
        userInfo.setRemarks1(mesresult.getRemarks1());
        userInfo.setGame(mesresult.getGame());
        userInfo.setAvatar(mesresult.getPicture());
        userInfo.setZfboff(mesresult.getZfboff());
        userInfo.setWxoff(mesresult.getWxoff());
        userInfo.setJinbi(mesresult.getJinbi());
        userInfo.setAllow(mesresult.getAllow());
        userInfo.setReale(mesresult.getReale());
        userInfo.settRole(mesresult.gettRole());
        userInfo.setName(mesresult.getTruename());
        userInfo.setGivenname(mesresult.getName());
        userInfo.setLvideo(mesresult.getLvideo());
        userInfo.setInreview(mesresult.getInreview());
    }

    /**
     * 更新本人字段信息
     *
     * @param
     */
    public static void user_save_update1(reguserinfo info) {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setGivenname(info.getTvname());
        userInfo.setName(info.getTruename());
        userInfo.setPesigntext(info.getPesigntext());
        userInfo.setQq(info.getQq());
        userInfo.setWx(info.getWx());
        userInfo.setSex(String.valueOf(info.getSex()));
        userInfo.setVip(info.getVip());
        userInfo.settRole(info.gettRole());
        userInfo.setAvatar(info.getPicture());
        userInfo.setAllow(info.getAllow());
        userInfo.setReale(info.getReale());
        userInfo.setInreview(info.getInreview());
    }

    /**
     * 更新本人字段信息
     *
     * @param member member
     */
    public static void user_save_update2(com.tencent.opensource.model.member member) {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setState(member.getStatus());
        userInfo.setLevel(member.getLevel());
        userInfo.setAvatar(member.getPicture());
        userInfo.setVip(member.getVip());
        userInfo.setAllow(member.getAllow());
        userInfo.setDuedate(member.getDuedate());
        userInfo.setUserId(String.valueOf(member.getId()));
        userInfo.setReale(member.getReale());
        userInfo.setInreview(member.getInreview());
    }

    /**
     * 提交给腾讯IM修改个人资料
     *
     * @param userInfo
     */
    public static void updateProfile(UserInfo userInfo) {
        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        String selfUserID = V2TIMManager.getInstance().getLoginUser();
        String avatar = TextUtils.isEmpty(userInfo.getAvatar()) ? userInfo.getPicture() : userInfo.getAvatar(); //普通未上传头像调微信头像
        String mIconUrl = TextUtils.isEmpty(avatar) ? picpath(selfUserID) : avatar; //没用头像调系统生成的头像

        if (TextUtils.isEmpty(selfUserID) || loginStatus == 0) {
            return;
        }
        V2TIMUserFullInfo v2TIMUserFullInfo = new V2TIMUserFullInfo();

        //修改头像
        if (!TextUtils.isEmpty(mIconUrl)) {
            v2TIMUserFullInfo.setFaceUrl(mIconUrl);
            userInfo.setAvatar(mIconUrl);
        }

        //修改昵称
        String nickName = userInfo.getName();
        v2TIMUserFullInfo.setNickname(nickName);

        // 修改个性签名
        String signature = userInfo.getName();
        v2TIMUserFullInfo.setSelfSignature(signature);

        //加我验证方式
        int allowType = 0;
        v2TIMUserFullInfo.setAllowType(allowType);

        //男生或女生
        v2TIMUserFullInfo.setGender(userInfo.getSex().equals("1") ? 1 : 2);

        //提交给腾讯服务IM
        V2TIMManager.getInstance().setSelfInfo(v2TIMUserFullInfo, new V2TIMCallback() {
            @Override
            public void onError(int code, String desc) {
            }

            @Override
            public void onSuccess() {
                TUIKitConfigs.getConfigs().getGeneralConfig().setUserFaceUrl(mIconUrl);
                TUIKitConfigs.getConfigs().getGeneralConfig().setUserNickname(nickName);
                TUIKitLive.refreshLoginUserInfo(new TUILiveRequestCallback() {
                    @Override
                    public void onError(int code, String desc) {
                    }

                    @Override
                    public void onSuccess(Object o) {

                    }
                });
            }
        });
    }

    /**
     * 随机生成图片
     *
     * @return
     */
    public static String picpath(String selfUserID) {
        byte[] bytes = selfUserID.getBytes();
        int index = bytes[bytes.length - 1] % 10;
        String avatarName = "avatar" + index + "_100";
        String mIconUrl = "https://imgcache.qq.com/qcloud/public/static/" + avatarName + ".20191230.png";
        return mIconUrl;
    }

    /**
     * 查询自己的资料
     *
     * @param userid
     */
    public static void getUsersInfo(String userid) {
        V2TIMManager.getInstance().getUsersInfo(Arrays.asList(userid), new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {
            @Override
            public void onError(int code, String desc) {

            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                //获取资料
                V2TIMUserFullInfo v2TIMUserFullInfo = v2TIMUserFullInfos.get(0);
                String faceUrl = v2TIMUserFullInfo.getFaceUrl();
                UserInfo.getInstance().setAvatar(faceUrl);
            }
        });
    }


    /**
     * 获取服务器保存的自己的资料
     */
    public static void checkTIMInfo(final String nick, final String faceUrl) {
        TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onSuccess(TIMUserProfile timUserProfile) {
                if (timUserProfile != null) {
                    //如果资料不一致  需要更新资料
                    String timNick = timUserProfile.getNickName();
                    String timFaceUrl = timUserProfile.getFaceUrl();
                    if (!timNick.equals(nick) || !timFaceUrl.equals(faceUrl)) {
                        updateTIMInfo(nick, faceUrl);
                    }
                }
            }
        });
    }

    /**
     * 更新TIM资料
     */
    private static void updateTIMInfo(String nick, String faceUrl) {
        HashMap<String, Object> profileMap = new HashMap<>();
        if (!TextUtils.isEmpty(nick)) {
            profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_NICK, nick);
        }
        if (!TextUtils.isEmpty(faceUrl)) {
            profileMap.put(TIMUserProfile.TIM_PROFILE_TYPE_KEY_FACEURL, faceUrl);
        }
        if (!profileMap.isEmpty()) {
            TIMFriendshipManager.getInstance().modifySelfProfile(profileMap, new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {

                }

                @Override
                public void onSuccess() {

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Utils.REQ_PERMISSION_CODE) {
            if (Utils.checkPermission(activity)) {
                aSwitch.setSwitchTextAppearance(context, R.style.s_true);
                Config_Msg.getInstance().setIsswitch(true);
                //启动服务检查是否有在线用户
                callService.startCall(null);
            } else {
                aSwitch.setSwitchTextAppearance(context, R.style.s_false);
                Config_Msg.getInstance().setIsswitch(false);
                Toashow.show("请打开相关权限");
            }
        }


    }
}