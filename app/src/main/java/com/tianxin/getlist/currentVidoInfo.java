package com.tianxin.getlist;

import java.io.Serializable;

public class currentVidoInfo implements Serializable {
    public String id;
    public String vip;
    public String index;
    public String title;
    public String description;
    public String clarity;
    public String grade;
    public String year;
    public String actor;
    public String news;
    public String country;
    public String pic;
    public String vipFragment;

    @Override
    public String toString() {
        return "currentVidoInfo{" +
                "id='" + id + '\'' +
                ", vip='" + vip + '\'' +
                ", index='" + index + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", clarity='" + clarity + '\'' +
                ", grade='" + grade + '\'' +
                ", year='" + year + '\'' +
                ", actor='" + actor + '\'' +
                ", news='" + news + '\'' +
                ", country='" + country + '\'' +
                ", pic='" + pic + '\'' +
                ", vipFragment='" + vipFragment + '\'' +
                '}';
    }
}
