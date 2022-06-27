/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/3 0003
 */


package com.tianxin.Module.api;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianxin.Util.Constants;
import com.tianxin.app.DemoApplication;

public class Buckets {
    private static Buckets sbuckets;
    private String name;
    /**
     * 存储桶所在地域
     */
    private String location;
    /**
     * 存储桶的创建时间，为 ISO8601 格式，例如2019-05-24T10:56:40Z
     */
    private String createDate;
    private String type;

    @Override
    public String toString() {
        return "Buckets{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", createDate='" + createDate + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setSbucket(this);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        setSbucket(this);
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
        setSbucket(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized static Buckets getSbuckets() {
        if (sbuckets == null) {
            SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
            String json = shareInfo.getString("buckets", "");
            sbuckets = new Gson().fromJson(json, Buckets.class);
            if (sbuckets == null) {
                sbuckets = new Buckets();
            }
        }
        return sbuckets;
    }


    /**
     * 保存数据
     *
     * @param buckets
     */
    public void setSbucket(Buckets buckets) {
        SharedPreferences shareInfo = DemoApplication.instance().getSharedPreferences(Constants.USERINFO, 0);
        SharedPreferences.Editor editor = shareInfo.edit();
        editor.putString("buckets", new Gson().toJson(buckets));
        editor.commit();
    }

}
