package com.tianxin.getlist;

import org.json.JSONArray;

public class listDynamics {
    public String msg;
    public String code;
    public JSONArray data;
    public String videoUrl;
    public String coverUrl;
    public String avatar;
    public String nickname;
    public String age;
    public String signature;

    @Override
    public String toString() {
        return "listDynamics{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                ", videoUrl='" + videoUrl + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", age='" + age + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
