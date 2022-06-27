package com.tencent.opensource.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.TUIKitLive;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;

import java.io.Serializable;

/**
 * 全局调用 修改只请注意 保存是否已登录mI通讯
 * 除非你了解他的使用用途
 */
public class UserInfo implements Serializable {
    private final static String PER_USER_MODEL = "per_user_model";
    private static UserInfo sUserInfo;
    private int SDKAPPID;     //IM_SDKAPPID
    private String zone;      //暂时留空
    private String username;  //登录帐号
    private String phone;     //帐号或手机
    private String mobile;    //帐号或手机
    private String pwd;       //登录密码
    private String token;     //访问令牌
    private String refresh;   //刷新令牌
    private String userId;    //会员UseID
    private String userSig;   //安全密钥

    private String name;        //别名
    private String givenname;
    private String avatar;      //普通头像
    private String picture;     //临时-->微信头像
    private String qq;          //qq
    private String wx;          //wx
    private String sex;         //性别
    private boolean autoLogin;  //登录成功标识
    private int balance;        //标记余额
    private int state;          //VIP会员标记
    private int vip;            //标记vip会员状态 1VIP
    private boolean svip;         //SVIP
    private int level;          //等级
    private int videotype;      //标记分类是否已选择
    private String duedate;     //会员到期时间
    private String pesigntext;
    private boolean isChecked;
    private boolean loginisChecked;
    private boolean Permission;

    private int lvideo;          //短视频开关
    private int live;            //直播板块开关
    private String remarks1;     //备注 预留备用
    private int allow;           //发布权限
    private int game;            //游戏开关
    private int tRole;           //用户角色【主播1】
    private int zfboff;          //支付宝关闭
    private int wxoff;           //微信关闭
    private double jinbi;        //微信关闭
    private boolean Nouse;       //禁止聊天，用于判断是否可以用来操作扣币标识
    private boolean gojinbi;     //判断是否同意允许扣除金币
    private int reale;           //是否需要实名认证
    private int inreview;        //头像审核中

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setUserInfo(this);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        setUserInfo(this);
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        setUserInfo(this);
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
        setUserInfo(this);
    }

    public int getInreview() {
        return inreview;
    }

    public void setInreview(int inreview) {
        this.inreview = inreview;
        setUserInfo(this);
    }

    public boolean isSvip() {
        return svip;
    }

    public void setSvip(boolean svip) {
        this.svip = svip;
        setUserInfo(this);
    }

    public int getLvideo() {
        return lvideo;
    }

    public void setLvideo(int lvideo) {
        this.lvideo = lvideo;
        setUserInfo(this);
    }

    public int getReale() {
        return reale;
    }

    public void setReale(int reale) {
        this.reale = reale;
        setUserInfo(this);
    }

    public int getSDKAPPID() {
        return SDKAPPID;
    }

    public void setSDKAPPID(int SDKAPPID) {
        this.SDKAPPID = SDKAPPID;
        setUserInfo(this);
    }

    public boolean isGojinbi() {
        return gojinbi;
    }

    public void setGojinbi(boolean gojinbi) {
        this.gojinbi = gojinbi;
        setUserInfo(this);
    }

    public boolean isNouse() {
        return Nouse;
    }

    public void setNouse(boolean nouse) {
        this.Nouse = nouse;
        setUserInfo(this);
    }

    public double getJinbi() {
        return jinbi;
    }

    public void setJinbi(double jinbi) {
        this.jinbi = jinbi;
        setUserInfo(this);
    }

    public int getZfboff() {
        return zfboff;
    }

    public void setZfboff(int zfboff) {
        this.zfboff = zfboff;
        setUserInfo(this);
    }

    public int getWxoff() {
        return wxoff;
    }

    public void setWxoff(int wxoff) {
        this.wxoff = wxoff;
        setUserInfo(this);
    }

    public boolean isLoginisChecked() {
        return loginisChecked;
    }

    public void setLoginisChecked(boolean loginisChecked) {
        this.loginisChecked = loginisChecked;
        setUserInfo(this);
    }

    public int gettRole() {
        return tRole;
    }

    public void settRole(int tRole) {
        this.tRole = tRole;
        setUserInfo(this);
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
        setUserInfo(this);
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
        setUserInfo(this);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        setUserInfo(this);
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
        setUserInfo(this);
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
        setUserInfo(this);
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
        setUserInfo(this);
    }

    public boolean isPermission() {
        return Permission;
    }

    public void setPermission(boolean permission) {
        Permission = permission;
        setUserInfo(this);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
        setUserInfo(this);
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
        setUserInfo(this);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        setUserInfo(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setUserInfo(this);
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
        setUserInfo(this);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        setUserInfo(this);
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
        setUserInfo(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setUserInfo(this);
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
        setUserInfo(this);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        setUserInfo(this);
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
        setUserInfo(this);
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
        setUserInfo(this);
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        setUserInfo(this);
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        setUserInfo(this);
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
        setUserInfo(this);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        setUserInfo(this);
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
        setUserInfo(this);
    }

    public int getVideotype() {
        return videotype;
    }

    public void setVideotype(int videotype) {
        this.videotype = videotype;
        setUserInfo(this);
    }

    public String getPesigntext() {
        return pesigntext;
    }

    public void setPesigntext(String pesigntext) {
        this.pesigntext = pesigntext;
        setUserInfo(this);
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "SDKAPPID=" + SDKAPPID +
                ", zone='" + zone + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", refresh='" + refresh + '\'' +
                ", userId='" + userId + '\'' +
                ", userSig='" + userSig + '\'' +
                ", name='" + name + '\'' +
                ", givenname='" + givenname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", qq='" + qq + '\'' +
                ", wx='" + wx + '\'' +
                ", sex='" + sex + '\'' +
                ", autoLogin=" + autoLogin +
                ", balance=" + balance +
                ", state=" + state +
                ", vip=" + vip +
                ", level=" + level +
                ", videotype=" + videotype +
                ", duedate='" + duedate + '\'' +
                ", pesigntext='" + pesigntext + '\'' +
                ", isChecked=" + isChecked +
                ", loginisChecked=" + loginisChecked +
                ", Permission=" + Permission +
                ", lvideo=" + lvideo +
                ", live=" + live +
                ", remarks1='" + remarks1 + '\'' +
                ", allow=" + allow +
                ", game=" + game +
                ", tRole=" + tRole +
                ", zfboff=" + zfboff +
                ", wxoff=" + wxoff +
                ", jinbi=" + jinbi +
                ", Nouse=" + Nouse +
                ", gojinbi=" + gojinbi +
                ", reale=" + reale +
                '}';
    }

    /**
     * 请取文件 json解析返回对像
     *
     * @return
     */
    public synchronized static UserInfo getInstance() {
        if (sUserInfo == null) {
            SharedPreferences shareInfo = TUIKitLive.getAppContext().getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString(PER_USER_MODEL, "");
            sUserInfo = new Gson().fromJson(json, UserInfo.class);
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
        }
        return sUserInfo;
    }

    /**
     * 请取文件 json解析返回对像
     *
     * @return
     */
    public synchronized static UserInfo getInstance(Context context) {
        if (sUserInfo == null) {
            SharedPreferences shareInfo = context.getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString(PER_USER_MODEL, "");
            sUserInfo = new Gson().fromJson(json, UserInfo.class);
            if (sUserInfo == null) {
                sUserInfo = new UserInfo();
            }
        }
        return sUserInfo;
    }


    /**
     * 写到本地文件中json
     *
     * @param info
     */
    public void setUserInfo(UserInfo info) {
        SharedPreferences shareInfo = TUIKitLive.getAppContext().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(PER_USER_MODEL, new Gson().toJson(info));
        editor.commit();
    }
}
