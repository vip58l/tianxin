/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/9 0009
 */


package com.tencent.qcloud.tim.uikit.utils;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.TUIKit;

public class Allcharge {
    private static Allcharge sallcharge;
    private int id;
    private int userid;
    private int video;
    private int Audio;
    private int money;
    private int contact;
    private String datetime;

    public int getContact() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact = contact;
        setallcharge(this);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        setallcharge(this);
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
        setallcharge(this);
    }

    public Allcharge(int video, int Audio) {
        this.video = video;
        this.Audio = Audio;
    }

    public Allcharge() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        setallcharge(this);
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
        setallcharge(this);
    }

    public int getVideo() {
        return video;
    }

    public void setVideo(int video) {
        this.video = video;
        setallcharge(this);
    }

    public int getAudio() {
        return Audio;
    }

    public void setAudio(int audio) {
        Audio = audio;
        setallcharge(this);
    }

    public String getDadetime() {
        return datetime;
    }

    public void setDadetime(String dadetime) {
        this.datetime = dadetime;
        setallcharge(this);
    }

    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static Allcharge getInstance() {
        if (sallcharge == null) {
            SharedPreferences shareInfo = TUIKit.getAppContext().getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString(Constants.ALLCHARGE, "");
            sallcharge = new Gson().fromJson(json, Allcharge.class);
        }
        if (sallcharge == null) {
            sallcharge = new Allcharge();
        }
        return sallcharge;
    }

    /**
     * 保存视频或语音通话最低消费金币
     *
     * @param allcharge
     */
    public void setallcharge(Allcharge allcharge) {
        SharedPreferences shareInfo = TUIKit.getAppContext().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.ALLCHARGE, new Gson().toJson(allcharge));
        editor.commit();
    }

    @Override
    public String toString() {
        return "Allcharge{" +
                "id=" + id +
                ", userid=" + userid +
                ", video=" + video +
                ", Audio=" + Audio +
                ", money=" + money +
                ", datetime='" + datetime + '\'' +
                '}';
    }
}
