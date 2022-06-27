package com.tencent.qcloud.costransferpractice.common.base;

public class BUserinfo {
    private int id;
    private int userid;
    private double balance;
    private double money;
    private int miniamount;
    private String datetime;
    private int status;
    private double Audio;
    private double video;

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

    public int getMiniamount() {
        return miniamount;
    }

    public void setMiniamount(int miniamount) {
        this.miniamount = miniamount;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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

    @Override
    public String toString() {
        return "BUserinfo{" +
                "id=" + id +
                ", userid=" + userid +
                ", balance=" + balance +
                ", money=" + money +
                ", miniamount=" + miniamount +
                ", datetime='" + datetime + '\'' +
                ", status=" + status +
                ", Audio=" + Audio +
                ", video=" + video +
                '}';
    }
}

