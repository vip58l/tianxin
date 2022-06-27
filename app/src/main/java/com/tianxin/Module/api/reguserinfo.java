/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/1 0001
 */


package com.tianxin.Module.api;

public class reguserinfo {
    private String truename;
    private String username;
    private String tvname;
    private String userid;
    private String password;
    private String password2;
    private String parent;
    private String wx;
    private String qq;
    private int sex;
    private int tRole;
    private String code;
    private String pesigntext;
    private int vip;
    private String picture;
    private String avatar;
    private int allow;
    private String duedate;
    private int reale;
    private int inreview;
    private String model;

    public int getInreview() {
        return inreview;
    }

    public void setInreview(int inreview) {
        this.inreview = inreview;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public int getReale() {
        return reale;
    }

    public void setReale(int reale) {
        this.reale = reale;
    }

    public int gettRole() {
        return tRole;
    }

    public void settRole(int tRole) {
        this.tRole = tRole;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTvname() {
        return tvname;
    }

    public void setTvname(String tvname) {
        this.tvname = tvname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPesigntext() {
        return pesigntext;
    }

    public void setPesigntext(String pesigntext) {
        this.pesigntext = pesigntext;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    @Override
    public String toString() {
        return "reguserinfo{" +
                "truename='" + truename + '\'' +
                ", username='" + username + '\'' +
                ", tvname='" + tvname + '\'' +
                ", userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                ", wx='" + wx + '\'' +
                ", qq='" + qq + '\'' +
                ", sex=" + sex +
                ", code='" + code + '\'' +
                ", pesigntext='" + pesigntext + '\'' +
                ", vip=" + vip +
                '}';
    }
}

