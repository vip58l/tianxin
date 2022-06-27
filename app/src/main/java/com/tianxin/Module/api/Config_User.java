/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/26 0026
 */


package com.tianxin.Module.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;

public class Config_User {
    private int id;
    private boolean sendout;
    private int Keaddmsg;
    private String Userid;
    private static Config_User sconfigure;
    private static final String tp1 = "tp1";

    public int getKeaddmsg() {
        return Keaddmsg;
    }

    public void setKeaddmsg(int keaddmsg) {
        this.Keaddmsg = keaddmsg;
        setUserInfo(this);
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        this.Userid = userid;
        setUserInfo(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setUserInfo(this);
    }

    public boolean isSendout() {
        return sendout;
    }

    public void setSendout(boolean sendout) {
        this.sendout = sendout;
        setUserInfo(this);
    }

    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static Config_User getInstance() {
        if (sconfigure == null) {
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.Edit, 0);
            String json = shareInfo.getString(tp1, "");
            sconfigure = new Gson().fromJson(json, Config_User.class);
            if (sconfigure == null) {
                sconfigure = new Config_User();
            }
        }
        return sconfigure;
    }

    /**
     * 保存数据
     *
     * @param info
     */
    public synchronized void setUserInfo(Config_User info) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.Edit, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(tp1, new Gson().toJson(info));
        editor.commit();
    }

}
