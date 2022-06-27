/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/31 0031
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.tianxin.getHandler.Webrowse;
import com.tianxin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_Msg extends Dialog {
    @BindView(R.id.webView)
    WebView webView;
    String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public static void dialogmsg(Context context) {
        dialog_Msg dialog_msg = new dialog_Msg(context);
        dialog_msg.show();
    }

    public static void dialogmsg(Context context, String url) {
        dialog_Msg dialog_msg = new dialog_Msg(context, url);
        dialog_msg.show();
    }


    public dialog_Msg(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.item_money_tvmsg);
        ButterKnife.bind(this);
        iniview();
    }


    public dialog_Msg(@NonNull Context context, String url) {
        super(context, R.style.fei_style_dialog);
        this.url = url;
        setContentView(R.layout.item_money_tvmsg);
        ButterKnife.bind(this);
        iniview();
    }

    private void iniview() {
        if (TextUtils.isEmpty(url)) {
            webView.loadUrl(Webrowse.webview);
        } else {
            webView.loadUrl(url);
        }

        webView.setWebViewClient(new webviewClient());
        webView.setWebChromeClient(new myWebChromeClient());
    }

    @OnClick({R.id.send})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send:
                dismiss();
                break;
        }
    }

    private class webviewClient extends WebViewClient {

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            resend.sendToTarget();
        }

        public void onPageFinished(WebView view, String paramString) {
            if (!view.getSettings().getLoadsImagesAutomatically())
                view.getSettings().setLoadsImagesAutomatically(true);
            super.onPageFinished(view, paramString);

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
    }

    private class myWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }


}
