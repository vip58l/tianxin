package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.tianxin.R;

public class Dialog_Loading extends Dialog {
    private TextView loadingtext;

    public Dialog_Loading(Context context, String paramString) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.loading_dialog_view);
        loadingtext = findViewById(R.id.loading_text);
        if (!TextUtils.isEmpty(paramString)) {
            loadingtext.setText(paramString);
        }
    }

    public Dialog_Loading(Context context) {
        super(context, R.style.MyDialogStyle);
        setContentView(R.layout.loading_dialog_view);
        loadingtext = findViewById(R.id.loading_text);

    }

    public void setLoadingtext(String msg) {
        loadingtext.setText(msg);
    }

    public static Dialog_Loading dialogLoading(Context context, String msg) {
        Dialog_Loading dialogLoading = new Dialog_Loading(context, msg);
        dialogLoading.setCanceledOnTouchOutside(false);
        dialogLoading.show();
        return dialogLoading;
    }

    public static Dialog_Loading dialogLoading(Context context) {
        Dialog_Loading dialogLoading = new Dialog_Loading(context);
        dialogLoading.setCanceledOnTouchOutside(false);
        dialogLoading.show();
        return dialogLoading;
    }

    /**
     * view自定义动画转动
     * Duration转到的时间
     * 360度转动
     */
    public void loadview(View view, long Duration) {
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setRepeatCount(Animation.INFINITE);
        ra.setDuration(Duration);
        view.startAnimation(ra);
    }

}

