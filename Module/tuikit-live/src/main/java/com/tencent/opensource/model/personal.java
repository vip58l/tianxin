/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/26 0026
 * <p>
 * Copyright 2021 json.cn
 * <p>
 * Copyright 2021 json.cn
 */


/**
 * Copyright 2021 json.cn
 */
package com.tencent.opensource.model;

import android.provider.ContactsContract;

import java.io.Serializable;
import java.util.List;

public class personal implements Serializable {
    private int id;
    private int userid;
    private int age;
    private int height;
    private int weight;
    private String education;
    private String occupation;
    private String mcharacter;
    private String figure;
    private String hobby;
    private String feeling;
    private String constellation;
    private String pree;
    private String cforsds;
    private String pesigntext;
    private String ip;
    private String province;
    private String city;
    private String district;
    private String address;
    private String jwd;

    private int age2;
    private int height2;
    private String pesigntext2;
    private int pallow;

    public int getPallow() {
        return pallow;
    }

    public void setPallow(int pallow) {
        this.pallow = pallow;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public int getHeight2() {
        return height2;
    }

    public void setHeight2(int height2) {
        this.height2 = height2;
    }

    public String getPesigntext2() {
        return pesigntext2;
    }

    public void setPesigntext2(String pesigntext2) {
        this.pesigntext2 = pesigntext2;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getMcharacter() {
        return mcharacter;
    }

    public void setMcharacter(String mcharacter) {
        this.mcharacter = mcharacter;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getPree() {
        return pree;
    }

    public void setPree(String pree) {
        this.pree = pree;
    }

    public String getCforsds() {
        return cforsds;
    }

    public void setCforsds(String cforsds) {
        this.cforsds = cforsds;
    }

    public String getPesigntext() {
        return pesigntext;
    }

    public void setPesigntext(String pesigntext) {
        this.pesigntext = pesigntext;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJwd() {
        return jwd;
    }

    public void setJwd(String jwd) {
        this.jwd = jwd;
    }

    @Override
    public String toString() {
        return "personal{" +
                "id=" + id +
                ", userid=" + userid +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", education='" + education + '\'' +
                ", occupation='" + occupation + '\'' +
                ", mcharacter='" + mcharacter + '\'' +
                ", figure='" + figure + '\'' +
                ", hobby='" + hobby + '\'' +
                ", feeling='" + feeling + '\'' +
                ", constellation='" + constellation + '\'' +
                ", pree='" + pree + '\'' +
                ", cforsds='" + cforsds + '\'' +
                ", pesigntext='" + pesigntext + '\'' +
                ", ip='" + ip + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", jwd='" + jwd + '\'' +
                '}';
    }
}
