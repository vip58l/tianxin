package com.tianxin.activity.LatestNews;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤微信号，QQ号
 */
public class InfoFilterUtil {

    //匹配用户名
    public static boolean checkUserName(String username) {
        String regExp = "^[a-zA-Z0-9]{4,16}$";
        if (username.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配密码
    public static boolean checkPassword(String password) {
        String regExp = "^[a-zA-Z0-9@/#.+=]{4,16}$";
        if (password.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配姓名
    public static boolean checkName(String name) {
        String regExp = "^[\\u4E00-\\u9FA5]{2,6}$";
        if (name.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配手机号
    public static boolean checkPhone(String phone) {
        String regExp = "^[1]([3-9])[0-9]{9}$";
        if (phone.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配手机验证码
    public static boolean checkCaptcha(String code) {
        String regExp = "^[0-9]{6}$";
        if (code.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配身份证
    public static boolean checkIdCard(String s) {
        String regExp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        if (s.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配0-200字符
    public static boolean check0_200Char(String s) {
        String regExp = "^[\\u4E00-\\u9FA5A-Za-z0-9_]{0,200}$";
        if (s.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }

    //匹配1-200字符加()
    public static boolean check1_200Char1(String s) {
        String regExp = "^[\\u4E00-\\u9FA5A-Za-z0-9_()（）]{1,200}$";
        if (s.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }


    //匹配0-400字符
    public static boolean check400Char(String s) {
        String regExp = "^([\\s\\S]*){0,400}$";
        if (s.matches(regExp)) {
            return true;
        } else {
            return false;
        }
    }


    //目前手机前两位：13,14,15,17,18
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }

        //1[3|4|5|7|8][0-9]表示以1开头，后跟3,4,5,7,8，[0-9]表示数字即可，\d{8}剩余八位填充随意数字
        Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    //QQ号或者微信号判断
    public static boolean isQQOrWX(String qqorwx) {
        if (TextUtils.isEmpty(qqorwx)) {
            return false;
        }
        //QQ号最短5位，微信号最短是6位最长20位
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(qqorwx);
        return m.matches();
    }

    //姓名有效性判断
    public static boolean isName(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }

        //因项目需求，只需要限定在中文和英文上即可，长度已经在Android EditText中限制输入，此处不做长度限制
        Pattern p = Pattern.compile("^[\u4E00-\u9FA5a-zA-Z]+");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 过滤手机号
     * @param text
     * @return
     */
    public String FindPhoneNumber(String text) {
        if (text == null)
            return FindQqOrWxNumber("");
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[35689]\\d{9})|(?:861[35689]\\d{9}))(?!\\d)");
        Matcher matcher = pattern.matcher(text);
        //StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            text = text.replace(matcher.group(), matcher.group().substring(0, 3) + "********");
        }
        return FindQqOrWxNumber(text);
    }


    /**
     * 过滤qq号微信号
     */
    private String FindQqOrWxNumber(String text) {

        Pattern pattern = Pattern.compile("(微信|wx|WX|约炮|QQ|qq|weixin|1[0-9]{10}|[a-zA-Z0-9\\-\\_]{6,16}|[0-9]{6,11})+");
        Matcher matcher = pattern.matcher(text);
        //StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "******");
        }
        return text;
    }

}


