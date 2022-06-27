/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/26 0026
 */


package com.tianxin.BasActivity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.tianxin.R;

import butterknife.ButterKnife;

public abstract class dialogBase extends Dialog {

    public dialogBase(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(itemview());
        ButterKnife.bind(this);
        inidate();
    }

    public abstract int itemview();

    public abstract void inidate();

    public abstract void OnClick(View v);



}
