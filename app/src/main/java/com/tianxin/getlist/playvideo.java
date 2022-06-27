/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/13 0013
 */


package com.tianxin.getlist;

import java.io.Serializable;

public  class playvideo implements Serializable {
    public String id;
    public String title;
    public String name;
    public String avatar;
    public String cover;
    public String url;
    public String address;
    public String label;
    public String ctime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    @Override
    public String toString() {
        return "GameActivityfinish{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", address='" + address + '\'' +
                ", label='" + label + '\'' +
                ", ctime='" + ctime + '\'' +
                '}';
    }
}
