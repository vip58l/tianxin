/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/13 0013
 */


package com.tencent.opensource.model;

import java.io.Serializable;

public class gethelp implements Serializable {
    private int id;
    private int userid;
    private String nametitle;
    private String deqy;
    private int people;
    private int duration;
    private int certificates;
    private String price;
    private String pic;
    private int type;
    private int status;
    private String datetime;
    private int evaluate;

    @Override
    public String toString() {
        return "gethelp{" +
                "id=" + id +
                ", userid=" + userid +
                ", nametitle='" + nametitle + '\'' +
                ", deqy='" + deqy + '\'' +
                ", people=" + people +
                ", duration=" + duration +
                ", certificates=" + certificates +
                ", price='" + price + '\'' +
                ", pic='" + pic + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", datetime='" + datetime + '\'' +
                ", evaluate=" + evaluate +
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

    public String getDeqy() {
        return deqy;
    }

    public void setDeqy(String deqy) {
        this.deqy = deqy;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCertificates() {
        return certificates;
    }

    public void setCertificates(int certificates) {
        this.certificates = certificates;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNametitle() {
        return nametitle;
    }

    public void setNametitle(String nametitle) {
        this.nametitle = nametitle;
    }
}

