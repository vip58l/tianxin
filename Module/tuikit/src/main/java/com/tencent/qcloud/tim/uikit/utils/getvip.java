/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utils;

public class getvip {
    private int id;
    private int sex;
    private int status;
    private int vip;

    @Override
    public String toString() {
        return "getvip{" +
                "id=" + id +
                ", sex=" + sex +
                ", status=" + status +
                ", vip=" + vip +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }
}
