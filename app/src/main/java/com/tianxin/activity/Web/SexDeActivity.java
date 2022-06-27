package com.tianxin.activity.Web;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.dialog.Dialog_Loading;

import butterknife.BindView;

public class SexDeActivity extends BasActivity2 {
    private WebView webView;
    private Dialog_Loading dialog;
    @BindView(R.id.img_back)
    public ImageView img_back;
    private handlers handler = new handlers();

    @Override
    protected int getview() {
        return R.layout.activity_baseweb;
    }

    @Override
    public void iniview() {
        String url = getIntent().getStringExtra("url");
        webView = findViewById(R.id.webview);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new webviewClient());
        agetSettings(webView);
        webView.loadUrl(url);
        dialog = Dialog_Loading.dialogLoading(this, "正在获取信息...");
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {
        finish();
    }

    @Override
    public void OnEorr() {

    }

    private void agetSettings(WebView paramWebView) {

        paramWebView.getSettings().setJavaScriptEnabled(true);
        paramWebView.getSettings().setAllowFileAccess(true);
        paramWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        paramWebView.getSettings().setSupportZoom(true);
        paramWebView.getSettings().setAppCacheEnabled(true);
        paramWebView.getSettings().setDomStorageEnabled(true);
        paramWebView.getSettings().setLoadWithOverviewMode(true);
        paramWebView.getSettings().setUseWideViewPort(true);
        paramWebView.getSettings().setBlockNetworkImage(false);
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
                    paramWebView.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        }
    }

    class handlers extends Handler {
        public void handleMessage(Message paramMessage) {
            webView.loadUrl("javascript:function setTop(){document.querySelector('div.art-input').style.display=\"none\";}setTop();");
            webView.loadUrl("javascript:function setTopA(){document.querySelector('div.state').style.display=\"none\";}setTopA();");
            webView.loadUrl("javascript:function setTopB(){document.querySelector('div.sharing').style.display=\"none\";}setTopB();");
            webView.loadUrl("javascript:function setTopC(){document.querySelector('div.mall').style.display=\"none\";}setTopC();");
            webView.loadUrl("javascript:function setTopD(){document.querySelector('div.artGoods').style.display=\"none\";}setTopD();");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            webView.setVisibility(View.VISIBLE);
        }
    }

    class webviewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            handler.sendEmptyMessage(1);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }


        public void onReceivedSslError(WebView view, SslErrorHandler paramSslErrorHandler, SslError paramSslError) {
            super.onReceivedSslError(view, paramSslErrorHandler, paramSslError);
            paramSslErrorHandler.cancel();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            if (url.startsWith("xys://") || url.startsWith("http://xys/")) {
                return true; //拦截
            } else {
                webView.loadUrl(url); //正常访问
                return false;
            }

        }
    }

    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if ((webView.canGoBack()) && (paramKeyEvent.getKeyCode() == 4)) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }


}
