/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/30 0030
 */


package com.tianxin.Util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.tianxin.R;
import com.tianxin.Util.glide.GlideRoundTransform;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.tuikit.live.utils.GildeRoundTransform;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import java.util.concurrent.ExecutionException;

public class Glideload {


    public static void clear(ImageView imageView) {
        Glide.with(DemoApplication.instance()).clear(imageView);
    }

    public static void loadImage(ImageView imageView, String uri, int radius) {
        RequestOptions getoptions = getoptions(radius);
        Glide.with(DemoApplication.instance()).load(uri).apply(getoptions).dontAnimate().into(imageView);

    }

    public static void loadImage(ImageView imageView, Bitmap bitmap, int radius) {
        Glide.with(DemoApplication.instance()).load(bitmap).transform(new CenterCrop(), new GlideRoundTransform(radius)).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri, int radius) {
        RequestOptions getoptions = getoptions(radius);
        Glide.with(DemoApplication.instance()).load(uri).apply(getoptions).into(imageView);
    }

    public static void loadImage(ImageView imageView, String uri) {
        Glide.with(DemoApplication.instance()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri) {
        Glide.with(DemoApplication.instance()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, Bitmap uri) {
        Glide.with(DemoApplication.instance()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, Drawable uri) {
        Glide.with(DemoApplication.instance()).load(uri).into(imageView);
    }

    public static void loadImage(ImageView imageView, String uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(DemoApplication.instance()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, Bitmap uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(DemoApplication.instance()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, Drawable uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(DemoApplication.instance()).load(uri).apply(options).into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri, int roundingRadius, boolean b) {
        RequestOptions options = new RequestOptions().error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).bitmapTransform(new RoundedCorners(roundingRadius));//图片圆角为30
        Glide.with(DemoApplication.instance()).load(uri).apply(options).into(imageView);
    }

    /**
     * 直角图片模糊
     *
     * @param imageView
     * @param uri
     * @param radius
     * @param sampling
     */
    public static void loadImage(ImageView imageView, String uri, int radius, int sampling) {
        BlurTransformation blurTransformation = new BlurTransformation(radius, sampling);
        RequestOptions transform = bitmapTransform(blurTransformation);//模糊
        RequestOptions transform2 = bitmapTransform(blurTransformation).transform(new GildeRoundTransform(radius)); //园角
        Glide.with(DemoApplication.instance()).load(uri).error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).apply(transform).dontAnimate().into(imageView);

        //Glide.with(DemoApplication.instance()).load(uri).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(imageView);
    }

    /**
     * 圆角图片模糊
     *
     * @param imageView
     * @param uri
     * @param radius
     * @param radius2
     * @param sampling
     */
    public static void loadImage(ImageView imageView, String uri, int radius, int radius2, int sampling) {

        BlurTransformation blurTransformation = new BlurTransformation(radius2, sampling);
        RoundedCornersTransformation roundedCornersTransformation = new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL);
        MultiTransformation multi = new MultiTransformation(blurTransformation, roundedCornersTransformation);
        RequestOptions requestOptions = bitmapTransform(multi);

        Glide.with(DemoApplication.instance())
                .load(uri).error(R.mipmap.cover_defult)
                .placeholder(R.mipmap.cover_defult)
                .apply(requestOptions)
                .dontAnimate()
                .into(imageView);

        //Glide.with(DemoApplication.instance()).load(uri).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(imageView);
    }

    public static void loadImage(ImageView imageView, int uri, int radius, int sampling) {
        RequestOptions requestOptions = bitmapTransform(new BlurTransformation(radius, sampling));
        Glide.with(DemoApplication.instance()).load(uri).error(R.mipmap.cover_defult).placeholder(R.mipmap.cover_defult).apply(requestOptions).dontAnimate().into(imageView);
        //Glide.with(DemoApplication.instance()).load(uri).apply(bitmapTransform(new BlurTransformation(5, 25))).dontAnimate().into(imageView);
    }

    /**
     * 使用Glide改变图片的圆角
     *
     * @param radius
     * @return
     */
    public static RequestOptions getoptions(int radius) {
        RequestOptions options = new RequestOptions().centerCrop()
                //.placeholder(R.mipmap.ic_launcher) //预加载图片
                //.error(R.mipmap.ic_launcher) //加载失败图片
                //.priority(Priority.HIGH) //优先级
                //.diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform(radius)); //圆角
        return options;
    }

    /**
     * 使用Glide改变图片的圆角
     *
     * @param radius
     * @return
     */
    public static RequestOptions getoptions(int radius, int top, int bottom) {
        RequestOptions options = new RequestOptions().centerCrop()
                //.placeholder(R.mipmap.ic_launcher) //预加载图片
                //.error(R.mipmap.ic_launcher) //加载失败图片
                //.priority(Priority.HIGH) //优先级
                //.diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform(radius)); //圆角
        return options;
    }

    /**
     * @param url 通过URL得到 Drawable
     * @return
     */
    public static void getDrawableGlide(String url, CustomTarget<Drawable> customTarget) {
        Glide.with(DemoApplication.instance()).load(url).into(customTarget);
    }

    /**
     * @param url 通过URL得到 Bitmap
     * @return
     */
    public static void getBitmapGlide(String url, CustomTarget<Bitmap> customTarget) {
        Glide.with(DemoApplication.instance()).asBitmap().load(url).into(customTarget);
    }

    /**
     * 这是一个耗时的操作需要异步处理
     *
     * @param url 通过URL得到 Drawable
     * @param
     * @return
     */
    public static Drawable getDrawableGlide(String url) {

        try {
            return Glide.with(DemoApplication.instance())
                    .load(url)
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这是一个耗时的操作需要异步处理
     *
     * @param url 通过URL得到 Bitmap
     * @return
     */
    public static Bitmap getBitmapGlide(String url) {

        try {
            return Glide.with(DemoApplication.instance())
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }

}
