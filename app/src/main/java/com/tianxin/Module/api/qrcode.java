/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/28 0028
 */


package com.tianxin.Module.api;

public class qrcode {
    private int id;
    private int userid;
    private String name;
    private String account;
    private int type;
    private String qrcode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "qrcode{" +
                "id=" + id +
                ", userid=" + userid +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", type=" + type +
                ", qrcode='" + qrcode + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
