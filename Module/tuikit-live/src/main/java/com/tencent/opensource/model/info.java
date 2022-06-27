/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/3 0003
 */


package com.tencent.opensource.model;

public class info {
    private String zone;
    private String phone;
    private String token;
    private String userId;
    private String userSig;
    private String name;
    private String avatar;
    private String sex;
    private boolean autoLogin;

    private int status;
    private double balance;
    private double money;
    private double miniamount;
    private double Audio;
    private double video;

    public double getAudio() {
        return Audio;
    }

    public void setAudio(double audio) {
        Audio = audio;
    }

    public double getVideo() {
        return video;
    }

    public void setVideo(double video) {
        this.video = video;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }

    public int getState() {
        return status;
    }

    public void setState(int state) {
        this.status = state;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getMiniamount() {
        return miniamount;
    }

    public void setMiniamount(double miniamount) {
        this.miniamount = miniamount;
    }

    @Override
    public String toString() {
        return "info{" +
                "zone='" + zone + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", userId='" + userId + '\'' +
                ", userSig='" + userSig + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", autoLogin=" + autoLogin +
                ", status=" + status +
                ", balance=" + balance +
                ", money=" + money +
                ", miniamount=" + miniamount +
                '}';
    }
}
