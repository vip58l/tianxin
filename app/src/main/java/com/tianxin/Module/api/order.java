/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/28 0028
 */


package com.tianxin.Module.api;

public class order {
    private int id;
    private String orderid;
    private int moneylist;
    private int userid;
    private int receiveid;
    private String username;
    private double money;
    private double paymoney;
    private int znumber;
    private String express;
    private String paytype;
    private int status;
    private int channel;
    private int type;
    private int ok;
    private int vip;
    private double changecurrency;
    private double balance;
    private String msg;
    private String give;
    private String datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getMoneylist() {
        return moneylist;
    }

    public void setMoneylist(int moneylist) {
        this.moneylist = moneylist;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(int receiveid) {
        this.receiveid = receiveid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(double paymoney) {
        this.paymoney = paymoney;
    }

    public int getZnumber() {
        return znumber;
    }

    public void setZnumber(int znumber) {
        this.znumber = znumber;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public double getChangecurrency() {
        return changecurrency;
    }

    public void setChangecurrency(double changecurrency) {
        this.changecurrency = changecurrency;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGive() {
        return give;
    }

    public void setGive(String give) {
        this.give = give;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "order{" +
                "id=" + id +
                ", orderid='" + orderid + '\'' +
                ", moneylist=" + moneylist +
                ", userid=" + userid +
                ", receiveid=" + receiveid +
                ", username='" + username + '\'' +
                ", money=" + money +
                ", paymoney=" + paymoney +
                ", znumber=" + znumber +
                ", express='" + express + '\'' +
                ", paytype='" + paytype + '\'' +
                ", status=" + status +
                ", channel=" + channel +
                ", type=" + type +
                ", ok=" + ok +
                ", vip=" + vip +
                ", changecurrency=" + changecurrency +
                ", balance=" + balance +
                ", msg='" + msg + '\'' +
                ", give='" + give + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
