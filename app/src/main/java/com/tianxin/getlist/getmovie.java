package com.tianxin.getlist;

import java.io.Serializable;

public class getmovie implements Serializable {
    public String name;
    public String ms;
    public String longMs;
    public String imgUrl;
    public String tag;
    public String times;
    public String type;
    public String mvUrl;
    public String tryUrl;
    public String mvType;
    public String detailimgurl;
    public String vedioId;
    @Override
    public String toString() {
        return "getmovie{" +
                "name='" + name + '\'' +
                ", ms='" + ms + '\'' +
                ", longMs='" + longMs + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", tag='" + tag + '\'' +
                ", times='" + times + '\'' +
                ", type='" + type + '\'' +
                ", mvUrl='" + mvUrl + '\'' +
                ", tryUrl='" + tryUrl + '\'' +
                ", mvType='" + mvType + '\'' +
                ", detailimgurl='" + detailimgurl + '\'' +
                ", vedioId='" + vedioId + '\'' +
                '}';
    }
}
