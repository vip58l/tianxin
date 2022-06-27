/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/15 0015
 */


package com.tianxin.amap;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.tianxin.Module.Datamodule;
import com.tianxin.Util.Config;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.amap.api.location.CoordinateConverter.calculateLineDistance;

/**
 * 定位当前地址
 * 地理围栏 即范围 用于共享单车使用
 * 坐标转换与位置判断 两点之间的距离位置
 */
public class lbsamap {
    public static String TAG = lbsamap.class.getName();

    /*********************** 高德定位服务 ******************************/
    public AMapLocationClient mLocationClient = null;
    private AMapLocationClient locationClientContinue = null;
    public AMapLocationClientOption mLocationOption = null;
    private Callback callBack;

    /**
     * 静态方法返回对像
     *
     * @param callBack
     * @return
     */
    public static lbsamap getmyLocation(Callback callBack) {
        lbsamap lbsamap = new lbsamap(callBack);
        return lbsamap;
    }

    private lbsamap(Callback callBack) {
        this.callBack = callBack;
        //单次客户端的定位监听初始化
        inimLocationClient();

        //连续后台多次监听动态定位初始化
        // inidatelocationClientContinue();

    }


    /**
     * 单次客户端的定位监听初始化
     */
    private void inimLocationClient() {
        //初始化定位
        mLocationClient = new AMapLocationClient(DemoApplication.instance());
        //设置定位回调监听
        mLocationClient.setLocationListener(locationSingleListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        //给定位客户端设置参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        mLocationClient.stopLocation();
        mLocationClient.startLocation();

        //选择定位模式
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，需要在室外环境下才可以成功定位。注意，自 v2.9.0 版本之后，仅设备定位模式下支持返回地址描述信息。
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //获取一次定位结果： 设置单次定位
        //该方法默认为false。
        //mLocationOption.setOnceLocation(true);

        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);

        //自定义连续定位
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        //mLocationOption.setInterval(1000);

        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);

        //设置定位请求超时时间，默认为30秒。
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

        //关闭缓存机制 设置是否开启定位缓存机制
        //mLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 单次客户端的定位监听回调
     */
    AMapLocationListener locationSingleListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null && location.getErrorCode() == 0) {
                getmap(location);
                Datamodule.getInstance().toAMapLocation(location, null);

            }
        }

    };


    /**
     * 连续后台多次监听动态定位初始化
     */
    private void inidatelocationClientContinue() {
        //创建连续定位客户端
        locationClientContinue = new AMapLocationClient(DemoApplication.instance());
        //设置监听事件
        locationClientContinue.setLocationListener(locationContinueListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //给定位客户端对象设置定位参数
        locationClientContinue.setLocationOption(mLocationOption);
        //启动定位
        locationClientContinue.startLocation();

        //启动辅助H5定位
        //locationClientContinue.startAssistantLocation();
        //停止辅助H5定位
        //locationClientContinue.stopAssistantLocation();
    }

    /**
     * 连续客户端的定位监听回调事件
     */
    private AMapLocationListener locationContinueListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            Log.d(TAG, "onLocationChanged: 暂时不使用");
            Log.d(TAG, "onLocationChanged: " + location.getAddress());
        }
    };


    /**
     * 当定位成功时，可在如上判断中解析amapLocation对象的具体字段，参考如下：
     */
    private void getmap(AMapLocation amapLocation) {
        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表 https://developer.amap.com/api/android-location-sdk/guide/utilities/location-type
        amapLocation.getLatitude();//获取纬度
        amapLocation.getLongitude();//获取经度
        amapLocation.getAccuracy();//获取精度信息 米
        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        amapLocation.getCountry();//国家信息
        amapLocation.getProvince();//省信息
        amapLocation.getCity();//城市信息
        amapLocation.getDistrict();//城区信息
        amapLocation.getStreet();//街道信息
        amapLocation.getStreetNum();//街道门牌号信息
        amapLocation.getCityCode();//城市编码
        amapLocation.getAdCode();//地区编码
        amapLocation.getAoiName();//获取当前定位点的AOI信息
        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
        amapLocation.getFloor();//获取当前室内定位的楼层
        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
        apmgetTime(amapLocation); //获取定位时间
        callBack.onSuccess(amapLocation); //设置回调接口
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy(); //销毁定位客户端，同时销毁本地定位服务。




        //scalculateLineDistance(amapLocation);
    }

    /**
     * 获取APK当前签名文件的SHA1
     *
     * @param context
     * @return
     */
    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取定位时间
     *
     * @param amapLocation
     * @return
     */
    private static String apmgetTime(AMapLocation amapLocation) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//获取定位时间
        Date date = new Date(amapLocation.getTime());
        String format = df.format(date);
        return format;
    }

    /*********************** 高德定位服务结束 ******************************/

    /**
     * 坐标转换
     */
    public static void CoordType() {
        CoordinateConverter converter = new CoordinateConverter(DemoApplication.instance());
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标点 DPoint类型
        // converter.coord(sourceLatLng);
        // 执行转换操作
        try {
            DPoint desLatLng = converter.convert();
            Log.d(TAG, "坐标转换 desLatLng: " + desLatLng.getLatitude());
            Log.d(TAG, "坐标转换 getLongitude: " + desLatLng.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断位置所在区域
     *
     * @param latitude
     * @param longitude
     */
    public void isAMapDataAvailable(double latitude, double longitude) {
        CoordinateConverter converter = new CoordinateConverter(DemoApplication.instance());
        //返回true代表当前位置在大陆、港澳地区，反之不在。
        boolean isAMapDataAvailable = CoordinateConverter.isAMapDataAvailable(latitude, longitude);
        //第一个参数为纬度，第二个为经度，纬度和经度均为高德坐标系。
        Log.d(TAG, "所在区域中国: " + isAMapDataAvailable);
    }

    /**
     * 两点间距离计算
     *
     * @param amapLocation
     */
    public static void scalculateLineDistance(AMapLocation amapLocation) {

        DPoint startlatlng = new DPoint();
        startlatlng.setLongitude(amapLocation.getLongitude());  //经度
        startlatlng.setLatitude(amapLocation.getLatitude());    //纬度

        DPoint endlatlng = new DPoint();
        endlatlng.setLongitude(112.144146);  //经度
        endlatlng.setLatitude(32.042426);    //纬度

        //startLatlng起点 endLatlng终点 两点间距离 单位：米
        float distance = calculateLineDistance(startlatlng, endlatlng);

        StringBuffer sb1 = new StringBuffer();
        sb1.append("距离: " + distance + "米\n");
        sb1.append("距离: " + Config.getdistance(distance) + "米\n");
        sb1.append("经度: " + amapLocation.getLongitude() + "\n");
        sb1.append("纬度: " + amapLocation.getLatitude() + "\n");
        sb1.append("精度: " + amapLocation.getAccuracy() + "米\n");

        StringBuffer sb2 = new StringBuffer();
        sb2.append("定位成功" + "\n");
        sb2.append("定位类型: " + amapLocation.getLocationType() + "\n");
        sb2.append("经    度    : " + amapLocation.getLongitude() + "\n");
        sb2.append("纬    度    : " + amapLocation.getLatitude() + "\n");
        sb2.append("精    度    : " + amapLocation.getAccuracy() + "米" + "\n");
        sb2.append("提供者    : " + amapLocation.getProvider() + "\n");
        sb2.append("速    度    : " + amapLocation.getSpeed() + "米/秒" + "\n");
        sb2.append("角    度    : " + amapLocation.getBearing() + "\n");
        sb2.append("星    数    : " + amapLocation.getSatellites() + "\n");
        sb2.append("国    家    : " + amapLocation.getCountry() + "\n");
        sb2.append("省            : " + amapLocation.getProvince() + "\n");
        sb2.append("市            : " + amapLocation.getCity() + "\n");
        sb2.append("城市编码 : " + amapLocation.getCityCode() + "\n");
        sb2.append("区            : " + amapLocation.getDistrict() + "\n");
        sb2.append("区域 码   : " + amapLocation.getAdCode() + "\n");
        sb2.append("地    址    : " + amapLocation.getAddress() + "\n");
        sb2.append("兴趣点    : " + amapLocation.getPoiName() + "\n");
        sb2.append("定位时间: " + apmgetTime(amapLocation) + "\n");
        Log.d(TAG, "sb1: " + sb1.toString());
        Log.d(TAG, "sb2: " + sb2.toString());
    }

    /**
     * 两点间距离计算
     *
     * @param amapLocation
     * @param jwd
     * @return
     */
    public static String scalculateLineDistance(AMapLocation amapLocation, String jwd) {
        if (amapLocation == null||TextUtils.isEmpty(jwd)) {
            return "";
        }

        try {
            DPoint startlatlng = new DPoint();
            startlatlng.setLatitude(amapLocation.getLatitude());        //纬度
            startlatlng.setLongitude(amapLocation.getLongitude());      //经度
            String[] split = jwd.split(",");
            DPoint endlatlng = new DPoint();
            endlatlng.setLatitude(Float.parseFloat(split[1]));          //纬度
            endlatlng.setLongitude(Float.parseFloat(split[0]));         //经度
            float v = calculateLineDistance(startlatlng, endlatlng);
            if (v == 0) {
                return "";
            }
            //返回数据
            return Config.getdistance(v);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }
}

