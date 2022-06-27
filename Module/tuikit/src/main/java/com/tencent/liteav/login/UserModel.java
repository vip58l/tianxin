package com.tencent.liteav.login;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String phone;
    public String userId;
    public String userSig;
    public String userName;
    public String userAvatar;
    public String token;

    //下面是自定义属性
    public int Gender;          //性别
    public int tRole;           //角色 0接收 1拨打
    public int Level;           //等级
    public int AllowType;       //允许类型

    //位置信息扩展
    public String province;
    public String city;
    public String district;
    public String address;
    public String jwd;

    //个性签名扩展
    public String cforsds;
    public String pesigntext;

    /**
     * 自动进入接听视频
     */
    public boolean floating;

    @Override
    public String toString() {
        return "UserModel{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                ", userSig='" + userSig + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", token='" + token + '\'' +
                ", Gender=" + Gender +
                ", tRole=" + tRole +
                ", Level=" + Level +
                ", AllowType=" + AllowType +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", address='" + address + '\'' +
                ", jwd='" + jwd + '\'' +
                ", cforsds='" + cforsds + '\'' +
                ", pesigntext='" + pesigntext + '\'' +
                '}';
    }
}
