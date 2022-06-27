package com.tencent.opensource.model;

public class ShareDate {
    private int id;
    private String t_img_path;
    private int status;
    private String shareUrl;
    private String datetime;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getT_img_path() {
        return t_img_path;
    }

    public void setT_img_path(String t_img_path) {
        this.t_img_path = t_img_path;
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

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }
}
