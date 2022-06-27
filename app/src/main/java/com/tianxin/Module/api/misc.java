/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/1 0001
 */


package com.tianxin.Module.api;

import java.io.Serializable;

/**
 * 自定义对像
 */
public class misc implements Serializable {
    private String id;
    private String title;
    private String tag;
    private String url;
    private String picture;
    private int type;
    private int status;
    private String datetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "misc{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", url='" + url + '\'' +
                ", picture='" + picture + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}