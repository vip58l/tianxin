/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/20 0020
 */


package com.tencent.qcloud.costransferpractice.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class FileUtils {
    private final static String PREFIX_VIDEO = "video/";

    /**
     * Get the Mime Type from a File
     *
     * @param fileName 文件名
     * @return 返回MIME类型
     * thx https://www.oschina.net/question/571282_223549
     * add by fengwenhua 2017年5月3日09:55:01
     */
    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileName);
        return type;
    }

    /**
     * 根据文件后缀名判断 文件是否是视频文件
     *
     * @param fileName 文件名
     * @return 是否是视频文件
     */
    public static boolean isVedioFile(String fileName) {
        String mimeType = getMimeType(fileName);
        return !TextUtils.isEmpty(fileName) && mimeType.contains(PREFIX_VIDEO);
    }

    /**
     * 是否为图片
     **/
    public static boolean getFileType(String fileName) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        return bitmap != null;
    }



    /**
     * MD5加密码
     *
     * @param key
     * @return
     */
    public static String md532(String key) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = key.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * MD5加密码
     *
     * @param key
     * @return
     */
    public static String md5(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(key.getBytes(StandardCharsets.UTF_8));
            return toHex(bytes).toLowerCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized int getVersionCode(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取图标 bitmap
     *
     * @param context
     */
    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); //xxx根据自己的情况获取drawable
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    public static String url() {
        return "http://v.6.cn/minivideo/getlist.php?act=recommend&pageSize=20&page=1&";
    }

    /**
     * 获取当前应用版本号
     *
     * @throws PackageManager.NameNotFoundException
     */
    public static String[] getPackageversion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = info.versionName;
            String packageName = info.packageName;
            int code = info.versionCode;

            //用数组的方式返回多个数据的内容
            String[] array = new String[3];
            array[0] = versionName;
            array[1] = packageName;
            array[2] = String.valueOf(code);
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String DateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String format = df.format(new Date());// new Date()为获取当前系统时间
        return format;
    }


    /**
     * 获取当前系统时间
     *
     * @return
     */
    public static String DateTime(boolean key) {
        SimpleDateFormat df = new SimpleDateFormat(key ? "yyyyMMddHHmmss" : "yyyyMMdd");//设置日期格式
        String format = df.format(new Date());// new Date()为获取当前系统时间
        return format;
    }

    /**
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        String res;
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = simpleDateFormat.parse(s);
        } catch (Exception e) {
        }
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = Long.valueOf(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String beginDate, boolean istime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(istime ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(Long.parseLong(beginDate) * 1000)); // 时间戳转换日期
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToTime(long stamp) {
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp * 1000);
        time = simpleDateFormat.format(date);
        return time;
    }

    /**
     * 当前年月日时分秒+五位随机数
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return str + rannum;// 当前时间 + 系统5随机生成位数
    }


    /**
     * Android中如何禁止用户截屏
     *
     * @param activity
     */
    public static void mysetFlags(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }


    /**
     * 网络状态
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void hideBottomUIMenu(Activity activity) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 黑色底部导航
     *
     * @param activity
     */
    public static void AsetctivityBLACK(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    /**
     * 获取文件名称
     *
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String[] result = path.split("/");
        return result[result.length - 1];
    }

    /**
     * 时间处理
     */
    public static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return hours > 0 ? String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds) : String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }



}
