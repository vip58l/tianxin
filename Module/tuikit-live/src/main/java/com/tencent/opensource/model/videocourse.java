/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/5/5 0005
 */


package com.tencent.opensource.model;

/**
 * 播放购买记录对像
 */
public class videocourse {
    int id;
    int userid;
    int videoid;
    int type;
    String datetime;

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

    public int getVideoid() {
        return videoid;
    }

    public void setVideoid(int videoid) {
        this.videoid = videoid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "videocourse{" +
                "id=" + id +
                ", userid=" + userid +
                ", videoid=" + videoid +
                ", type=" + type +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
