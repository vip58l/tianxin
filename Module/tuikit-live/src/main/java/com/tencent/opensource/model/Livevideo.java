/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/23 0023
 */


package com.tencent.opensource.model;

import java.io.Serializable;

/**
 * 播放数据源公共类父类
 */
public class Livevideo implements Serializable {
    private String id;
    private String userid;
    private String title;
    private String fltitle;
    private String avatar;
    private String picurl;    //头像1
    private String bigpicurl; //背景
    private String picuser;   //头像2
    private String playurl;   //播放地址
    private String time;      //播放时长
    private String alias;
    private String anum;
    private String pnum;
    private String fnum;
    private String znum;
    private int status;
    private int tencent;    //腾讯云背景标识
    private int pictencent; //腾讯云头像标识
    private String sec;
    private String datetime;

    private int play;
    private String qq;
    private String wx;
    private String phoen;
    private String ip;
    private int relevance;
    private int jinbi;
    private String playtest;

    private String province;
    private String city;
    private String district;
    private String address;
    private String jwd;
    private int follow;
    private int type; //0普通 1付费+模糊 2购物 3模糊 4 特别推荐 5 6 ..
    private int fenleijb; //内容分类
    private personal personal;
    private member member;

    public member getMember() {
        return member;
    }

    public void setMember(member member) {
        this.member = member;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getDadetime() {
        return datetime == null ? "" : datetime;
    }

    public void setDadetime(String dadetime) {
        this.datetime = dadetime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getBigpicurl() {
        return bigpicurl;
    }

    public void setBigpicurl(String bigpicurl) {
        this.bigpicurl = bigpicurl;
    }

    public String getPicuser() {
        return picuser;
    }

    public void setPicuser(String picuser) {
        this.picuser = picuser;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAnum() {
        return anum;
    }

    public void setAnum(String anum) {
        this.anum = anum;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getFnum() {
        return fnum;
    }

    public void setFnum(String fnum) {
        this.fnum = fnum;
    }

    public String getZnum() {
        return znum;
    }

    public void setZnum(String znum) {
        this.znum = znum;
    }

    public int getTencent() {
        return tencent;
    }

    public void setTencent(int tencent) {
        this.tencent = tencent;
    }

    public int getPictencent() {
        return pictencent;
    }

    public void setPictencent(int pictencent) {
        this.pictencent = pictencent;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public com.tencent.opensource.model.personal getPersonal() {
        return personal;
    }

    public void setPersonal(com.tencent.opensource.model.personal personal) {
        this.personal = personal;
    }

    public int getFenleijb() {
        return fenleijb;
    }

    public void setFenleijb(int fenleijb) {
        this.fenleijb = fenleijb;
    }

    public String getFltitle() {
        return fltitle;
    }

    public void setFltitle(String fltitle) {
        this.fltitle = fltitle;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getPhoen() {
        return phoen;
    }

    public void setPhoen(String phoen) {
        this.phoen = phoen;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getRelevance() {
        return relevance;
    }

    public void setRelevance(int relevance) {
        this.relevance = relevance;
    }

    public int getJinbi() {
        return jinbi;
    }

    public void setJinbi(int jinbi) {
        this.jinbi = jinbi;
    }

    public String getPlaytest() {
        return playtest;
    }

    public void setPlaytest(String playtest) {
        this.playtest = playtest;
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

    @Override
    public String toString() {
        return "videolist{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", fltitle='" + fltitle + '\'' +
                ", avatar='" + avatar + '\'' +
                ", picurl='" + picurl + '\'' +
                ", bigpicurl='" + bigpicurl + '\'' +
                ", picuser='" + picuser + '\'' +
                ", playurl='" + playurl + '\'' +
                ", time='" + time + '\'' +
                ", alias='" + alias + '\'' +
                ", anum='" + anum + '\'' +
                ", pnum='" + pnum + '\'' +
                ", fnum='" + fnum + '\'' +
                ", znum='" + znum + '\'' +
                ", status=" + status +
                ", tencent=" + tencent +
                ", pictencent=" + pictencent +
                ", sec='" + sec + '\'' +
                ", datetime='" + datetime + '\'' +
                ", play=" + play +
                ", qq='" + qq + '\'' +
                ", wx='" + wx + '\'' +
                ", phoen='" + phoen + '\'' +
                ", ip='" + ip + '\'' +
                ", relevance=" + relevance +
                ", jinbi=" + jinbi +
                ", playtest='" + playtest + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", jwd='" + jwd + '\'' +
                ", type=" + type +
                ", fenleijb=" + fenleijb +
                ", personal=" + personal +
                ", member=" + member +
                '}';
    }
}

