/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/24 0024
 */


package com.tencent.opensource.model;

import java.io.Serializable;

public class curriculum implements Serializable {
    private int id;
    private String userid;
    private String title;
    private String msg;
    private String tag;
    private String pic;
    private String video;
    private String duration;
    private String download;
    private int type;
    private int status;
    private String money;
    private String price;
    private int pay;
    private int play;
    private int sellout;
    private int tencent;
    private String datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getPlay() {
        return play;
    }

    public void setPlay(int play) {
        this.play = play;
    }

    public int getSellout() {
        return sellout;
    }

    public void setSellout(int sellout) {
        this.sellout = sellout;
    }

    public int getTencent() {
        return tencent;
    }

    public void setTencent(int tencent) {
        this.tencent = tencent;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    @Override
    public String toString() {
        return "curriculum{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", tag='" + tag + '\'' +
                ", pic='" + pic + '\'' +
                ", video='" + video + '\'' +
                ", duration='" + duration + '\'' +
                ", download='" + download + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", money='" + money + '\'' +
                ", price='" + price + '\'' +
                ", pay=" + pay +
                ", play=" + play +
                ", sellout=" + sellout +
                ", tencent=" + tencent +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
