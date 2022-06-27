package com.tianxin.activity.Web;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.sysmessage;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import butterknife.BindView;

/**
 * 浏览器
 */
public class DyWebActivity extends BasActivity2 implements View.OnTouchListener {
    private String TAG = DyWebActivity.class.getSimpleName();
    @BindView(R.id.dy_webview)
    WebView webView;
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.lin1)
    LinearLayout lin1;
    @BindView(R.id.conversation)
    ConstraintLayout conversation;
    Handlers handler = new Handlers();
    private static final int QRReader = 2021;


    public static void starAction(Activity context, sysmessage sysmessage) {
        if (sysmessage != null) {
            Map<String, String> map = new HashMap<>();
            map.put(com.tianxin.utils.Constants.USERID, UserInfo.getInstance().getUserId());
            map.put(com.tianxin.utils.Constants.ID, String.valueOf(sysmessage.getId()));
            map.put(com.tianxin.utils.Constants.TOKEN, UserInfo.getInstance().getToken());
            String path = Webrowse.showsysmessage + "?" + Webrowse.getMap(map, 3);
            Intent intent = new Intent();
            intent.setClass(context, DyWebActivity.class);
            intent.putExtra(com.tianxin.utils.Constants.sysmessage, new Gson().toJson(sysmessage));
            intent.putExtra(com.tianxin.utils.Constants.VIDEOURL, path);
            context.startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }

    public static void starAction(Context context, String videoUrl) {
        Intent intent = new Intent(context, DyWebActivity.class);
        intent.putExtra(Constants.VIDEOURL, videoUrl);
        context.startActivity(intent);
    }

    /**
     * 带回调参数
     *
     * @param activity
     * @param videoUrl
     */
    public static void starAction(Activity activity, String videoUrl) {
        Intent intent = new Intent(activity, DyWebActivity.class);
        intent.putExtra(Constants.VIDEOURL, videoUrl);
        activity.startActivityForResult(intent, Constants.requestCode);
    }

    @Override
    protected int getview() {
        return R.layout.activity_dy;
    }

    @Override
    public void iniview() {
        videoUrl = getIntent().getStringExtra(Constants.VIDEOURL);
        JSON = getIntent().getStringExtra(Constants.JSON);
        iniurl();
    }

    private void iniurl() {
        lin1.setVisibility(View.VISIBLE);
        webView.loadUrl(videoUrl);
        webView.setWebViewClient(new webviewClient());
        webView.setWebChromeClient(new myWebChromeClient());
        webView.setOnLongClickListener(new setOnLongClickListener());
        webView.setDownloadListener(downloadListener);
        basestartActivity.WebSettings(webView);
    }

    @Override
    public void initData() {
        conversation.setOnTouchListener(this);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        iniurl();
        return false;
    }

    private class webviewClient extends WebViewClient {

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
            Log.d(TAG, "onFormResubmission: ");
        }

        public void onPageFinished(WebView view, String url) {
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                //设置网页加载完成再显示图片
                view.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setBlockNetworkImage(false);
                itemback.settitle(view.getTitle());
            }
            super.onPageFinished(view, url);


        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            itemback.settitle(view.getTitle());
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            lin1.setVisibility(View.GONE);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            lin1.setVisibility(View.GONE);
            super.onReceivedError(view, request, error);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //拦截超链接 & 自定义跳转 true 为拦截点击事件 false 就是正常的处理事件
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
                return shouldOverrideUrlLoadingByApp(view, request.toString());
            } else {
                return shouldOverrideUrlLoadingByApp(view, request.getUrl().toString());
            }
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "覆盖Url加载:2 ");
            view.loadUrl(url);
            return true;
        }


    }

    private class myWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                itemback.settitle(view.getTitle());
            } else {
                progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                progressBar.setProgress(newProgress);//设置进度值
            }
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("")
                    .setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {

            final EditText et = new EditText(view.getContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("")
                    .setMessage(message)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.confirm(et.getText().toString());
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            result.cancel();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return true;
        }

    }

    /**
     * 根据url的scheme处理跳转第三方app的业务
     */
    public boolean shouldOverrideUrlLoadingByApp(WebView view, String url) {
        if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
            //不处理http, https, ftp的请求
            return false;
        }
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            intent.setComponent(null);
            if (isInstall(intent)) {
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断app是否已安装
     * @param intent
     * @return
     */
    public boolean isInstall(Intent intent) {
        return getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }


    /**
     * ebView 实现长按保存图片
     */
    public class setOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            WebView.HitTestResult result = webView.getHitTestResult();
            if (result == null) {
                return false;
            }

            //操作类型
            int TYPE = result.getType();
            switch (TYPE) {
                case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE: // 带有链接的图片类型
                case WebView.HitTestResult.IMAGE_TYPE: // 处理长按图片的菜单项
                case WebView.HitTestResult.IMAGE_ANCHOR_TYPE: // 处理长按图片的菜单项

                    //Saveurlpath(result.getExtra()); //获取图片链接

                    //开启一条子线程
                    new Thread(new renrqcode(result.getExtra())).start();

                    return true;
                case WebView.HitTestResult.EDIT_TEXT_TYPE: // 选中的文字类型
                    break;
                case WebView.HitTestResult.PHONE_TYPE: // 处理拨号
                    break;
                case WebView.HitTestResult.EMAIL_TYPE: // 处理Email
                    break;
                case WebView.HitTestResult.GEO_TYPE:  //地图类型
                    break;
                case WebView.HitTestResult.UNKNOWN_TYPE: //未知
                    break;
                case WebView.HitTestResult.SRC_ANCHOR_TYPE: // 超链接
                    Toast.makeText(DyWebActivity.this, "超链接", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * AlertDialog保存的地址URL
     * @param url
     */
    public void Saveurlpath(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DyWebActivity.this);
        builder.setTitle("提示");
        builder.setMessage("保存图片到本地");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //联网操作需要开启子线程
                new Thread(new run(url)).start();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    /**
     * 将图片保存在相册
     * @param context
     * @param file
     */
    public void displayToGallery(Context context, File file) {
        if (file == null || !file.exists()) {
            handler.sendEmptyMessage(Config.fail);
            return;
        }
        String photoPath = file.getAbsolutePath();
        String photoName = file.getName();

        // 把文件插入到系统图库
        try {
            ContentResolver contentResolver = context.getContentResolver();
            MediaStore.Images.Media.insertImage(contentResolver, photoPath, photoName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        //sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + photoPath)));
        handler.sendEmptyMessage(Config.sussess);

    }

    /**
     * 子线程保存数据
     */
    public class run implements Runnable {
        String url;

        public run(String str) {
            this.url = str;
        }

        @Override
        public void run() {
            try {
                File file = Glide.with(DyWebActivity.this).asFile().load(url).submit().get();
                displayToGallery(DyWebActivity.this, file);
            } catch (Exception e) {
                handler.sendEmptyMessage(Config.fail);
                e.printStackTrace();

            }
        }
    }


    public class renrqcode implements Runnable {
        private final String url;

        public renrqcode(String str) {
            this.url = str;
        }

        @Override
        public void run() {
            try {
                //1获取返回的 Bitmap
                Bitmap myBitmap = Glide.with(getBaseContext()).asBitmap().load(url).submit().get();

                //判断是否为二维码
                Result result = parsePic(myBitmap);
                if (result == null) {
                    //非二维码
                    doaloggetqrcode(url, 0);
                } else {
                    //二维码
                    doaloggetqrcode(result.getText(), 1);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(Config.fail);
                e.printStackTrace();

            }
        }
    }


    public class Handlers extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Config.sussess:
                    Toashow.toastMessage(getString(R.string.serview));
                    break;
                case Config.fail:
                    Toashow.toastMessage(getString(R.string.severeorr));
                    break;
                case QRReader:
                    break;
            }
        }
    }


    /**
     * 解析二维码图片
     * 判断是否为二维码
     *
     * @return
     */
    public Result parsePic(Bitmap bitmap) {
        //解析转换类型UTF-8
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] data = new int[width * height];
        bitmap.getPixels(data, 0, width, 0, 0, width, height);
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(width, height, data);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 打开二维码地址
     *
     * @param path
     */
    public void doaloggetqrcode(String path, int TYPE) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(DyWebActivity.this);
                String[] Title = TYPE == 0 ? new String[]{getString(R.string.tv_msg218)} : new String[]{getString(R.string.tv_msg218), getString(R.string.tv_msg219)};

                if (TYPE == 0) {
                    builder.setTitle(getString(R.string.alertDialog_title));
                    builder.setMessage(getString(R.string.tv_msg218));
                    builder.setPositiveButton(getString(R.string.btn_ok), (dialogInterface, i) -> sendbutton(path));
                    builder.setNegativeButton(getString(R.string.btn_cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                }

                builder.setItems(Title, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                //联网操作需要开启子线程
                                new Thread(new run(path)).start();
                                break;
                            case 1:
                                startActivity(new Intent(getBaseContext(), DyWebActivity.class).putExtra(Constants.VIDEOURL, path));
                                break;

                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });

    }

    private void sendbutton(String path) {
        new Thread(new run(path)).start();
    }

    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.destroy();
            webView.removeAllViews();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 调用系统浏览器下载文件
     */
    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    };

    /**
     * 将cookie同步到WebView
     *
     * @param url
     * @param cookies
     */
    public void syncCookie(String url, String cookies) {
        Log.d(TAG, "syncCookie: " + cookies);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //cookieManager.removeAllCookie(); //删除所有cookie 重新设置
        String[] split = cookies.split(";");
        for (String cookie : split) {
            Log.d(TAG, "syncCookie_for: " + cookie.trim());
            cookieManager.setCookie(url, cookie.trim());
        }
        CookieSyncManager.getInstance().sync();
    }


}
