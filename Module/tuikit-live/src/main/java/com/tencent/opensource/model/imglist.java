/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/1 0001
 */


package com.tencent.opensource.model;

import java.io.Serializable;

public class imglist implements Serializable {
    private String id;
    private String title;
    private String userid;
    private String pic;
    private String bgpic;
    private int status;
    private int tencent;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getBgpic() {
        return bgpic;
    }

    public void setBgpic(String bgpic) {
        this.bgpic = bgpic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "imglist{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", userid='" + userid + '\'' +
                ", pic='" + pic + '\'' +
                ", bgpic='" + bgpic + '\'' +
                ", status=" + status +
                ", tencent=" + tencent +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
