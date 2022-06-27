/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/19 0019
 */


package com.tianxin.Module.api;

public class banner {
    private int id;
    private String title;
    private String picture;
    private String path;
    private int status;
    private int type;
    private String datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return "banner{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", picture='" + picture + '\'' +
                ", path='" + path + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
