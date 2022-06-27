/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/26 0026
 */


package com.tianxin.Module.api;

public class videotitle {
    private int id;
    private String icon;
    private String title;
    private String tag;
    private int type;
    private int status;
    private int select;
    private String datetime;

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
        return "videotitle{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
