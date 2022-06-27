/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/18 0018
 */


package com.tianxin.BasActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.Module.ControlPanel;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Paymnets;
import com.tencent.liteav.TUIVoiceRoomLogin;
import com.tencent.liteav.login.UserModel;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.io.File;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public abstract class BasActivity extends AppCompatActivity {
    public Unbinder bind;
    public String getdata;
    public Activity activity;
    public Context context;
    public UserInfo userInfo;
    public Allcharge allcharge;
    public UserModel mSelfModel;    //表示当前用户的UserModel
    public UserModel mSearchModel;  //表示当前对方的usermodel
    public com.tencent.opensource.model.info info;
    public com.tencent.opensource.model.member member;
    public Datamodule datamodule;
    public TUIVoiceRoomLogin tuiVoiceRoomLogin;
    public int TYPE;
    public ControlPanel controlPanel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getview());
        bind = ButterKnife.bind(this);
        context = this;
        activity = this;
        userInfo = UserInfo.getInstance();
        allcharge = Allcharge.getInstance();
        datamodule = new Datamodule(context);

        iniview();
        initData();
    }

    protected abstract int getview();

    public abstract void iniview();

    public abstract void initData();

    public abstract void OnClick(View v);

    public abstract void OnEorr();

    public String getGetdata() {
        return getdata;
    }

    public void setGetdata(String getdata) {
        this.getdata = getdata;
    }

    public void Glideload(ImageView imags, String path) {
        Glide.with(DemoApplication.instance()).load(path).into(imags);
    }

    public void Glideload(ImageView imags, int path) {
        Glide.with(DemoApplication.instance()).load(path).into(imags);
    }

    public void Glideload(ImageView imags, int path, ImageView img) {
        Glide.with(DemoApplication.instance()).load(path).into(img);
    }

    public void Glideload(ImageView imags, String path, ImageView img) {
        Glide.with(DemoApplication.instance()).load(path).into(img);
    }

    public void Glideload(ImageView imags, int path, int radius, int sampling) {
        Glide.with(DemoApplication.instance()).load(path).apply(bitmapTransform(new BlurTransformation(radius, sampling))).into(imags);
    }

    public void Glideload(ImageView imags, String path, int radius, int sampling) {
        Glide.with(DemoApplication.instance()).load(path).apply(bitmapTransform(new BlurTransformation(radius, sampling))).into(imags);
    }

    public void Glideload(ImageView imags, Bitmap path) {
        Glide.with(DemoApplication.instance()).load(path).into(imags);
    }

    public void Glideload(ImageView imags, Drawable path) {
        Glide.with(DemoApplication.instance()).load(path).into(imags);
    }

    public void Glideload(ImageView imags, Uri path) {
        Glide.with(DemoApplication.instance()).load(path).into(imags);
    }

    public void Glideload(ImageView imags, File file) {
        Glide.with(DemoApplication.instance()).load(file).into(imags);
    }

    public void Glideload(ImageView imags, URL url) {

        Glide.with(DemoApplication.instance()).load(url).into(imags);
    }

    public void Glideload(ImageView imags, byte[] model) {
        Glide.with(DemoApplication.instance()).load(model).into(imags);
    }

    public void Glideload(ImageView imags, Object model) {
        Glide.with(DemoApplication.instance()).load(model).into(imags);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }

    public void tostartActivity(Class<?> cls, int TYPE) {
        Intent intent = new Intent(context, cls);
        intent.putExtra(Constants.TYPE, TYPE);
        startActivity(intent);
    }

    public Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void ToKen(String msg) {
            Toashow.show(getString(R.string.tv_msg1933));
            BaseActivity.logout(DemoApplication.instance());
        }


    };

}
