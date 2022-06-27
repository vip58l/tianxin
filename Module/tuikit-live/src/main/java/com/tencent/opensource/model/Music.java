/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/19 0019
 */


package com.tencent.opensource.model;

public class Music {
    private String id;
    private String title;
    private String tag;
    private String url;
    private int type;
    private int status;
    private String datetime;

    @Override
    public String toString() {
        return "music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", url='" + url + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", datetime='" + datetime + '\'' +
                '}';
    }

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
}
