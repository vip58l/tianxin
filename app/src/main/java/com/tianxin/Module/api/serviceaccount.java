/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/16 0016
 */


package com.tianxin.Module.api;

public class serviceaccount {
    private int id;
    private int userid;
    private String content;
    private int type;
    private int status;
    private int typetitle;
    private String idx;
    private String title;
    private String datetime;
    private String icon;

    @Override
    public String toString() {
        return "serviceaccount{" +
                "id=" + id +
                ", userid=" + userid +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", typetitle=" + typetitle +
                ", idx='" + idx + '\'' +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                ", icon='" + icon + '\'' +
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

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getTypetitle() {
        return typetitle;
    }

    public void setTypetitle(int typetitle) {
        this.typetitle = typetitle;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
