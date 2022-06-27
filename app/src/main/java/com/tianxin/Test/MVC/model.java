package com.tianxin.Test.MVC;

import com.tianxin.Test.MDEMO.Account;
import com.tianxin.Test.MDEMO.Callback;

import java.util.Random;

/**
 * MVC模式
 * model数据网络请求http Post get 等操作 通过接口方式回调数据结果
 * 方便ACTIVITY层维护，
 */
public class model {
    public void getAccountDate(String name, Callback callback) {
        Random random = new Random();
        int irs = (int) (Math.random() * 50) + 50;
        boolean b = random.nextBoolean();
        if (b) {
            Account account = new Account();
            account.setName(name);
            account.setAge(irs);
            account.setLevel(irs);
            callback.onSuccess(account);
        } else {
            callback.onFailed();
        }

    }
}
