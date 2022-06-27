/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/30 0030
 */


package com.tianxin.Module.api;

public class reward {
    private int id;
    private int userid;
    private String balance;
    private String money;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "reward{" +
                "id=" + id +
                ", userid=" + userid +
                ", balance='" + balance + '\'' +
                ", money='" + money + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
