package com.tencent.opensource.model;

public class Party {

    private int id;
    private int userid;
    private String title;
    private String tdesc;
    private int status;
    private String datetime;
    private String partytime;
    private String address;
    private int partynumbe;
    private int partyenumbe;
    private String partyadvanced;
    private String msg;
    private String cover;
    private int type;
    private int code;
    private int finish;
    private member member;

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public member getMember() {
        return member;
    }

    public void setMember(member member) {
        this.member = member;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
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

    public String getPartytime() {
        return partytime;
    }

    public void setPartytime(String partytime) {
        this.partytime = partytime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPartynumbe() {
        return partynumbe;
    }

    public void setPartynumbe(int partynumbe) {
        this.partynumbe = partynumbe;
    }

    public int getPartyenumbe() {
        return partyenumbe;
    }

    public void setPartyenumbe(int partyenumbe) {
        this.partyenumbe = partyenumbe;
    }

    public String getPartyadvanced() {
        return partyadvanced;
    }

    public void setPartyadvanced(String partyadvanced) {
        this.partyadvanced = partyadvanced;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
