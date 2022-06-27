package com.tencent.opensource.AES;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

//* AES解密模式:CBC填充: PKCS5Padding 数据块:128 密码:6587423185469874偏移量:0102030405060708 输出:字符集：
public class AES {

    /**
     * AES解密模式:CBC填充: PKCS5Padding 数据块:128 密码:6587423185469874偏移量:0102030405060708 输出:字符集：
     *
     * @param paramString1
     * @param sKey
     * @param ivParameter
     * @return
     */
    public static String decrypt(String paramString1, String sKey, String ivParameter) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            //先用base64解密
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(paramString1);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密AES CbC 128 PKCS5Padding
     *
     * @param paramString1
     * @param ivParameter
     * @return
     */
    public static String decrypt(String paramString1, String ivParameter) {
        try {
            byte[] arrayOfByte = new BASE64Decoder().decodeBuffer(paramString1);
            SecretKeySpec localSecretKeySpec = new SecretKeySpec("6587423185469874".getBytes(), "AES");
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(ivParameter.getBytes());
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
            String str = new String(localCipher.doFinal(arrayOfByte), StandardCharsets.UTF_8);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密AES CbC 128 PKCS5Padding
     */
    public static String decrypt(String paramString1) {
        try {
            byte[] arrayOfByte = new BASE64Decoder().decodeBuffer(paramString1);
            SecretKeySpec localSecretKeySpec = new SecretKeySpec("6587423185469874".getBytes(), "AES");
            IvParameterSpec localIvParameterSpec = new IvParameterSpec("0102030405060708".getBytes());
            Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
            String str = new String(localCipher.doFinal(arrayOfByte), StandardCharsets.UTF_8);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * resultInfo=数据 sKey=6587423185469874 ivParameter=0102030405060708
     * AES加密模式:CBC填充: PKCS5Padding 数据块:128 密码:6587423185469874偏移量:0102030405060708 输出:字符集：
     *
     * @param resultInfo
     * @param sKey
     * @param ivParameter
     * @return
     * @throws Exception
     */
    public static String Encrypt(String resultInfo, String sKey, String ivParameter) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypted = cipher.doFinal(resultInfo.getBytes(StandardCharsets.UTF_8));
            String str = new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] mybuffer(String paramString) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(paramString.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        String htmlStr = new String(outStream.toByteArray(), StandardCharsets.UTF_8);
        buffer.clone();
        outStream.close();
        inputStream.close();
        return outStream.toByteArray();
    }

}
