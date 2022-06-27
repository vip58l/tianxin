/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/28 0028
 * 保存系统相关配置信息，如开关，黑色主题 头像 标记
 */


package com.tianxin.Module.api;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.app.DemoApplication;

/**
 * 保存相关配置信息
 */
public class Config_Uration {
    public final static String Config = "Config";
    public final static String readwrite = "Configuration";
    private String id;
    private String userid;
    private String username;
    private String passwoird;
    private String phone;
    private String token;
    private String name;
    private String avatar;
    private int sex;
    private boolean autoLogin;

    private static Config_Uration Configuration;

    /**
     * 保存数据Config.mxl
     * Configuration
     */
    public void setConfiguration(Config_Uration configuration) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Config, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString(readwrite, new Gson().toJson(configuration));
        editor.commit();
    }


    /**
     * 读取保存的配置信息静态对像 同步化的 synchronized
     * synchronized其中方法是实例方法和静态方法分别锁的是该类的实例对象和该类的对象
     * 一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞
     * 一个线程获取了该对象的锁之后，其他线程无法获取该对象的锁，就不能访问该对象的其他synchronized实例方法
     * @return
     */
    public synchronized static Config_Uration getInstance() {

        if (Configuration == null) {
            //1到本地获取数据
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Config, 0);
            String json = shareInfo.getString(readwrite, "");
            //读取json格式内容转换成对像Configuration
            Configuration = new Gson().fromJson(json, Config_Uration.class);

            //2拿不到数居重新创建对像返回
            if (Configuration == null) {
                Configuration = new Config_Uration();
            }
        }
        return Configuration;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", passwoird='" + passwoird + '\'' +
                ", phone='" + phone + '\'' +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex='" + sex + '\'' +
                ", autoLogin=" + autoLogin +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setConfiguration(this);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
        setConfiguration(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        setConfiguration(this);
    }

    public String getPasswoird() {
        return passwoird;
    }

    public void setPasswoird(String passwoird) {
        this.passwoird = passwoird;
        setConfiguration(this);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        setConfiguration(this);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        setConfiguration(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setConfiguration(this);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        setConfiguration(this);
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
        setConfiguration(this);
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
    }
}
