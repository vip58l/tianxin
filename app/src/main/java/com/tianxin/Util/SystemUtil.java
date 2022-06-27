package com.tianxin.Util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

import static android.content.Context.TELEPHONY_SERVICE;

public class SystemUtil {
    private static final String TAG = SystemUtil.class.getName();

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private static String getandroid(Context context) {
        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            if (Build.VERSION.SDK_INT > 28) {
                return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                return TelephonyMgr.getDeviceId();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }

    public static void show() {
        /**
         * android.os.Build.BOARD：获取设备基板名称
         * android.os.Build.BOOTLOADER:获取设备引导程序版本号
         * android.os.Build.BRAND：获取设备品牌
         * android.os.Build.CPU_ABI：获取设备指令集名称（CPU的类型）
         * android.os.Build.CPU_ABI2：获取第二个指令集名称
         * android.os.Build.DEVICE：获取设备驱动名称
         * android.os.Build.DISPLAY：获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
         * android.os.Build.FINGERPRINT：设备的唯一标识。由设备的多个信息拼接合成。
         * android.os.Build.HARDWARE：设备硬件名称,一般和基板名称一样（BOARD）
         * android.os.Build.HOST：设备主机地址
         * android.os.Build.ID:设备版本号。
         * android.os.Build.MODEL ：获取手机的型号 设备名称。
         * android.os.Build.MANUFACTURER:获取设备制造商
         * android:os.Build.PRODUCT：整个产品的名称
         * android:os.Build.RADIO：无线电固件版本号，通常是不可用的 显示unknown
         * android.os.Build.TAGS：设备标签。如release-keys 或测试的 test-keys
         * android.os.Build.TIME：时间
         * android.os.Build.TYPE:设备版本类型  主要为"user" 或"eng".
         * android.os.Build.USER:设备用户名 基本上都为android-build
         * android.os.Build.VERSION.RELEASE：获取系统版本字符串。如4.1.2 或2.2 或2.3等
         * android.os.Build.VERSION.CODENAME：设备当前的系统开发代号，一般使用REL代替
         * android.os.Build.VERSION.INCREMENTAL：系统源代码控制值，一个数字或者git hash值
         * android.os.Build.VERSION.SDK：系统的API级别 一般使用下面大的SDK_INT 来查看
         * android.os.Build.VERSION.SDK_INT：系统的API级别 数字表示
         * JAVA字符串格式化-String.format()的使用
         * %s字符串 %c字符类型 %b布尔类型 %d 10 整数类型%x 整数类型（16进制）%o 整数类型（8进制）%f 浮点类型%a 制浮点类型%e 数类型%g %h散列码 %%百分比类型 %n换行符 %tx日期与时间类型
         */
    }

    /**
     * 检查检权是否申请获得
     * //Android 6.0以上需要申请权限
     *
     * @param activity
     */
    public static void getPermission(Activity activity, List<String> permissionList) {
        String[] strings = permissionList.toArray(new String[permissionList.size()]);
        ActivityCompat.requestPermissions(activity, strings, Config.sussess);
    }

    /**
     * 检查检权是否申请获得
     * //Android 6.0以上需要申请权限
     *
     * @param activity
     */
    public static void getPermission(Activity activity, List<String> permissionList, int REQUEST_CODE) {
        String[] strings = permissionList.toArray(new String[permissionList.size()]);
        ActivityCompat.requestPermissions(activity, strings, REQUEST_CODE);
    }

    public static void showlog() {
        Log.d(TAG, "BOARD: " + android.os.Build.BOARD);
        Log.d(TAG, "DISPLAY: " + android.os.Build.DISPLAY);
        Log.d(TAG, "FINGERPRINT: " + android.os.Build.MODEL);
        Log.d(TAG, "HARDWARE: " + android.os.Build.HARDWARE);
        Log.d(TAG, "HOST: " + android.os.Build.HOST);
        Log.d(TAG, "ID: " + android.os.Build.ID);
        Log.d(TAG, "MODEL: " + android.os.Build.MODEL);
        Log.d(TAG, "MANUFACTURER: " + android.os.Build.MANUFACTURER);
        Log.d(TAG, "PRODUCT: " + android.os.Build.PRODUCT);

    }

    /**
     * 信息打印出输
     * 写入数据保存
     *
     * @param context
     */
    public static String showlog(Context context) {
        String systemModel = getSystemModel();//获取手机型号
        String deviceBrand = getDeviceBrand();//获取手机厂商
        String systemVersion = getSystemVersion();//获取当前手机系统版本号
        String getandroid = getandroid(context);//获取手机IMEI
        String systemLanguage = getSystemLanguage();//获取当前手机系统语言
        Locale[] systemLanguageList = getSystemLanguageList();//获取当前系统上的语言列表(Locale列表)
        return String.format("%s|%s|%s|%s", deviceBrand, systemModel, systemVersion, getandroid);
    }

}