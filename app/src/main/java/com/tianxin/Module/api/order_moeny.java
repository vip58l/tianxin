/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/31 0031
 */


package com.tianxin.Module.api;

public class order_moeny {
    private int id;
    private int userid;
    private String money;
    private int type;
    private String msg;
    private String sexplain;
    private int status;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSexplain() {
        return sexplain;
    }

    public void setSexplain(String sexplain) {
        this.sexplain = sexplain;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "order_moeny{" +
                "id=" + id +
                ", userid=" + userid +
                ", money='" + money + '\'' +
                ", type=" + type +
                ", msg='" + msg + '\'' +
                ", sexplain='" + sexplain + '\'' +
                ", status=" + status +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
