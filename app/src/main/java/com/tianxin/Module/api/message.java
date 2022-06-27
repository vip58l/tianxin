/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/26 0026
 */


package com.tianxin.Module.api;

public class message {
    int id;
    String msg;
    String datetime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "message{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
