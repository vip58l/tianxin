package com.tencent.opensource.model;

public class trend {
    private int id;
    private int userid;
    private int tencent;
    private String title;
    private String content;
    private String image;
    private String video;
    private String videotest;
    private int type;
    private int sex;
    private String msg;
    private int status;
    private int love;
    private int count;
    private int give;
    private String datetime;
    private member member;
    private personal personal;

    public int getGive() {
        return give;
    }

    public void setGive(int give) {
        this.give = give;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTencent() {
        return tencent;
    }

    public void setTencent(int tencent) {
        this.tencent = tencent;
    }

    public String getVideotest() {
        return videotest;
    }

    public void setVideotest(String videotest) {
        this.videotest = videotest;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public com.tencent.opensource.model.member getMember() {
        return member;
    }

    public void setMember(com.tencent.opensource.model.member member) {
        this.member = member;
    }

    public com.tencent.opensource.model.personal getPersonal() {
        return personal;
    }

    public void setPersonal(com.tencent.opensource.model.personal personal) {
        this.personal = personal;
    }

    @Override
    public String toString() {
        return "trend{" +
                "id=" + id +
                ", userid=" + userid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", video='" + video + '\'' +
                ", videotest='" + videotest + '\'' +
                ", type=" + type +
                ", msg='" + msg + '\'' +
                ", status=" + status +
                ", love=" + love +
                ", datetime='" + datetime + '\'' +
                ", member=" + member +
                '}';
    }
}
