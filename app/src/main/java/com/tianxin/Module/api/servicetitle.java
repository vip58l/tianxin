/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/16 0016
 */


package com.tianxin.Module.api;

public class servicetitle {
    private int id;
    private String icon;
    private String title;
    private int type;
    private int status;
    private int idx;
    private String datetime;

    @Override
    public String toString() {
        return "servicetitle{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", idx=" + idx +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
