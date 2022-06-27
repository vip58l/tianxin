/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/13 0013
 */
package com.tianxin.Module.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;

public class Config_Msg {
    private int id;
    private String msg;
    private int type;
    private String message;
    private String datetime;
    private static Config_Msg sconfigmsg;
    private boolean isswitch;
    private boolean chat;
    private boolean dialog_call;
    private boolean one;//表示首次安装

    private boolean message_shock;
    private boolean message_voice;

    private boolean conceal_1 = true;
    private boolean conceal_2 = true;
    private boolean conceal_3 = true;
    private boolean conceal_4 = true;
    private boolean conceal_5 = true;

    public boolean isOne() {
        return one;
    }

    public void setOne(boolean one) {
        this.one = one;
        seconfigmsg(this);
    }

    public boolean isConceal_1() {
        return conceal_1;
    }

    public void setConceal_1(boolean conceal_1) {
        this.conceal_1 = conceal_1;
        seconfigmsg(this);
    }

    public boolean isConceal_2() {
        return conceal_2;
    }

    public void setConceal_2(boolean conceal_2) {
        this.conceal_2 = conceal_2;
        seconfigmsg(this);
    }

    public boolean isConceal_3() {
        return conceal_3;
    }

    public void setConceal_3(boolean conceal_3) {
        this.conceal_3 = conceal_3;
        seconfigmsg(this);
    }

    public boolean isConceal_4() {
        return conceal_4;
    }

    public void setConceal_4(boolean conceal_4) {
        this.conceal_4 = conceal_4;
        seconfigmsg(this);
    }

    public boolean isConceal_5() {
        return conceal_5;
    }

    public void setConceal_5(boolean conceal_5) {
        this.conceal_5 = conceal_5;
        seconfigmsg(this);
    }

    public boolean isMessage_shock() {
        return message_shock;
    }

    public void setMessage_shock(boolean message_shock) {
        this.message_shock = message_shock;
        seconfigmsg(this);
    }

    public boolean isMessage_voice() {
        return message_voice;
    }

    public void setMessage_voice(boolean message_voice) {
        this.message_voice = message_voice;
        seconfigmsg(this);
    }

    public boolean isDialog_call() {
        return dialog_call;
    }

    public void setDialog_call(boolean dialog_call) {
        this.dialog_call = dialog_call;
        seconfigmsg(this);
    }

    public boolean isChat() {
        return chat;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
        this.seconfigmsg(this);
    }

    public boolean isIsswitch() {
        return isswitch;
    }

    public void setIsswitch(boolean isswitch) {
        this.isswitch = isswitch;
        seconfigmsg(this);
    }

    public int getId() {
        return id;

    }

    public void setId(int id) {
        this.id = id;
        seconfigmsg(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        seconfigmsg(this);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        seconfigmsg(this);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        seconfigmsg(this);
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
        seconfigmsg(this);
    }

    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static Config_Msg getInstance() {
        if (sconfigmsg == null) {
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString(Constants.configmsg, "");
            sconfigmsg = new Gson().fromJson(json, Config_Msg.class);
            if (sconfigmsg == null) {
                sconfigmsg = new Config_Msg();
            }
        }
        return sconfigmsg;
    }

    /**
     * 保存数据
     *
     * @param configmsg
     */
    public void seconfigmsg(Config_Msg configmsg) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(Constants.configmsg, new Gson().toJson(configmsg));
        editor.commit();
    }


    @Override
    public String toString() {
        return "Config_Msg{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                ", datetime='" + datetime + '\'' +
                ", isswitch=" + isswitch +
                '}';
    }
}
