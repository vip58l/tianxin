package com.tianxin.activity.Web;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.tianxin.R;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;

import androidx.annotation.Nullable;

/**
 * 浏览器页
 */
public class BookWebActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.white));
        webView = new WebView(this);
        setContentView(webView);
        setSettings(webView);
        String path = getIntent().getStringExtra("bookUrl");
        webView.loadUrl(path);
        webView.setWebViewClient(webViewClient);
    }

    private void setSettings(WebView paramWebView) {
        paramWebView.getSettings().setJavaScriptEnabled(true);
        paramWebView.getSettings().setAllowFileAccess(true);
        paramWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        paramWebView.getSettings().setSupportZoom(true);
        paramWebView.getSettings().setAppCacheEnabled(true);
        paramWebView.getSettings().setDomStorageEnabled(true);
        paramWebView.getSettings().setLoadWithOverviewMode(true);
        paramWebView.getSettings().setUseWideViewPort(true);
        paramWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paramWebView.getSettings().setBuiltInZoomControls(false);
        paramWebView.setHorizontalScrollBarEnabled(false);
        if (Build.VERSION.SDK_INT >= 19) {
            paramWebView.getSettings().setLoadsImagesAutomatically(true);
            while (true) {
                if (Build.VERSION.SDK_INT >= 21) {
                    paramWebView.getSettings().setMixedContentMode(0);
                    return;
                } else {
                    paramWebView.getSettings().setLoadsImagesAutomatically(false);
                }
            }
        }

    }

    protected void onDestroy() {
        if (webView == null)
            webView.destroy();
        super.onDestroy();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (paramInt == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            finish();
            return false;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    private final WebViewClient webViewClient=new  WebViewClient() {

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
        }

        public void onPageFinished(WebView paramWebView, String paramString) {
            if (!paramWebView.getSettings().getLoadsImagesAutomatically()) {
                paramWebView.getSettings().setLoadsImagesAutomatically(true);
            }
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

        public void onReceivedSslError(WebView paramWebView, SslErrorHandler paramSslErrorHandler, SslError paramSslError) {
            super.onReceivedSslError(paramWebView, paramSslErrorHandler, paramSslError);
            paramSslErrorHandler.cancel();
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if (url.startsWith("alipays://")) {
                Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                startActivity(localIntent);
                return true;
            }

            if (url.startsWith("baiduboxapp://") || url.startsWith("baiduboxlite://") || url.startsWith("https://boxer")) {
                Toashow.show(BookWebActivity.this, "限制访问");
                return true;
            }
            view.loadUrl(url);
            return false;

        }
    };
}
