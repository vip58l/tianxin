/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/15 0015
 */


package com.tianxin.Util;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

//权限数组（申请定位）
public class ActivityLocation {
    public static final int OPEN_SET_REQUEST_CODE = 100;
    public static final int OPEN_SET_REQUEST_CODE102 = 102;
    private static final String TAG = "ActivityLocation";

    public static boolean checkpermissions(Activity activity) {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        for (String permission : list) {
            int star = ContextCompat.checkSelfPermission(activity, permission);
            if (star != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, list.toArray(new String[list.size()]), OPEN_SET_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissions(Activity activity) {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
        for (String permission : list) {
            int star = ContextCompat.checkSelfPermission(activity, permission);
            if (star != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


}
