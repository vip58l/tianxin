package com.tencent.qcloud.tim.uikit.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    private static AlertDialog mPermissionDialog;

    public static boolean checkPermission(Context context, String permission) {
        boolean flag = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ActivityCompat.checkSelfPermission(context, permission);
            if (PackageManager.PERMISSION_GRANTED != result) {
                //showPermissionDialog(context, permission);
                TOActivityCompat(context, permission);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 检查及申请权限
     *相机 录音
     * @param context
     * @return
     */
    public static boolean checkPermission(Context context) {
        boolean flag = true;
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : listPermission) {
                int result = ActivityCompat.checkSelfPermission(context, s);
                if (PackageManager.PERMISSION_GRANTED != result) {
                    ActivityCompat.requestPermissions((Activity) context, listPermission.toArray(new String[listPermission.size()]), Constants.requestCode);
                    flag = false;
                }
            }

        }
        return flag;
    }

    /**
     * 申请权限
     *
     * @param context
     * @return
     */
    public static boolean checkPermission(Context context, int requestCode) {
        boolean flag = true;
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.CAMERA);
        listPermission.add(Manifest.permission.RECORD_AUDIO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String s : listPermission) {
                int result = ActivityCompat.checkSelfPermission(context, s);
                if (PackageManager.PERMISSION_GRANTED != result) {
                    flag = false;
                }
            }

        }
        return flag;
    }

    /**
     * 通知用户给权限
     * 使用该功能，需要开启权限，鉴于您禁用相关权限，请手动设置开启权限<
     *
     * @param context
     */
    private static void showPermissionDialog(final Context context, String permission) {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(context)
                    .setMessage(TUIKit.getAppContext().getString(R.string.permission_content))
                    .setPositiveButton(TUIKit.getAppContext().getString(R.string.setting), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + context.getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            context.startActivity(intent);


                        }
                    })
                    .setNegativeButton(TUIKit.getAppContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    private static void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    /**
     * 申请拍照，电话 视频
     *
     * @param context
     * @param permission
     */
    private static void TOActivityCompat(Context context, String permission) {
        List<String> list = new ArrayList<>();
        list.add(permission);
        String[] permissions = list.toArray(new String[list.size()]);
        ActivityCompat.requestPermissions((Activity) context, permissions, 100);
    }


}
