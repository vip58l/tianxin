/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utils;

public class goldcoin {
    private int id;
    private int userid;
    private int balance;
    private int money;
    private int miniamount;
    private int state;
    private int video;
    private int Audio;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMiniamount() {
        return miniamount;
    }

    public void setMiniamount(int miniamount) {
        this.miniamount = miniamount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
    }

    public int getAudio() {
        return Audio;
    }

    public void setAudio(int audio) {
        Audio = audio;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "goldcoin{" +
                "id=" + id +
                ", userid=" + userid +
                ", balance=" + balance +
                ", money=" + money +
                ", miniamount=" + miniamount +
                ", state=" + state +
                ", video=" + video +
                ", Audio=" + Audio +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
