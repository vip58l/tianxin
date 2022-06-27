/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/20 0020
 */


package com.tencent.opensource.model;

public class navigation {
    private int id;
    private String title;
    private String tag;
    private String pic;
    private String path;
    private String cls;
    private int type;
    private int status;
    private int idx;
    private int vip;
    public int activity;
    private String datetime;

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "navigation{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tag='" + tag + '\'' +
                ", pic='" + pic + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", idx=" + idx +
                ", activity=" + activity +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
