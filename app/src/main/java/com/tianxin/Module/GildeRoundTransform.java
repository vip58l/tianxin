package com.tianxin.Module;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.tianxin.R;

import java.security.MessageDigest;

/**
 * 使用Glide改变图片的圆角
 */
public class GildeRoundTransform extends BitmapTransformation {
    private static float radius = 0f;

    public GildeRoundTransform() {
        this(4);
    }

    public GildeRoundTransform(int dp) {
        super();
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        //变换的时候裁切
        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return roundCrop(pool, bitmap);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);

        //左上角、右上角圆角
//        RectF rectRound = new RectF(0f, 100f, source.getWidth(), source.getHeight());
//        canvas.drawRect(rectRound, paint);
        return result;
    }

    public static RequestOptions getoptions(){
        //使用Glide改变图片的圆角
        RequestOptions options = new RequestOptions()
                .centerCrop()
                // .placeholder(R.mipmap.ic_launcher) //预加载图片
                // .error(R.drawable.ic_launcher_foreground) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform()); //圆角

        return options;
    }

    public static RequestOptions getoptions(int dp){
        //使用Glide改变图片的圆角
        RequestOptions options = new RequestOptions()
                .centerCrop()
                // .placeholder(R.mipmap.ic_launcher) //预加载图片
                // .error(R.drawable.ic_launcher_foreground) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform(dp)); //圆角

        return options;
    }

    //使用说明
    private void Test_Glide_RequestOptions() {

        //使用Glide改变图片的圆角
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher) //预加载图片
                .error(R.mipmap.ic_launcher) //加载失败图片
                .priority(Priority.HIGH) //优先级
                .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
                .transform(new GildeRoundTransform(4)); //圆角
        // Glide.with(onter ).load(R.mipmap.a8f).apply(options).into( holder.pic );
    }


}
