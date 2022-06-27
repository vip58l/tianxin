/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/23 0023
 */


package com.tianxin.Module.api;

import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;

import java.io.Serializable;

/**
 * 播放数据源公共类父类
 */
public class videolist implements Serializable {
    private String id;
    private String userid;
    private String title;
    private String fltitle;
    private String picurl;    //头像1
    private String bigpicurl; //背景图片
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
    private String dadetime;

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

    public String getDadetime() {
        return dadetime == null ? "" : dadetime;
    }

    public void setDadetime(String dadetime) {
        this.dadetime = dadetime;
    }

    @Override
    public String toString() {
        return "videolist{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
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
                ", tencent=" + tencent +
                ", pictencent=" + pictencent +
                ", sec='" + sec + '\'' +
                ", dadetime=" + dadetime +
                ", type=" + type +
                ", personal=" + personal +
                '}';
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
}

