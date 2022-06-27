/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/27 0027
 */


package com.tencent.opensource.model;

/**
 * 接收返回状态结果
 */
public class Mesresult {
    public int id;               //返回id
    private String status;       //登录识别标记
    private String usreid;       //用户userid
    private String username;     //登录帐号
    private String mobile;       //登录手机号
    private String password;     //登录密码
    private String msg;          //消息
    private String sex;          //性别
    private String ok;           //状态
    private String alias;        //别名
    private String name;         //姓名
    private String token;        //访问令牌
    private String refresh;      //刷新令牌
    private String picture;      //普通头像
    private String code;         //验证码注册或找回密码
    private String duedate;      //验证码注册或找回密码
    private String avatar;       //微信头像
    private String truename;     //昵称
    private int member;          //审核状态
    private int game;            //备注 预留备用
    private int vip;             //VIP状态
    private int level;           //级别
    private int video;           //上传视频数量
    private int cover;           //上传封面数量
    private int photo;           //上传图片数量
    private int videotype;       //是否已点击关注问题
    private int money;           //金币余额
    private boolean success;     //状态
    private String data;         //状态
    private int type;            //标识
    private int tRole;            //角色
    private int live;            //直播板块开关
    private String remarks1;     //备注 预留备用
    private int zfboff;          //支付宝关闭
    private int wxoff;           //微信关闭
    private double jinbi;        //金币
    private int reale;           //是否需要实名认证
    private int allow;           //允许发布权限
    private int lvideo;          //短视频
    private int inreview;       //头像审核中

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getInreview() {
        return inreview;
    }

    public void setInreview(int inreview) {
        this.inreview = inreview;
    }

    public int getLvideo() {
        return lvideo;
    }

    public void setLvideo(int lvideo) {
        this.lvideo = lvideo;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public int getReale() {
        return reale;
    }

    public void setReale(int reale) {
        this.reale = reale;
    }

    public double getJinbi() {
        return jinbi;
    }

    public void setJinbi(double jinbi) {
        this.jinbi = jinbi;
    }

    private String UserSig;      //登录IM签名

    public String getUserSig() {
        return UserSig;
    }

    public void setUserSig(String userSig) {
        UserSig = userSig;
    }

    public int getZfboff() {
        return zfboff;
    }

    public void setZfboff(int zfboff) {
        this.zfboff = zfboff;
    }

    public int getWxoff() {
        return wxoff;
    }

    public void setWxoff(int wxoff) {
        this.wxoff = wxoff;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int gettRole() {
        return tRole;
    }

    public void settRole(int tRole) {
        this.tRole = tRole;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getGame() {
        return game;
    }

    public void setGame(int game) {
        this.game = game;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsreid() {
        return usreid;
    }

    public void setUsreid(String usreid) {
        this.usreid = usreid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getVideotype() {
        return videotype;
    }

    public void setVideotype(int videotype) {
        this.videotype = videotype;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getLive() {
        return live;
    }

    public void setLive(int live) {
        this.live = live;
    }

    public String getRemarks1() {
        return remarks1;
    }

    public void setRemarks1(String remarks1) {
        this.remarks1 = remarks1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Mesresult{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", usreid='" + usreid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", msg='" + msg + '\'' +
                ", sex='" + sex + '\'' +
                ", ok='" + ok + '\'' +
                ", alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", refresh='" + refresh + '\'' +
                ", picture='" + picture + '\'' +
                ", code='" + code + '\'' +
                ", duedate='" + duedate + '\'' +
                ", avatar='" + avatar + '\'' +
                ", truename='" + truename + '\'' +
                ", member=" + member +
                ", game=" + game +
                ", vip=" + vip +
                ", level=" + level +
                ", video=" + video +
                ", cover=" + cover +
                ", photo=" + photo +
                ", videotype=" + videotype +
                ", money=" + money +
                ", success=" + success +
                ", data='" + data + '\'' +
                ", type=" + type +
                ", tRole=" + tRole +
                ", live=" + live +
                ", remarks1='" + remarks1 + '\'' +
                ", zfboff=" + zfboff +
                ", wxoff=" + wxoff +
                ", jinbi=" + jinbi +
                ", reale=" + reale +
                ", allow=" + allow +
                ", lvideo=" + lvideo +
                ", inreview=" + inreview +
                ", UserSig='" + UserSig + '\'' +
                '}';
    }
}
