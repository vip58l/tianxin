/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/26 0026
 */


package com.tencent.opensource.model;

import java.io.Serializable;

public class followlist implements Serializable {
    private int id;
    private String userid;
    private String mcontent;
    private String touserid;
    private String msg;
    private String datetime;
    private member member;
    private trend trend;

    public com.tencent.opensource.model.trend getTrend() {
        return trend;
    }

    public void setTrend(com.tencent.opensource.model.trend trend) {
        this.trend = trend;
    }

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

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
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

    public com.tencent.opensource.model.member getMember() {
        return member;
    }

    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "followlist{" +
                "id=" + id +
                ", userid='" + userid + '\'' +
                ", mcontent='" + mcontent + '\'' +
                ", touserid='" + touserid + '\'' +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                ", member=" + member +
                ", trend=" + trend +
                '}';
    }
}
