package com.tianxin.activity.shareqrcode;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.activity.CaptureActivity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Share;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.DensityUtil;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.SystemUtil;
import com.tianxin.Util.Config;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 推荐分享好友
 */
public class activity_scan_qrcode extends BasActivity2 {

    @BindView(R.id.back)
    itembackTopbr itembackTopbr;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.nameid)
    TextView nameid;
    @BindView(R.id.send1)
    TextView send1;
    @BindView(R.id.send2)
    TextView send2;
    @BindView(R.id.myqrcode)
    ImageView myqrcode;
    @BindView(R.id.qrcodeliine)
    LinearLayout qrcodeliine;
    Share share = new Share();

    public static void setAction(Activity context) {
        Intent intent = new Intent(context, activity_scan_qrcode.class);
        context.startActivity(intent);
    }

    private final int REQUEST_CODE = 1000;

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.c_main1));
        return R.layout.scan_qrcode;
    }

    @Override
    public void iniview() {
        itembackTopbr.setHaidtopBackgroundColor(getResources().getColor(R.color.c_main1));
        itembackTopbr.settvsetTextColor(Color.WHITE);
        itembackTopbr.settitle(getString(R.string.tv_msg_123));
        itembackTopbr.setSendbtn(R.mipmap.alivc_screen_mode_large);
        itembackTopbr.setSendbtnshow(true);

        String url = Webrowse.register + "?t=" + userInfo.getUserId();
        myqrcode.setImageBitmap(createCode(url));


        share.setH5Url(url);
        share.setText("您的好友邀请你加入" + getString(R.string.app_name));
        share.setText("上面小姐姐真人认证,脱单神器 立即下载体验！");

        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glideload.loadImage(icon, userInfo.getAvatar());
        }
        name.setText(userInfo.getName());
        nameid.setText(String.format("%sid:%s", getString(R.string.app_name), userInfo.getUserId()));

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.send1, R.id.send2, R.id.sendbtn})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1: {
                if (checkinidate()) {
                    activity_shareqrcode.viewSaveToImage1(context, qrcodeliine);
                }
                break;
            }
            case R.id.send2: {

                //分享链接
                WXpayObject.getsWXpayObject().sharewechat(share, SendMessageToWX.Req.WXSceneSession);

                //分享图片
                // sharewechat();

                break;
            }
            case R.id.sendbtn: {
                if (checkinidate()) {
                    CaptureActivity();
                }
                break;
            }
        }
    }

    @Override
    public void OnEorr() {

    }


    public void getWechatApi() {
        ClipboardManager tvCopy = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("复制内容", String.format(getString(R.string.tv_msg215) + "", getString(R.string.app_name), userInfo.getUserId().hashCode(), Webrowse.register + "?t=" + userInfo.getUserId()));
        tvCopy.setPrimaryClip(clipData);
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.toastLongMessage(getString(R.string.tv_msg214));
        }


    }


    private boolean checkinidate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            list.add(Manifest.permission.CAMERA);
            int star = ContextCompat.checkSelfPermission(this, list.get(0));
            if (star != PackageManager.PERMISSION_GRANTED) {
                SystemUtil.getPermission(this, list);
                return false;
            } else {
                return true;
            }
        }
        return false;

    }


    private void CaptureActivity() {
        startActivityForResult(new Intent(context, CaptureActivity.class), REQUEST_CODE);
    }

    private void setCapture(String result) {
        boolean b1 = result.toLowerCase().startsWith("http://");
        boolean b2 = result.toLowerCase().startsWith("https://");
        boolean b3 = result.toLowerCase().endsWith(".apk");
        if (b1 || b2) {
            //打开浏览器
            Intent intent = new Intent(context, DyWebActivity.class);
            intent.putExtra(Constants.VIDEOURL, result);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && data != null) {
            String result = data.getStringExtra(CaptureActivity.SCAN_QRCODE_RESULT);
            setCapture(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //已授权标识 GRANTED---授权  DINIED---拒绝
        if (requestCode == Config.sussess) {
            int granted = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (granted != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, Config.sussess);
                return;
            }

            //所有权限已获取
            CaptureActivity();
        }

    }


    /**
     * 生成二维码1
     */
    public Bitmap createCode(String codeUrl) {
        try {
            int width = DensityUtil.dp3px(context, 160);
            int height = DensityUtil.dp3px(context, 160);
            final Bitmap codeBitmap = DensityUtil.createQRImage(codeUrl, width, height);
            return codeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sharewechat() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = activity_shareqrcode.viewSaveToImage1(context, qrcodeliine);
                WXpayObject.getsWXpayObject().sharewechat(context, bitmap);
            }
        });
    }

}
