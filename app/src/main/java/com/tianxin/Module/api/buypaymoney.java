/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/7 0007
 */


package com.tianxin.Module.api;

public class buypaymoney {
    private String id;
    private String title;
    private String money1;
    private String money2;
    private String money3;
    private int days;
    private String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney1() {
        return money1;
    }

    public void setMoney1(String money1) {
        this.money1 = money1;
    }

    public String getMoney2() {
        return money2;
    }

    public void setMoney2(String money2) {
        this.money2 = money2;
    }

    public String getMoney3() {
        return money3;
    }

    public void setMoney3(String money3) {
        this.money3 = money3;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
