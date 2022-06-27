package com.tianxin.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tianxin.Module.api.Share;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.dialog_Item_icon;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.opensource.model.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 推荐分享
 */
public class Dialog_fenxing extends BottomSheetDialog {
    private Share share;
    private String paramStr;
    @BindView(R.id.delete)
    dialog_Item_icon delete;
    @BindView(R.id.layout5)
    dialog_Item_icon layout5;
    private Paymnets paymnets;
    public static Dialog_fenxing dialogFenxing;

    public static Dialog_fenxing getDialogFenxing(Context context) {
        if (dialogFenxing==null){
            dialogFenxing=new Dialog_fenxing(context);
        }
        return dialogFenxing;
    }

    public static void myshow(Context context, String paramStr) {
        Dialog_fenxing dialog_fenxing = new Dialog_fenxing(context);
        dialog_fenxing.setParamStr(paramStr);
        dialog_fenxing.show();
    }

    public static void myshow(Context context, String paramStr, Paymnets paymnets) {
        Dialog_fenxing dialog_fenxing = new Dialog_fenxing(context);
        dialog_fenxing.setParamStr(paramStr);
        dialog_fenxing.setPaymnets(paymnets);
        dialog_fenxing.show();
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
        if (this.share != null && share.getUserid().equals(UserInfo.getInstance().getUserId())) {
            delete.setVisibility(View.VISIBLE);
            layout5.setVisibility(View.GONE);
        }
    }

    public Dialog_fenxing(Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_fenxing);
        ButterKnife.bind(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public void setParamStr(String paramStr) {
        try {
            this.paramStr = paramStr;
            this.share = new Gson().fromJson(paramStr, Share.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClick({R.id.cancel_action, R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.layout5, R.id.delete})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_action:
                dismiss();
                break;
            case R.id.layout1:
            case R.id.layout2:
                dismiss();
                getmobileqqApi();
                break;
            case R.id.layout3:
                //分享到微信朋友
                if (share != null) {
                    WXpayObject.getsWXpayObject().sharewechat(share, SendMessageToWX.Req.WXSceneSession);
                } else {
                    getWechatApi();
                }
                dismiss();
                break;
            case R.id.layout4:
                //分享到朋友圈
                if (share != null) {
                    WXpayObject.getsWXpayObject().sharewechat(share, SendMessageToWX.Req.WXSceneTimeline);
                } else {
                    getWechatApi();
                }
                dismiss();
                break;
            case R.id.layout5:
                getweiboApi();
                break;
            case R.id.delete:
                dismiss();
                if (paymnets != null && share != null) {
                    paymnets.onSuccess();
                }
                break;
        }
    }

    public void getWechatApi() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toashow.show(getContext(), "检查到您手机没有安装微信，请安装后使用该功能");
        }
    }

    public void getmobileqqApi() {

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.SplashActivity");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toashow.show(getContext(), "检查到您手机没有安装QQ，请安装后使用该功能");
        }
    }

    public void getweiboApi() {

        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.sina.weibo", "com.sina.weibo.SplashActivity");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toashow.show(getContext(), "检查到您手机没有安装新浪微博，请安装后使用该功能");
        }
    }

    /**
     * 复制文本到粘贴板
     *
     * @param context
     * @param paramStr
     */
    public static void paramcopy(Context context, String paramStr) {
        ClipboardManager tvCopy = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", paramStr);
        tvCopy.setPrimaryClip(clipData);

    }

    public static void getWechatApi(Context context, String paramStr) {
        try {
            paramcopy(context, paramStr);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (Exception e) {
            Toashow.show("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }
}
