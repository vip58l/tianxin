package com.tencent.opensource.model;

public class Citychat {
    private int id;
    private int userid;
    private int cityid;
    private String title;
    private String alias;
    private String msg;
    private int status;
    private String icon;
    private String avatar;
    private String picture;
    private String datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "Citychat{" +
                "id=" + id +
                ", userid=" + userid +
                ", cityid=" + cityid +
                ", title='" + title + '\'' +
                ", alias='" + alias + '\'' +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                ", icon='" + icon + '\'' +
                ", avatar='" + avatar + '\'' +
                ", picture='" + picture + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
