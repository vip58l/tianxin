/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/6 0006
 */


package com.tianxin.Module.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.JsonUitl;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class moneylist {
    private static moneylist sUserInfo;
    private int id;
    private String money;
    private int give;
    private String msg;
    private int vip;
    private String datetime;

    @Override
    public String toString() {
        return "moneylist{" +
                "id=" + id +
                ", moneylist=" + money +
                ", give=" + give +
                ", datetime='" + datetime + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        moneylist(this);
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
        moneylist(this);
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
        moneylist(this);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        moneylist(this);
    }

    public int getGive() {
        return give;
    }

    public void setGive(int give) {
        this.give = give;
        moneylist(this);
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
        moneylist(this);
    }

    /**
     * 保存余额
     *
     * @param key
     */
    public static void setsever(String key) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.MONEYLIST3, key);
        editor.commit();
    }

    /**
     * 读取余额
     *
     * @return
     */
    public static String getserver() {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, MODE_PRIVATE);
        String json = shareInfo.getString(Constants.MONEYLIST3, "");
        return json;
    }


    /**
     * 保存数据
     *
     * @param moneylist
     */
    public synchronized void moneylist(moneylist moneylist) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.MONEYLIST1, new Gson().toJson(moneylist));
        editor.commit();
    }

    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static moneylist getInstance() {
        if (sUserInfo == null) {
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString(Constants.MONEYLIST2, "");
            sUserInfo = new Gson().fromJson(json, moneylist.class);
            if (sUserInfo == null) {
                sUserInfo = new moneylist();
            }
        }
        return sUserInfo;
    }

    /**
     * 保存数据
     *
     * @param json
     */
    public synchronized static void setmoneylist(String json) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.MONEYLIST2, json);
        editor.commit();
    }

    /**
     * 读取数据
     *
     * @return
     */
    public synchronized static List<moneylist> getsetmoneylist() {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, MODE_PRIVATE);
        String json = shareInfo.getString(Constants.MONEYLIST2, "");
        List<moneylist> moneylistList = JsonUitl.stringToList(json, moneylist.class);
        return moneylistList;
    }

}
