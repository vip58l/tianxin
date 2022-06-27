package com.tencent.liteav.model;

import com.tencent.liteav.login.UserModel;

import java.io.Serializable;
import java.util.List;

/**
 * 转换添加Serializable
 */
public class IntentParams implements Serializable {
    public List<UserModel> mUserModels;

    public IntentParams(List<UserModel> userModels) {
        this.mUserModels = userModels;
    }
}