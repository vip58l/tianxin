package com.tencent.opensource.model;

public class moneyLog {
    private int id;
    private int userid;
    private int r_userid;
    private double money;
    private double balance;
    private String ip;
    private String msg;
    private int type;
    private String correlationid;
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

    public int getR_userid() {
        return r_userid;
    }

    public void setR_userid(int r_userid) {
        this.r_userid = r_userid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCorrelationid() {
        return correlationid;
    }

    public void setCorrelationid(String correlationid) {
        this.correlationid = correlationid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}