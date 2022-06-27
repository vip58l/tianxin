package com.tianxin.getlist;

import java.io.Serializable;

/**
 * 获取视频地址类
 */
public class GradeTag implements Serializable {
    public String uid;
    public String fid;
    public String avatar;
    public String nickname;
    public String sex;
    public String isAuth;
    public String sort;
    public String signature;
    public String age;
    public String city;
    public String type;
    public String onlineStatus;
    public String videoAmount;
    public String voiceAmount;
    public String grade;
    public String ifVip;
    public String backgroundVideo;
    public String height;
    public String lastOpenTime;
    public String videoStatus;
    public String inviteVideo;
    public String surplusUnsend;
    public String stampAmount;
    public String personalPicUrl;
    public String personalVideoUrl;
    public String chatStatus;
    public String content;
    public String thumUrl;
    public String videoUrl;
    public String blurryUrl;

    @Override
    public String toString() {
        return "GradeTag{" +
                "uid='" + uid + '\'' +
                ", fid='" + fid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", isAuth='" + isAuth + '\'' +
                ", sort='" + sort + '\'' +
                ", signature='" + signature + '\'' +
                ", age='" + age + '\'' +
                ", city='" + city + '\'' +
                ", type='" + type + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", videoAmount='" + videoAmount + '\'' +
                ", voiceAmount='" + voiceAmount + '\'' +
                ", grade='" + grade + '\'' +
                ", ifVip='" + ifVip + '\'' +
                ", backgroundVideo='" + backgroundVideo + '\'' +
                ", height='" + height + '\'' +
                ", lastOpenTime='" + lastOpenTime + '\'' +
                ", videoStatus='" + videoStatus + '\'' +
                ", inviteVideo='" + inviteVideo + '\'' +
                ", surplusUnsend='" + surplusUnsend + '\'' +
                ", stampAmount='" + stampAmount + '\'' +
                ", personalPicUrl='" + personalPicUrl + '\'' +
                ", personalVideoUrl='" + personalVideoUrl + '\'' +
                ", chatStatus='" + chatStatus + '\'' +
                ", content='" + content + '\'' +
                ", thumUrl='" + thumUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", blurryUrl='" + blurryUrl + '\'' +
                '}';
    }
}
