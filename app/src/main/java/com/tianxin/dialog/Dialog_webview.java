package com.tianxin.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tianxin.R;

public class Dialog_webview extends Dialog {
    public Dialog_webview(Context context) {
        //super(context, R.style.dialog_animation);
        //super(context, R.style.fei_style_dialog);
        super(context, R.style.setting_dialog);
        //super(context, R.style.setting_dialog_button);
        //super(context, R.style.setting_dialog_iv);
        setContentView(R.layout.dialog_webview);

        WebView webView = findViewById(R.id.webview);
        inidatesetwebView(webView);
        webView.loadUrl("http://www.baidu.com");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                resend.sendToTarget();
            }

            public void onPageFinished(WebView paramWebView, String paramString) {
                if (!paramWebView.getSettings().getLoadsImagesAutomatically())
                    paramWebView.getSettings().setLoadsImagesAutomatically(true);
                super.onPageFinished(paramWebView, paramString);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            public boolean shouldOverrideUrlLoading(WebView paramWebView, String paramString) {
                paramWebView.loadUrl(paramString);
                return true;
            }

        });

    }

    @SuppressLint({"NewApi"})
    private void inidatesetwebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//设置 缓存模式
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setHorizontalScrollBarEnabled(false);

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setLoadsImagesAutomatically(true);
            while (true) {
                if (Build.VERSION.SDK_INT >= 21) {
                    webView.getSettings().setMixedContentMode(0);
                    return;
                } else {
                    webView.getSettings().setLoadsImagesAutomatically(false);
                }

            }
        }
    }

}
