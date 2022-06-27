package com.tencent.opensource.model;

public class LiveRoom {
    private int id;
    private int userid;
    private int type;
    private int ktv;
    private String datetime;
    private String exitRoom	;
    private member member;

    public int getKtv() {
        return ktv;
    }

    public void setKtv(int ktv) {
        this.ktv = ktv;
    }

    public String getExitRoom() {
        return exitRoom;
    }

    public void setExitRoom(String exitRoom) {
        this.exitRoom = exitRoom;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
