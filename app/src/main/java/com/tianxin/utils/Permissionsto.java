package com.tianxin.utils;


import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;

public class Permissionsto {
    private final static String PER_USER_MODEL = "permissions";
    private static Permissionsto spermissions;
    String date;
    String msg;
    boolean sussess;
    int pemisson;
    int pgs;

    public int getPgs() {
        return pgs;
    }

    public void setPgs(int pgs) {
        this.pgs = pgs;
        setSpermissions(this);
    }

    public int getPemisson() {
        return pemisson;
    }

    public void setPemisson(int pemisson) {
        this.pemisson = pemisson;
        setSpermissions(this);
    }

    public boolean isSussess() {
        return sussess;
    }

    public void setSussess(boolean sussess) {
        this.sussess = sussess;
        setSpermissions(this);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        setSpermissions(this);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        setSpermissions(this);
    }


    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static Permissionsto getInstance() {
        if (spermissions == null) {
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.PERMISSIONS, 0);
            String json = shareInfo.getString(PER_USER_MODEL, "");
            spermissions = new Gson().fromJson(json, Permissionsto.class);
            if (spermissions == null) {
                spermissions = new Permissionsto();
            }
        }
        return spermissions;
    }

    public static void setSpermissions(Permissionsto spermissions) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.PERMISSIONS, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(PER_USER_MODEL, new Gson().toJson(spermissions));
        editor.commit();
    }

    @Override
    public String toString() {
        return "Permissionsto{" +
                "date='" + date + '\'' +
                ", msg='" + msg + '\'' +
                ", sussess=" + sussess +
                '}';
    }
}
