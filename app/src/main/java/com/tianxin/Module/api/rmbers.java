/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/19 0019
 */


package com.tianxin.Module.api;

import com.tencent.opensource.model.member;

public class rmbers {
    private int id;
    private int userid;
    private int sex;
    private int count;
    private int type;
    private String dadetime;
    private member member;

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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDadetime() {
        return dadetime;
    }

    public void setDadetime(String dadetime) {
        this.dadetime = dadetime;
    }


    @Override
    public String toString() {
        return "rmbers{" +
                "id=" + id +
                ", userid=" + userid +
                ", sex=" + sex +
                ", count=" + count +
                ", type=" + type +
                ", dadetime='" + dadetime + '\'' +
                ", member=" + member +
                '}';
    }
}
