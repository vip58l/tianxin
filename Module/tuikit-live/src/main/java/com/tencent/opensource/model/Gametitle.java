package com.tencent.opensource.model;

public class Gametitle {
    int id;
    String pic;
    String name;
    int status;
    String msg;
    String alias;
    int frequency;
    String datetime;
    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "gametitle{" +
                "id=" + id +
                ", pic='" + pic + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
