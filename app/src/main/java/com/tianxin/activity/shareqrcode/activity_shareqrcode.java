package com.tianxin.activity.shareqrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.api.Share;
import com.tianxin.R;
import com.tianxin.Util.DensityUtil;
import com.tianxin.Util.Imagecompressiontool;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分享二维码
 */
public class activity_shareqrcode extends BasActivity2 implements View.OnLongClickListener {
    private String TAG = activity_shareqrcode.class.getSimpleName();
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.shareqrcodes)
    RelativeLayout shareqrcodes;
    private String user_openId, accessToken, refreshToken, scope;
    private Share share;

    public static void starAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_shareqrcode.class);
        context.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        user_openId = intent.getStringExtra("openId");
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        scope = intent.getStringExtra("scope");
        Log.d(TAG, "onNewIntent: " + user_openId);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.shareqrcode;
    }

    @Override
    public void iniview() {
        qrcode(Webrowse.register + "?t=" + userInfo.getUserId());
        image3.setOnLongClickListener(this);
    }

    @Override
    public void initData() {
        datamodule.Sharewchat(new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                share = (Share) object;
            }

            @Override
            public void onSuccess(String msg) {
                Toashow.show(msg);

            }
        });
    }

    /**
     * 生成图片显示
     *
     * @param content
     */
    private void qrcode(String content) {
        Bitmap bitmap = createCode(content);
        image3.setImageBitmap(bitmap);
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

    @Override
    @OnClick({R.id.back, R.id.l1, R.id.l3, R.id.save})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.l1:
                //分享朋友圈
                WXpayObject.getsWXpayObject().sharewechat(share, SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.l3:
                //分享到微信
                WXpayObject.getsWXpayObject().sharewechat(share, SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.back:
                onBackPressed();
                break;
            case R.id.save:
                viewSaveToImage1(context, shareqrcodes);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public boolean onLongClick(View v) {
        viewSaveToImage1(context, shareqrcodes);
        return true;
    }

//******************** 生成图片到本地 ****************************************************//

    /**
     * 把一个View转换成图片
     */
    public static Bitmap viewSaveToImage1(Context context, View view) {
        //New创建文件
        File file = Imagecompressiontool.dataDir();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        Imagecompressiontool.oNqualityCompress(bitmap, file);
        view.setDrawingCacheBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(false);
        saveImageToGallery(context, file);
        return bitmap;
    }

    /**
     * 把一个View转换成图片
     *
     * @param view
     * @return
     */
    public String viewSaveToImage2(Context context, View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        //把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);
        String res = saveImageToGallery(context, cachebmp);
        view.destroyDrawingCache();
        return res;
    }

    /**
     * 把一个View转换成图片
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        // 如果不设置canvas画布为白色，则生成透明
        v.layout(0, 0, w, h);
        v.draw(c);
        return bmp;
    }

    /**
     * 将图片文件插入到相册
     *
     * @param context
     * @param bmp
     * @return
     */
    private static String saveImageToGallery(Context context, Bitmap bmp) {

        File file = Imagecompressiontool.dataDir();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtil.toastLongMessage("保存成功");
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将图片文件插入到相册
     *
     * @param context
     * @param file
     */
    private static void saveImageToGallery(Context context, File file) {
        if (!file.exists()) {
            return;
        }
        String photoPath = file.getAbsolutePath();
        String photoName = file.getName();
        try {
            //把文件插入到系统图库
            //ContentResolver contentResolver = context.getContentResolver();
            //MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);

            //通知图库更新
            Uri parse = Uri.parse("file://" + photoPath);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, parse));

            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            ToastUtil.toastLongMessage("保存成功");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
