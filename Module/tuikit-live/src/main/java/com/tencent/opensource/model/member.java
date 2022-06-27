/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/26 0026
 */


package com.tencent.opensource.model;

import java.io.Serializable;
import java.util.List;

/**
 * 个人相关信息
 */
public class member implements Serializable {
    private int id;
    private String tvname;
    private String username;
    private String password;
    private String rpassword;
    private String mobile;
    private String truename;
    private String parent; //推荐人
    private int sex;       //性别
    private String jinbi;
    private int status;    //实名状态
    private String msg;
    private String picture; //头像
    private String avatar;  //微信头像
    private String pic;     //生活图片
    private int tencent;
    private int level;
    private String wx;
    private String qq;
    private String zfb;
    private String datetime;

    private int vip;
    private String duedate;
    private String province;
    private String city;
    private String district;
    private String address;
    private String jwd;
    private String configmsg;
    private int evaluate;
    private int myconun;
    private int tacount;
    private int online;
    private String logintime;
    private int allow;
    private int tRole;
    private int reale;
    private int video;
    private int audio;
    private int givecount;
    private int matchmaker;
    private int inreview;    //头像审核中
    private int memberlogs;  //标识看过联系方式
    private int kefu;        //客服标记

    public int getKefu() {
        return kefu;
    }

    public void setKefu(int kefu) {
        this.kefu = kefu;
    }

    public int getMemberlogs() {
        return memberlogs;
    }

    public void setMemberlogs(int memberlogs) {
        this.memberlogs = memberlogs;
    }

    public int getInreview() {
        return inreview;
    }

    public void setInreview(int inreview) {
        this.inreview = inreview;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getMatchmaker() {
        return matchmaker;
    }

    public void setMatchmaker(int matchmaker) {
        this.matchmaker = matchmaker;
    }

    public int getGivecount() {
        return givecount;
    }

    public void setGivecount(int givecount) {
        this.givecount = givecount;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public int getReale() {
        return reale;
    }

    public void setReale(int reale) {
        this.reale = reale;
    }

    /**
     * 个人相关标签 //详情资料
     */
    private personal personal;
    /**
     * 关注对像
     */
    private followlist followlist;
    /**
     * 详情图片
     */
    private List<perimg> perimg;
    /**
     * 帮助了多少人
     */
    private gethelp help;
    //分类标签
    private List<videotype> videotype;

    /**
     * 显示隐私设置
     */
    private videoPush videoPush;

    public videoPush getVideoPush() {
        return videoPush;
    }

    public void setVideoPush(com.tencent.opensource.model.videoPush videoPush) {
        this.videoPush = videoPush;
    }

    public int gettRole() {
        return tRole;
    }

    public void settRole(int tRole) {
        this.tRole = tRole;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTvname() {
        return tvname;
    }

    public void setTvname(String tvname) {
        this.tvname = tvname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRpassword() {
        return rpassword;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getJinbi() {
        return jinbi;
    }

    public void setJinbi(String jinbi) {
        this.jinbi = jinbi;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getTencent() {
        return tencent;
    }

    public void setTencent(int tencent) {
        this.tencent = tencent;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getZfb() {
        return zfb;
    }

    public void setZfb(String zfb) {
        this.zfb = zfb;
    }

    public String getDadetime() {
        return datetime;
    }

    public void setDadetime(String dadetime) {
        this.datetime = dadetime;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    public String getConfigmsg() {
        return configmsg;
    }

    public void setConfigmsg(String configmsg) {
        this.configmsg = configmsg;
    }

    public int getMyconun() {
        return myconun;
    }

    public void setMyconun(int myconun) {
        this.myconun = myconun;
    }

    public int getTacount() {
        return tacount;
    }

    public void setTacount(int tacount) {
        this.tacount = tacount;
    }

    public com.tencent.opensource.model.personal getPersonal() {
        return personal;
    }

    public void setPersonal(com.tencent.opensource.model.personal personal) {
        this.personal = personal;
    }

    public com.tencent.opensource.model.followlist getFollowlist() {
        return followlist;
    }

    public void setFollowlist(com.tencent.opensource.model.followlist followlist) {
        this.followlist = followlist;
    }

    public List<com.tencent.opensource.model.perimg> getPerimg() {
        return perimg;
    }

    public void setPerimg(List<com.tencent.opensource.model.perimg> perimg) {
        this.perimg = perimg;
    }

    public gethelp getHelp() {
        return help;
    }

    public void setHelp(gethelp help) {
        this.help = help;
    }

    public List<com.tencent.opensource.model.videotype> getVideotype() {
        return videotype;
    }

    public void setVideotype(List<com.tencent.opensource.model.videotype> videotype) {
        this.videotype = videotype;
    }

    @Override
    public String toString() {
        return "member{" +
                "id=" + id +
                ", tvname='" + tvname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", rpassword='" + rpassword + '\'' +
                ", mobile='" + mobile + '\'' +
                ", truename='" + truename + '\'' +
                ", parent='" + parent + '\'' +
                ", sex=" + sex +
                ", jinbi='" + jinbi + '\'' +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                ", picture='" + picture + '\'' +
                ", avatar='" + avatar + '\'' +
                ", pic='" + pic + '\'' +
                ", tencent=" + tencent +
                ", level=" + level +
                ", wx='" + wx + '\'' +
                ", qq='" + qq + '\'' +
                ", zfb='" + zfb + '\'' +
                ", datetime='" + datetime + '\'' +
                ", vip=" + vip +
                ", duedate='" + duedate + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", jwd='" + jwd + '\'' +
                ", configmsg='" + configmsg + '\'' +
                ", evaluate=" + evaluate +
                ", myconun=" + myconun +
                ", tacount=" + tacount +
                ", online=" + online +
                ", logintime='" + logintime + '\'' +
                ", allow=" + allow +
                ", tRole=" + tRole +
                ", reale=" + reale +
                ", video=" + video +
                ", audio=" + audio +
                ", givecount=" + givecount +
                ", matchmaker=" + matchmaker +
                ", inreview=" + inreview +
                ", memberlogs=" + memberlogs +
                ", kefu=" + kefu +
                ", personal=" + personal +
                ", followlist=" + followlist +
                ", perimg=" + perimg +
                ", help=" + help +
                ", videotype=" + videotype +
                ", videoPush=" + videoPush +
                '}';
    }
}
