
package com.tianxin.activity.DouYing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.CrashUtils.init;

/**
 * 自定义Webview浏览器
 */
public class KWebView extends WebView {

    String TAG = KWebView.class.getName();

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private HtmlCallback htmlCallback;

    public KWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public KWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 传入监听对像回调内容
     *
     * @param htmlCallback
     */
    public void setHtmlCallback(HtmlCallback htmlCallback) {
        this.htmlCallback = htmlCallback;
    }

    private void init() {
        // Dom存储支持
        getSettings().setDomStorageEnabled(true);
        // 设置支持javascript
        getSettings().setJavaScriptEnabled(true);
        // 启动缓存
        getSettings().setAppCacheEnabled(true);
        // 设置缓存模式
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 允许混合模式（http与https）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        // 获取网页内容的Js调用
        addJavascriptInterface(new InJavaScriptLocalObj(this), "java_obj");

        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ;
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dispose();

                //定时执行快速上手和分析   每隔200毫秒执行一次逻辑代码 第二个，可以选择不同的时间类型，可以选秒，分钟，小时等
                // 定时轮询获取网页内容，直到获取到有效信息
                compositeDisposable.add(Observable.interval(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        //执行获取标签输出html
                        .subscribe(integer -> view.loadUrl("javascript:window.java_obj.getSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');")));

                super.onPageFinished(view, url);

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
    }

    /**
     * 取消订阅
     */
    public void dispose() {
        //compositeDisposable.dispose();
        compositeDisposable.clear();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //离开退出当前控件
        dispose();
    }

    /**
     * 逻辑处理
     *
     * @author linzewu
     */
    private class InJavaScriptLocalObj {

        public KWebView webView;

        public InJavaScriptLocalObj(KWebView webView) {
            this.webView = webView;
        }

        @JavascriptInterface
        //自定义的一个JS调用方法
        public void getSource(String html) {
            String url = (String) getTag();

            //抖音加载出了地址 获取内容是否包含指定字符串 如查没有继续查询
            if (!html.contains("playwm") && url.contains(activity_jsonvideo.douyin)) {
                Log.d(TAG, "抖音加载失败...");
                return;

            }

            //陌陌指定的字符或字符串返回 true，否则返回 false 如查没有继续查询
            if (!html.contains("my-video") && url.contains(activity_jsonvideo.immomo)) {
                Log.d(TAG, "陌陌加载失败...");
                return;
            }

            // 主线程运行取消
            ((Activity) webView.getContext()).runOnUiThread(() -> webView.dispose());

            // 回调返回html内容
            if (htmlCallback != null) {
                htmlCallback.onHtmlGet(html);
            }


        }
    }

    public interface HtmlCallback {
        void onHtmlGet(String html);
    }


}
