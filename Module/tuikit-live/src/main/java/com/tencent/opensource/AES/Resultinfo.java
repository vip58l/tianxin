package com.tencent.opensource.AES;

import android.util.Log;

public class Resultinfo {

    public static String TAG = Resultinfo.class.getName();

    //解密文件
    public static String decrypt(String result) {
        String replace = result.replace(" ", "");
        String decrypt = AES.decrypt(replace);
        return decrypt;
    }


    /**
     * 需要加密处理
     */
    public static void EncryptTest() {
        String resultInfo = AES.Encrypt("再超美国！中国逆势而上 成2020年全球最大外资流入国", "6587423185469874", "0102030405060708");
        System.out.println(resultInfo);
        Log.d(TAG, "需要加密处理EncryptTest:="+resultInfo);
    }

    public static void myTest() {

        //解密演示文档
        String decrypt =  AES.decrypt("1lAT7v7pvurbXTLQoDTblATA9Dzbb6Hgo0MYNzLRJzNgNcoBZ08o7Y5SnHelDPvaM7GWdaEmKbEa8S3bEcYXS80vCLwLSBmbVELUYEWLQMY=");
        String decrypt1 = AES.decrypt("1lAT7v7pvurbXTLQoDTblATA9Dzbb6Hgo0MYNzLRJzNgNcoBZ08o7Y5SnHelDPvaM7GWdaEmKbEa8S3bEcYXS80vCLwLSBmbVELUYEWLQMY=", "0102030405060708");
        String decrypt2 = AES.decrypt("1lAT7v7pvurbXTLQoDTblATA9Dzbb6Hgo0MYNzLRJzNgNcoBZ08o7Y5SnHelDPvaM7GWdaEmKbEa8S3bEcYXS80vCLwLSBmbVELUYEWLQMY=", "6587423185469874", "0102030405060708");
        String decrypt3 = AES.decrypt("gfo8QvBRu4KFGqUEw17VN9uHf91rXNuqUk3E+hVEWNtv8OhWvd1q4eKR6CJLS7fSFLxPnYIblUZrN3Aje+wL4g==", "6587423185469874", "0102030405060708");

        //加密演示文档
        String Encrypt = AES.Encrypt("再超美国！中国逆势而上 成2020年全球最大外资流入国", "6587423185469874", "0102030405060708");

        Log.d(TAG, "解密演示decrypt=: "+decrypt);
        Log.d(TAG, "解密演示decrypt1=: "+decrypt1);
        Log.d(TAG, "解密演示decrypt2=: "+decrypt2);
        Log.d(TAG, "解密演示decrypt3=: "+decrypt3);
        Log.d(TAG, "加密演示Encrypt=: "+Encrypt);
    }

}
