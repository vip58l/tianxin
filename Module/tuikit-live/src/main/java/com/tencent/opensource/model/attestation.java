/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/26 0026
 */


package com.tencent.opensource.model;

public class attestation {
    public String userid;
    public int status;
    public int type;
    public String card1;
    public String card2;
    public String card3;
    public int tencent;
    public String datetime;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getCard1() {
        return card1;
    }

    public void setCard1(String card1) {
        this.card1 = card1;
    }

    public String getCard2() {
        return card2;
    }

    public void setCard2(String card2) {
        this.card2 = card2;
    }

    public String getCard3() {
        return card3;
    }

    public void setCard3(String card3) {
        this.card3 = card3;
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
        return "attestation{" +
                "userid='" + userid + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", card1='" + card1 + '\'' +
                ", card2='" + card2 + '\'' +
                ", card3='" + card3 + '\'' +
                ", tencent=" + tencent +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
