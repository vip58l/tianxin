/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/13 0013
 */


package com.tencent.opensource.model;

public class getservice {
    private int id;
    private int userid;
    private String title;
    private String msg;
    private String money;
    private int second;
    private int duration;
    private int status;
    private int type;
    private int volume;
    private String datetime;

    @Override
    public String toString() {
        return "getservice{" +
                "id=" + id +
                ", userid=" + userid +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", money='" + money + '\'' +
                ", second=" + second +
                ", duration=" + duration +
                ", status=" + status +
                ", type=" + type +
                ", volume=" + volume +
                ", datetime='" + datetime + '\'' +
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
