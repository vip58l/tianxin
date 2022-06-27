package com.tianxin.Module.api;

public class shouchangmember {
    private String id;
    private String title;
    private String uid;
    private String userid;
    private String truename;
    private String username;
    private String dadetime;
    private int type;
    private String pic;

    @Override
    public String toString() {
        return "shouchangmember{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", uid='" + uid + '\'' +
                ", userid='" + userid + '\'' +
                ", truename='" + truename + '\'' +
                ", username='" + username + '\'' +
                ", dadetime='" + dadetime + '\'' +
                ", type=" + type +
                ", pic='" + pic + '\'' +
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDadetime() {
        return dadetime;
    }

    public void setDadetime(String dadetime) {
        this.dadetime = dadetime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
