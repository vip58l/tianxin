/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/6 0006
 */


package com.tianxin.Module.api;

import com.tencent.opensource.model.member;

public class goldcoin {
    private int id;
    private int userid;
    private int balance;
    private int money;
    private int miniamount;
    private int state;
    private String datetime;
    private member member;
    private String msg;

    @Override
    public String toString() {
        return "goldcoin{" +
                "id=" + id +
                ", userid=" + userid +
                ", balance=" + balance +
                ", money=" + money +
                ", miniamount=" + miniamount +
                ", state=" + state +
                ", datetime='" + datetime + '\'' +
                ", member=" + member +
                ", msg='" + msg + '\'' +
                '}';
    }

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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public com.tencent.opensource.model.member getMember() {
        return member;
    }

    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
