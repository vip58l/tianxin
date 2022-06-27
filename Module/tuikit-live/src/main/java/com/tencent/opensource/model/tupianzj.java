package com.tencent.opensource.model;

public class tupianzj {
    private int id;
    private int userid;
    private int cover;
    private String title;
    private int perimg;
    private String pic;
    private String source;
    private int type;
    private int status;
    private String msg;
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

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPerimg() {
        return perimg;
    }

    public void setPerimg(int perimg) {
        this.perimg = perimg;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "tupianzj{" +
                "id=" + id +
                ", userid=" + userid +
                ", cover=" + cover +
                ", title='" + title + '\'' +
                ", perimg=" + perimg +
                ", pic='" + pic + '\'' +
                ", source='" + source + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
