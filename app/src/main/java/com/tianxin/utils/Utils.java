package com.tianxin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tianxin.app.DemoApplication;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static final int REQ_PERMISSION_CODE = 0x100;

    //权限检查
    public static boolean checkPermission1(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissions = new ArrayList<>();
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(DemoApplication.instance(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(DemoApplication.instance(), Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(DemoApplication.instance(), Manifest.permission.RECORD_AUDIO)) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(DemoApplication.instance(), Manifest.permission.READ_PHONE_STATE)) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(DemoApplication.instance(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissions.size() != 0) {
                String[] permissionsArray = permissions.toArray(new String[1]);
                ActivityCompat.requestPermissions(activity, permissionsArray, REQ_PERMISSION_CODE);
                return false;
            }
        }

        return true;
    }


    /**
     * 检查手机申请权限
     *
     * @return
     */
    public static boolean checkPermission2(Activity activity) {
        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        list.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        list.add(Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);

        //list.add(Manifest.permission.READ_PHONE_STATE);
        //list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        for (String permission : list) {
            int star = ContextCompat.checkSelfPermission(activity, permission);
            if (star != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
