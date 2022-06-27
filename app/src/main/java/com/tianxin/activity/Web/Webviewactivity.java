package com.tianxin.activity.Web;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Config;
import com.tianxin.widget.itembackTopbr;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import butterknife.BindView;

public class Webviewactivity extends BasActivity2 {
    private String TAG = Webviewactivity.class.getSimpleName();
    @BindView(R.id.dy_webview)
    WebView webView;
    @BindView(R.id.itemback)
    itembackTopbr itemback;

    public static void setAction(Context context, String url) {
        Intent intent = new Intent();
        intent.setClass(context, Webviewactivity.class);
        intent.putExtra(Constants.VIDEOURL, url);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        StatusBarUtil.setNavigationBarColor(activity);
        return R.layout.itemwebview;
    }

    @Override
    public void iniview() {
        videoUrl = getIntent().getStringExtra(Constants.VIDEOURL);
        JSON = getIntent().getStringExtra(Constants.JSON);
        itemback.setTRANSPARENT();
        webView.loadUrl(videoUrl);
        basestartActivity.WebSettings(webView);
        webView.setWebViewClient(new webviewClient());
        webView.setWebChromeClient(new myWebChromeClient());
        webView.setVisibility(View.INVISIBLE);
        webView.setOnLongClickListener(new setOnLongClickListener());
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

                //调用系统浏览器下载文件
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                //下载文件
                //if (url.endsWith(".jpg") || url.endsWith(".png") || url.endsWith(".gif") || url.endsWith(".apk") || url.endsWith(".mp4") || url.endsWith(".mp3"))
                //new mydowon(url).start();

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    private class webviewClient extends WebViewClient {

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
            Log.d(TAG, "onFormResubmission: ");
        }

        public void onPageFinished(WebView view, String paramString) {
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                //设置网页加载完成再显示图片
                try {
                    view.getSettings().setLoadsImagesAutomatically(true);
                    webView.getSettings().setBlockNetworkImage(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onPageFinished(view, paramString);
            webView.setVisibility(View.VISIBLE);
            //加载完成执行JS代码
            //view.loadUrl("javascript:function setTop(){document.querySelector('div.appbanner').style.display=\"none\";}setTop();");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            webView.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            webView.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            //打开网页时不调用系统浏览器
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
     *
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
                    Toast.makeText(context, "超链接", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                File file = Glide.with(activity).asFile().load(url).submit().get();
                displayToGallery(context, file);
            } catch (Exception e) {
                handler.sendEmptyMessage(Config.fail);
                e.printStackTrace();

            }
        }
    }

    /**
     * 将图片保存在相册
     *
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

    private void sendbutton(String path) {
        new Thread(new run(path)).start();
    }
}
