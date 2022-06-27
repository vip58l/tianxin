package com.tianxin.getlist;

import java.io.Serializable;

public class listSmallVideos implements Serializable {
    public String id;
    public String createTime;
    public String desc;
    public String coverImage;
    public String playUrl;
    public String free;
    public String blurUrl;
    public String age;

    @Override
    public String toString() {
        return "listSmallVideos{" +
                "id='" + id + '\'' +
                ", createTime='" + createTime + '\'' +
                ", desc='" + desc + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", free='" + free + '\'' +
                ", blurUrl='" + blurUrl + '\'' +
                '}';
    }
}
