package com.tianxin.getlist;

import java.io.Serializable;

public class minivideo implements Serializable {
    public String vid;
    public String uid;
    public String picurl;
    public String bigpicurl;
    public String title;
    public String alias;
    public String picuser;
    public String vnum;
    public String znum;
    public String sec;
    public String playurl;
    public String isLive;
    public String recommend;
    public String channel;

    @Override
    public String toString() {
        return "minivideo{" +
                "vid='" + vid + '\'' +
                ", uid='" + uid + '\'' +
                ", picurl='" + picurl + '\'' +
                ", bigpicurl='" + bigpicurl + '\'' +
                ", title='" + title + '\'' +
                ", alias='" + alias + '\'' +
                ", picuser='" + picuser + '\'' +
                ", vnum='" + vnum + '\'' +
                ", znum='" + znum + '\'' +
                ", sec='" + sec + '\'' +
                ", playurl='" + playurl + '\'' +
                ", isLive='" + isLive + '\'' +
                ", recommend='" + recommend + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPicuser() {
        return picuser;
    }

    public void setPicuser(String picuser) {
        this.picuser = picuser;
    }

    public String getVnum() {
        return vnum;
    }

    public void setVnum(String vnum) {
        this.vnum = vnum;
    }

    public String getZnum() {
        return znum;
    }

    public void setZnum(String znum) {
        this.znum = znum;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getIsLive() {
        return isLive;
    }

    public void setIsLive(String isLive) {
        this.isLive = isLive;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
