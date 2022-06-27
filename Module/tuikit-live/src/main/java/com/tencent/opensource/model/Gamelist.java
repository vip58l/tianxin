package com.tencent.opensource.model;

public class Gamelist {
    private int id;
    private int gametype;
    private int userid;
    private int status;
    private String name;
    private String descshow;
    private double money;
    private int quantity;
    private int duration;
    private String msg;
    private String datetime;
    private String alias;
    private int frequency;;
    private member member;
    private Gametitle gametitle;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGametype() {
        return gametype;
    }

    public void setGametype(int gametype) {
        this.gametype = gametype;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescshow() {
        return descshow;
    }

    public void setDescshow(String descshow) {
        this.descshow = descshow;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public Gametitle getGametitle() {
        return gametitle;
    }

    public void setGametitle(Gametitle gametitle) {
        this.gametitle = gametitle;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "gamelist{" +
                "id=" + id +
                ", gametype=" + gametype +
                ", userid=" + userid +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", descshow='" + descshow + '\'' +
                ", money='" + money + '\'' +
                ", quantity=" + quantity +
                ", duration=" + duration +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                ", member=" + member +
                '}';
    }
}
