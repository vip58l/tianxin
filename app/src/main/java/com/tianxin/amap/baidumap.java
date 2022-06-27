package com.tianxin.amap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.tianxin.Util.Toashow;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 用来调起地图APP工具类
 * https://blog.csdn.net/u013408059/article/details/109312637
 */
public class baidumap {
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名
    private static final String TAG = baidumap.class.getSimpleName();

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    private static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    private static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 打开高德地图导航功能
     * API地址 "https://lbs.amap.com/api/amap-mobile/guide/android/route
     *
     * @param slat  起点纬度
     * @param slon  起点经度
     * @param sname 起点名称 可不填（0,0，null）
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称 必填
     */
    public static void openGaoDeNavi(double slat, double slon, String sname, double dlat, double dlon, String dname) {
        boolean avilible = baidumap.isAvilible(DemoApplication.instance(), baidumap.PN_GAODE_MAP);
        if (!avilible) {
            Toashow.show("尚未安装高德地图,请先下载安装");
            return;
        }
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("amapuri://route/plan?sid=&sourceApplication=maxuslife");
            if (slat != 0) {
                builder.append("&sname=").append(sname)
                        .append("&slat=").append(slat)
                        .append("&slon=").append(slon);
            }
            builder.append("&dlat=").append(dlat)
                    .append("&dlon=").append(dlon)
                    .append("&dname=").append(dname)
                    .append("&dev=0")
                    .append("&t=0");

            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_GAODE_MAP);
            intent.setData(Uri.parse(uriString));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastLongMessage("尚未安装高德地图,请先下载安装");
        }
    }

    /**
     * 打开百度地图导航功能(默认坐标点是高德地图，需要转换)
     * API地址 "http://lbsyun.baidu.com/index.php?title=uri/api/android"
     *
     * @param slat  起点纬度
     * @param slon  起点经度
     * @param sname 起点名称 可不填（0,0，null）
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称 必填
     */
    public static void openBaiDuNavi(double slat, double slon, String sname, double dlat, double dlon, String dname) {
        boolean avilible = baidumap.isAvilible(DemoApplication.instance(), baidumap.PN_BAIDU_MAP);
        if (!avilible) {
            Toashow.show("尚未安装腾讯地图,请先下载安装");
            return;
        }
        try {
            String uriString = null;
            double destination[] = gaoDeToBaidu(dlat, dlon);
            dlat = destination[0];
            dlon = destination[1];

            StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");
            if (slat != 0) {
                double[] origin = gaoDeToBaidu(slat, slon);
                slat = origin[0];
                slon = origin[1];

                builder.append("origin=latlng:")
                        .append(slat)
                        .append(",")
                        .append(slon)
                        .append("|name:")
                        .append(sname);
            }
            builder.append("&destination=latlng:")
                    .append(dlat)
                    .append(",")
                    .append(dlon)
                    .append("|name:")
                    .append(dname);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_BAIDU_MAP);
            intent.setData(Uri.parse(uriString));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastLongMessage("尚未安装腾讯地图,请先下载安装");
        }
    }

    /**
     * 打开腾讯地图
     * API地址: "https://lbs.qq.com/webApi/uriV1/uriGuide/uriMobileRoute"
     *
     * @param slat  起点纬度
     * @param slon  起点经度
     * @param sname 起点名称 可不填（0,0，null）
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称 必填
     *              驾车：type=drive，policy有以下取值
     *              0：较快捷
     *              1：无高速
     *              2：距离
     *              policy的取值缺省为0
     *              &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     */
    public static void openTencentMap(double slat, double slon, String sname, double dlat, double dlon, String dname) {
        boolean avilible = baidumap.isAvilible(DemoApplication.instance(), baidumap.PN_TENCENT_MAP);
        if (!avilible) {
            Toashow.show("尚未安装腾讯地图,请先下载安装");
            return;
        }
        try {
            String uriString = null;
            StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&referer=zhongshuo");
            if (slat != 0) {
                builder.append("&from=").append(sname)
                        .append("&fromcoord=").append(slat)
                        .append(",")
                        .append(slon);
            }
            builder.append("&to=").append(dname)
                    .append("&tocoord=").append(dlat)
                    .append(",")
                    .append(dlon);
            uriString = builder.toString();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage(PN_TENCENT_MAP);
            intent.setData(Uri.parse(uriString));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.toastLongMessage("尚未安装腾讯地图,请先下载安装");
        }
    }


    /**
     * 判断是否已安装应用包
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> pName = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


}