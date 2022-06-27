/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/30 0030
 */


package com.tianxin.Util;

/**
 * 汉字工具类
 *
 * @ClassName HanZiUtil
 * @Description
 * @Author liuFuTao
 * @Date 2020/6/16 10:56
 */
public class HanZiUtil {

    /**
     * 返回一个汉字
     *
     * @param
     * @return
     */
    public static char getRandomHanZi() {
        return (char) (0x4e00 + (int) (Math.random() * (0x9fa5 - 0x4e00 + 1)));
    }

    /**
     * 获取多个汉字中间有空格
     *
     * @param size 汉字个数
     * @return
     */
    public static String getRandomHanZi(int size) {

        if (size <= 0) {
            size = 1;
        }
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < size; i++) {
            stringBuffer.append(getRandomHanZi() + " ");
        }
        return stringBuffer.toString();
    }

    /**
     * 获取多个汉字中间无空格
     *
     * @param size 汉字个数
     * @return
     */
    public static String getRandomHanZiNoSpace(int size) {

        if (size <= 0) {
            size = 1;
        }
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < size; i++) {
            stringBuffer.append(getRandomHanZi());
        }
        return stringBuffer.toString();
    }
}

