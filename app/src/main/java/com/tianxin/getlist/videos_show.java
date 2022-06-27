package com.tianxin.getlist;

import java.io.Serializable;

public class videos_show implements Serializable {
    public String serialVersionUID;
    public String id;
    public String title;
    public String index;
    public String pic;
    public String description;
    public String news;
    public String grade;
    public String picNew;
    public String rank;
    public String vipFragment;

    @Override
    public String toString() {
        return "videos_show{" +
                "serialVersionUID='" + serialVersionUID + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", index='" + index + '\'' +
                ", pic='" + pic + '\'' +
                ", description='" + description + '\'' +
                ", news='" + news + '\'' +
                ", grade='" + grade + '\'' +
                ", picNew='" + picNew + '\'' +
                ", rank='" + rank + '\'' +
                ", vipFragment='" + vipFragment + '\'' +
                '}';
    }
}
