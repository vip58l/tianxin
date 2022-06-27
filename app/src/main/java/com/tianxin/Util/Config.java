package com.tianxin.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.tianxin.R;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.app.DemoApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

//猫抓直播
//http://www.miaobolive.com/Ajax/live/Live_RoomSelect.aspx            30条
//http://www.miaobolive.com/Ajax/Home/GetRoomHotNewSome.ashx?&page=1  30条

//https://www.yequ.live/index.html椰趣直播
//https://wap.yequ.live/api/mainpage/mainPage/hotRooms|直播2           89条

//6房音短视频
//http://v.6.cn/minivideo/getlist.php?act=recommend&pageSize=20&page=1
public class Config {
    private final static String info = "info";
    private final static String edit = "edit";
    public static final int sussess = 200;
    public static final int on = 201;
    public static final int fail = 400;
    public static final int setResult = 800;
    public final static String api = BuildConfig.HTTP_API;
    public static final String miaobolive = BuildConfig.miaobolive;

    public static int a(Context context, String String1, String String2) {
        return context.getResources().getIdentifier(String2, String1, context.getPackageName());
    }

    public static boolean ok(String paramString) {
        return (paramString != null) && (!paramString.equals("")) && (!paramString.equals("null") && (paramString.contains("|")));
    }

    public static String mp(String paramString) {
        int i = paramString.lastIndexOf("/");
        int j = paramString.lastIndexOf(".");
        if ((i != -1) && (j != -1))
            return paramString.substring(i + 1, j);
        return "";
    }

    public static int Display(Context context, float paramFloat) {
        return (int) (0.5F + paramFloat * context.getResources().getDisplayMetrics().density);
    }

    /**
     * 在写系统写入参数
     *
     * @param context
     * @param Model
     * @param device
     * @param Version
     * @param IMEI
     */
    public static void putString(Context context, String Model, String device, String Version, String IMEI) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("Model", Model);
        edit.putString("device", device);
        edit.putString("Version", Version);
        edit.putString("IMEI", IMEI);
        edit.commit();
    }

    /**
     * 写系统写入参数
     *
     * @param key
     * @param value
     */
    public static void putString(String key, Object value) {
        SharedPreferences sp = DemoApplication.instance().getSharedPreferences(edit, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, new Gson().toJson(value));
        edit.commit();
    }

    /**
     * 写系统写入参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 写入参数
     *
     * @param context
     * @param key
     * @return
     */
    public static void putString(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    /**
     * 写入参数
     *
     * @param context
     * @param key
     * @return
     */
    public static void putString(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    /**
     * 取值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        return sp.getString(key, "");
    }


    /**
     * 返回 true false
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean getboolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static int getint(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 删除单值
     *
     * @param context
     * @param key
     */
    public static void DeleteString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    /**
     * 删除全部
     */
    public static void DeLeteall(Context context) {
        SharedPreferences sp = context.getSharedPreferences(info, MODE_PRIVATE);
        sp.edit().clear().commit();

    }

    /**
     * DeLeteUserinfo删除全部
     */
    public static void DeLeteUserinfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(Constants.USERINFO, MODE_PRIVATE);
        sp.edit().clear().commit();

    }

    /**
     * DeLeteUserinfo删除全部
     */
    public static void DeLeteUserinfo(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(Constants.USERINFO, MODE_PRIVATE);
        sp.edit().remove(key).commit();

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
     * @return 当前应用的版本名称
     */
    public static int getVersionCode() {
        try {
            PackageManager packageManager = DemoApplication.instance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(DemoApplication.instance().getPackageName(), 0);
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
     * 获取当前系统时间
     *
     * @return
     */
    public static String Datehh() {
        SimpleDateFormat df = new SimpleDateFormat("HH");//设置日期格式
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
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s) * 1000;
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s, int f) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(s) * 1000;
            Date date = new Date(lt);
            res = simpleDateFormat.format(date);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
     * 显示时间是多久之前，比如10分钟前，8小时前，一月前等等
     *
     * @param timestamp
     * @return
     */
    public static String timestamp(String timestamp) {
        long lt = new Long(timestamp) * 1000;
        Date date = new Date(lt);
        return TimeUtil.getTimeFormatText(date);

    }


    /**
     * Android中设置禁止用户截屏
     *
     * @param activity
     */
    public static void mysetFlags(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    /**
     * 网络异常
     * 网络异常判断
     *
     * @param context
     * @return
     */
    public static boolean networkConnected(Context context) {
        return wifimone.isNetworkConnected(context);
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
     * 网络状态
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) DemoApplication.instance().getSystemService(Context.CONNECTIVITY_SERVICE);
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
     * 黑色底部导航
     *
     * @param activity
     */
    public static void AsetctivityBLACK(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setNavigationBarColor(color);
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
     * 获取文件名称
     *
     * @param path
     * @return
     */
    public static String getFileName(String path, String j) {
        String[] result = path.split(j);
        return result[result.length - 1];
    }

    /**
     * 播放视频时间处理
     */
    public static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000); //除1000
        int seconds = totalSeconds % 60; //时
        int minutes = (totalSeconds / 60) % 60; //分
        int hours = totalSeconds / 3600; //秒
        return hours > 0 ? String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds) : String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    /**
     * 距离千米-米
     *
     * @param distance
     */
    public static String getdistance(float distance) {
        float Kilometer = (distance / 1000); //千米
        float rice = (distance % 1000); //米
        StringBuffer sb = new StringBuffer();
        if (Kilometer > 0.1f) {
            sb.append(String.format("%.1f", Kilometer) + "Km");
        } else {
            sb.append(String.format("%.2f", rice) + "米");
        }
        return sb.toString();
    }

    /*
     * 判断服务是否启动,context上下文对象 ，className服务的name
     */
    public static boolean isServiceRunning(String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) DemoApplication.instance().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().contains(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 得到Cookie
     */
    public static String GetCOOKIE(String key) {
        SharedPreferences preference = DemoApplication.instance().getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }

    /**
     * 保存Cookie
     */
    public static void SetCOOKIE(String key, String value) {
        SharedPreferences preference = DemoApplication.instance().getSharedPreferences("COOKIE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 读取assets 中的文件
     *
     * @param fileName
     * @param context
     * @return
     */
    public static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStreamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 返回状态
     */
    public static boolean loginByGet() throws IOException {
        URL url = new URL(BuildConfig.HTTP_API);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        return code == 200;
    }

    /**
     * 打开指定应用
     *
     * @param context
     */
    public static void openactiity(Context context, String appPackageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
        context.startActivity(intent);
    }


    /**
     * 随机签名
     *
     * @return
     */
    public static String getName() {
        String[] stringArray = DemoApplication.instance().getResources().getStringArray(R.array.myarray);
        int result = new Random().nextInt(stringArray.length);
        return stringArray[result];
    }

    /**
     * 随机人气
     *
     * @return
     */
    public static int random() {
        return (int) (Math.random() * 1000 + 1);
    }


    /**
     * 过滤手机号代码
     *
     * @param text
     * @return
     */
    public static String FindPhoneNumber(String text) {
        if (TextUtils.isEmpty(text)) {
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
        if (TextUtils.isEmpty(text)) {
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

    /**
     * 判断手机号格式
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * QQ号或者微信号判断
     *
     * @param qqorwx
     * @return
     */
    public static boolean isQQOrWX(String qqorwx) {
        if (qqorwx.equals("")) {
            return false;
        }
        //QQ号最短5位，微信号最短是6位最长20位
        Pattern p = Pattern.compile("^[a-zA-Z0-9_-]{5,19}$");
        Matcher m = p.matcher(qqorwx);
        return m.matches();
    }

    /**
     * 项目只需要判断是中文，还是英文，或中文+英文，过滤掉其他特殊字符或表情等等
     *
     * @param name
     * @return
     */
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
     * 方案一:比较精准的判断手机段位,但是随着手机号段的增多要不断的修改正则
     *
     * @param phone
     * @return
     */
    public boolean isPhoneNumber1(String phone) {
        String regExp = "^[1]([3][0-9]{1}|50|51|52|53|54|55|56|57|58|59|47|77|80|81|82|83|84|85|86|87|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }

    /**
     * 方案二:相对于方法一可以算得上一劳永逸,对140,141等目前不存在的号段没办法判断
     *
     * @param phone
     * @return
     */
    public boolean isPhoneNumber2(String phone) {
        String regExp = "^1[3|4|5|7|8]\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phone);
        return m.find();
    }
}