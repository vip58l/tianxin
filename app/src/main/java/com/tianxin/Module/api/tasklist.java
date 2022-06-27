/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/30 0030
 */


package com.tianxin.Module.api;

public class tasklist {
    private int id;
    private String title;
    private String msg;
    private String action;
    private String path;
    private int type;
    private int status;
    private int qiandao;
    private int bind;
    private String datetime;

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public int getQiandao() {
        return qiandao;
    }

    public void setQiandao(int qiandao) {
        this.qiandao = qiandao;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return "tasklist{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", action='" + action + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", qiandao=" + qiandao +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
