/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/27 0027
 */


package com.tianxin.Module.api;

public class comment {
    private int id;
    private int uid;
    private int trendid;
    private int userid;
    private String mcontent;
    private int status;
    private int type;
    private int suserid;
    private String datetime;

    //获取字段集合值 获取头像，和名称
    private String truename;
    private String tvname;
    private String picture;
    private int tencent;
    private String mcount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getTrendid() {
        return trendid;
    }

    public void setTrendid(int trendid) {
        this.trendid = trendid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSuserid() {
        return suserid;
    }

    public void setSuserid(int suserid) {
        this.suserid = suserid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getTvname() {
        return tvname;
    }

    public void setTvname(String tvname) {
        this.tvname = tvname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getTencent() {
        return tencent;
    }

    public void setTencent(int tencent) {
        this.tencent = tencent;
    }

    public String getMcount() {
        return mcount;
    }

    public void setMcount(String mcount) {
        this.mcount = mcount;
    }

    @Override
    public String toString() {
        return "comment{" +
                "id=" + id +
                ", uid=" + uid +
                ", trendid=" + trendid +
                ", userid=" + userid +
                ", mcontent='" + mcontent + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", suserid=" + suserid +
                ", datetime='" + datetime + '\'' +
                ", truename='" + truename + '\'' +
                ", tvname='" + tvname + '\'' +
                ", picture='" + picture + '\'' +
                ", tencent=" + tencent +
                ", mcount='" + mcount + '\'' +
                '}';
    }
}
