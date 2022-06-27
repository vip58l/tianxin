package com.tencent.qcloud.tim.tuikit.live.base;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {
    private static boolean sEnablePKButton    = true;            // 是否显示主播页底部栏PK按钮
    private static final boolean sEnableFloatWindow = true;      // 是否开启悬浮窗模式

    public static boolean getPKButtonStatus() {
        return sEnablePKButton;
    }

    public static void setPKButtonStatus(boolean enable) {
        sEnablePKButton = enable;
    }

    public static boolean enableFloatWindow() {
        return sEnableFloatWindow;
    }

    /**
     * 过滤手机号代码
     *
     * @param text
     * @return
     */
    public static String FindPhoneNumber(String text) {
        if (TextUtils.isEmpty(text)){
            return FindQqOrWxNumber("");
        }
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[345689]\\d{9})|(?:861[35689]\\d{9}))(?!\\d)");
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
    private static String FindQqOrWxNumber(String text) {
        if (TextUtils.isEmpty(text)){
            return "";
        }
        Pattern pattern = Pattern.compile("(微信|QQ|qq|weixin|WX|wx|1[0-9]{10}|[a-zA-Z0-9\\-\\_]{6,16}|[0-9]\n{6,11})+");
        Matcher matcher = pattern.matcher(text);
        //StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            text = text.replace(matcher.group(), "******");
        }
        return text;
    }

}
