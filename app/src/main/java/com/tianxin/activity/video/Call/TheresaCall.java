package com.tianxin.activity.video.Call;

import static android.net.Uri.parse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.tianxin.Receiver.FloatingService;
import com.tianxin.Util.Toashow;

public class TheresaCall extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, parse("package:" + getPackageName())), 0);
            } else {
                //启动服务
                startService(new Intent(this, FloatingService.class));
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toashow.show("授权失败");
            } else {
                Toashow.show("授权成功");
                startService(new Intent(this, FloatingService.class));
                finish();
            }
        }
    }


}

