/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/30 0030
 */


package com.tencent.qcloud.tim.uikit.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.tencent.qcloud.tim.tuikit.live.utils.GildeRoundTransform;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

public class Glideloads {

    public static void clear(ImageView imageView) {
        Glide.with(TUIKit.getAppContext()).clear(imageView);
    }

    public static void loadImage(ImageView imageView, String uri, int radius) {
        RequestOptions getoptions = getoptions(radius);
        Glide.with(TUIKit.getAppContext()).load(uri).apply(getoptions).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri, int radius) {
        RequestOptions getoptions = getoptions(radius);
        Glide.with(TUIKit.getAppContext()).load(uri).apply(getoptions).into(imageView);
    }

    public static void loadImage(ImageView imageView, String uri) {

        Glide.with(TUIKit.getAppContext()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri) {
        Glide.with(TUIKit.getAppContext()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, Bitmap uri) {
        Glide.with(TUIKit.getAppContext()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, Drawable uri) {
        Glide.with(TUIKit.getAppContext()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, String uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.drawable.ic_avatar).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(TUIKit.getAppContext()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, Bitmap uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.drawable.ic_avatar).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(TUIKit.getAppContext()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, Drawable uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.drawable.ic_avatar).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(TUIKit.getAppContext()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.drawable.ic_avatar).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(TUIKit.getAppContext()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView,String uri,int radius, int sampling){
        Glide.with(TUIKit.getAppContext()).load(uri).apply(bitmapTransform(new BlurTransformation(radius, sampling))).dontAnimate().into(imageView);
        //Glide.with(TUIKit.getAppContext()).load(uri).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(imageView);
    }

    private static RequestOptions getoptions(int radius) {
        //使用Glide改变图片的圆角
        RequestOptions options = new RequestOptions().centerCrop()
                //.placeholder(R.drawable.ic_avatar) //预加载图片
                //.error(R.drawable.ic_avatar) //加载失败图片
                //.priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform(radius)); //圆角
        return options;
    }


}
