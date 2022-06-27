package com.tencent.opensource.model;

public class givelist {
    String id;
    String userid;
    String touserid;
    String type;
    String tid;
    String status;
    String msg;
    String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTouserid() {
        return touserid;
    }

    public void setTouserid(String touserid) {
        this.touserid = touserid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "givelist{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", touserid='" + touserid + '\'' +
                ", type='" + type + '\'' +
                ", tid='" + tid + '\'' +
                ", status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
