/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/31 0031
 */


package com.tianxin.Module.api;

public class version {
    private int id;
    private String title;
    private int version;
    private String path;
    private String loginMsg;
    private String datetime;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLoginMsg() {
        return loginMsg;
    }

    public void setLoginMsg(String loginMsg) {
        this.loginMsg = loginMsg;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    @Override
    public String toString() {
        return "version{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", version=" + version +
                ", path='" + path + '\'' +
                ", loginMsg='" + loginMsg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
