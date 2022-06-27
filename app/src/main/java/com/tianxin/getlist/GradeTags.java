package com.tianxin.getlist;

import java.io.Serializable;

public class GradeTags implements Serializable {
    public String isPay;
    public String uid;
    public String fid;
    public String avatar;
    public String nickname;
    public String sex;
    public String isAuth;
    public String age;
    public String signature;
    public String onlineStatus;
    public String videoAmount;
    public String voiceAmount;
    public String ifFollow;
    public String did;
    public String createtime;
    public String timestamp;
    public String type;
    public String content;
    public String thumUrl;
    public String videoUrl;
    public String blurryUrl;
    public String duration;
    public String height;
    public String width;
    public String giftAmount;
    public String commentAmount;
    public String praiseAmount;
    public String playAmount;
    public String propId;
    public String propNum;
    public String ifFree;

    @Override
    public String toString() {
        return "GradeTags{" +
                "isPay='" + isPay + '\'' +
                ", uid='" + uid + '\'' +
                ", fid='" + fid + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", isAuth='" + isAuth + '\'' +
                ", age='" + age + '\'' +
                ", signature='" + signature + '\'' +
                ", onlineStatus='" + onlineStatus + '\'' +
                ", videoAmount='" + videoAmount + '\'' +
                ", voiceAmount='" + voiceAmount + '\'' +
                ", ifFollow='" + ifFollow + '\'' +
                ", did='" + did + '\'' +
                ", createtime='" + createtime + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", thumUrl='" + thumUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", blurryUrl='" + blurryUrl + '\'' +
                ", duration='" + duration + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                ", giftAmount='" + giftAmount + '\'' +
                ", commentAmount='" + commentAmount + '\'' +
                ", praiseAmount='" + praiseAmount + '\'' +
                ", playAmount='" + playAmount + '\'' +
                ", propId='" + propId + '\'' +
                ", propNum='" + propNum + '\'' +
                ", ifFree='" + ifFree + '\'' +
                '}';
    }
}
