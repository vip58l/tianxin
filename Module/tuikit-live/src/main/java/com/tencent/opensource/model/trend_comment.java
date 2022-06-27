package com.tencent.opensource.model;

public class trend_comment {
    private int id;
    private int trendid;
    private int suserid;
    private int userid;
    private String mcontent;
    private int status;
    private int type;
    private int count;
    private String datetime;
    private member member;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public com.tencent.opensource.model.member getMember() {
        return member;
    }

    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrendid() {
        return trendid;
    }

    public void setTrendid(int trendid) {
        this.trendid = trendid;
    }

    public int getSuserid() {
        return suserid;
    }

    public void setSuserid(int suserid) {
        this.suserid = suserid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getMcontent() {
        return mcontent;
    }

    public void setMcontent(String mcontent) {
        this.mcontent = mcontent;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
