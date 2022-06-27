package com.savglotti.model;

import java.io.Serializable;

public class UserModel {
    public String userId;
    public String userName;
    public String userAvatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
