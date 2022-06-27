package com.tianxin.getlist;

import java.io.Serializable;

public class Advert implements Serializable {
    public String id;
    public String linkType;
    public String desc;
    public String title;
    public String linkUrl;
    public String picUrl;
    public String videoUrl;
    public String type;
    public String titleColor;
    public String descColor;
    public String buttonColor;
    public String buttonTextColor;
    public String buttonText;
    public String picHeight;
    public String picWidth;

    @Override
    public String toString() {
        return "Advert{" +
                "id='" + id + '\'' +
                ", linkType='" + linkType + '\'' +
                ", desc='" + desc + '\'' +
                ", title='" + title + '\'' +
                ", linkUrl='" + linkUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", type='" + type + '\'' +
                ", titleColor='" + titleColor + '\'' +
                ", descColor='" + descColor + '\'' +
                ", buttonColor='" + buttonColor + '\'' +
                ", buttonTextColor='" + buttonTextColor + '\'' +
                ", buttonText='" + buttonText + '\'' +
                ", picHeight='" + picHeight + '\'' +
                ", picWidth='" + picWidth + '\'' +
                '}';
    }
}
