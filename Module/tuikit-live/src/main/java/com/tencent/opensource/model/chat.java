package com.tencent.opensource.model;

public class chat {
    int id;
    int userid;
    int idx;
    String groupid;;
    int groupallow;
    int free;
    int type;
    int sex;
    String message;
    int page;
    String msg;
    String datetime;

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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public int getGroupallow() {
        return groupallow;
    }

    public void setGroupallow(int groupallow) {
        this.groupallow = groupallow;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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
        return "chat{" +
                "id=" + id +
                ", userid=" + userid +
                ", idx=" + idx +
                ", groupid='" + groupid + '\'' +
                ", groupallow=" + groupallow +
                ", free=" + free +
                ", type=" + type +
                ", sex=" + sex +
                ", message='" + message + '\'' +
                ", page=" + page +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
