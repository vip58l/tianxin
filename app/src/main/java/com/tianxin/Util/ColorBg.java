package com.tianxin.Util;

import com.tianxin.R;
import com.tianxin.app.DemoApplication;

public class ColorBg {

    public static int Color(int paramInt) {
        return (int) (Math.random() * paramInt);
    }

    /**
     * 随机背景色
     *
     * @return
     */
    public static String sColor() {
        String[] arrayOfString = DemoApplication.instance().getResources().getStringArray(R.array.tabs4);
        return arrayOfString[Color(arrayOfString.length)];
    }

    /**
     * 随机背景色
     *
     * @return
     */
    public static String sColor(int paramInt) {
        String[] arrayOfString = DemoApplication.instance().getResources().getStringArray(R.array.tabs4);
        return paramInt < (arrayOfString.length - 1) ? arrayOfString[paramInt] : arrayOfString[arrayOfString.length - 1];
    }
}
