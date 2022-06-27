/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/31 0031
 */


package com.tianxin.Module.api;

public class wmoney {
    private int id;
    private String money;
    private String msg;
    private String datetime;

    @Override
    public String toString() {
        return "wmoney{" +
                "id=" + id +
                ", money='" + money + '\'' +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
