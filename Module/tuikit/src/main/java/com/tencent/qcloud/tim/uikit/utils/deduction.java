/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utils;

public class deduction {
    private int status;
    private int money;
    private String msg;
    private String usermoney;
    private String type;
    private String sendoutid;
    private String receiveid;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsermoney() {
        return usermoney;
    }

    public void setUsermoney(String usermoney) {
        this.usermoney = usermoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSendoutid() {
        return sendoutid;
    }

    public void setSendoutid(String sendoutid) {
        this.sendoutid = sendoutid;
    }

    public String getReceiveid() {
        return receiveid;
    }

    public void setReceiveid(String receiveid) {
        this.receiveid = receiveid;
    }
}
