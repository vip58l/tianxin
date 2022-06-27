/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/14 0014
 */


package com.tencent.opensource.model;

import java.io.Serializable;

public class videotype implements Serializable {
    private int id;
    private int userid;
    private int videotitle;
    private int type;
    private int status;
    private String title;
    private String datetime;

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

    public int getVideotitle() {
        return videotitle;
    }

    public void setVideotitle(int videotitle) {
        this.videotitle = videotitle;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "videotype{" +
                "id=" + id +
                ", userid=" + userid +
                ", videotitle=" + videotitle +
                ", type=" + type +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
