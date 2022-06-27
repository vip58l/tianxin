package com.tencent.opensource.model;

public class amountconfig {
    private int id;
    private int userid;
    private int invest;
    private int status;
    private int agent;
    private int addmoney;
    private int withdraw;
    private int maxmoney;
    private int allow;
    private String quantity;
    private String datetime;

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

    public int getInvest() {
        return invest;
    }

    public void setInvest(int invest) {
        this.invest = invest;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public int getAddmoney() {
        return addmoney;
    }

    public void setAddmoney(int addmoney) {
        this.addmoney = addmoney;
    }

    public int getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(int withdraw) {
        this.withdraw = withdraw;
    }

    public int getMaxmoney() {
        return maxmoney;
    }

    public void setMaxmoney(int maxmoney) {
        this.maxmoney = maxmoney;
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "amountconfig{" +
                "id=" + id +
                ", userid=" + userid +
                ", invest=" + invest +
                ", status=" + status +
                ", agent=" + agent +
                ", addmoney=" + addmoney +
                ", withdraw=" + withdraw +
                ", maxmoney=" + maxmoney +
                ", allow=" + allow +
                ", quantity='" + quantity + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
