/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/24 0024
 */


package com.tencent.qcloud.costransferpractice.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.tencent.qcloud.costransferpractice.common.base.BaseActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 压缩图片工具类
 */
public class Imagecompressiontool {

    /**
     * 质量压缩
     * 设置bitmap options属性，降低图片的质量，像素不会减少
     * 第一个参数为需要压缩的bitmap图片对象，第二个参数为压缩后图片保存的位置
     * 设置options 属性0-100，来实现压缩（因为png是无损压缩，所以该属性对png是无效的）
     * 使用场景：将图片压缩后将图片上传到服务器，或者保存到本地，根据实际需求
     *
     * @param bmp
     * @param file
     */
    public static Bitmap qualityCompress(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int quality = 20;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 直接显示不压缩图片
     * @param bmp
     * @param file
     * @return
     */
    public static Bitmap oNqualityCompress(Bitmap bmp, File file) {
        // 0-100 100为不压缩
        int quality = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    /**
     * 采样率压缩
     *
     * @param inSampleSize 可以根据需求计算出合理的inSampleSize
     */
    public static Bitmap qualityCompress(File file, File newfile, int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Bitmap resultBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultBitmap;

    }

    /**
     *尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
     * 使用场景：缓存缩略图的时候（头像处理）
     * 通过减少图片的像素来降低图片的磁盘空间大小和内存大小，可以用于缓存缩略图
     *
     * @param
     */
    public static Bitmap qualityCompress(File file, File newfile) {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        //设置缩放比 尺寸压缩倍数,值越大，图片尺寸越小
        int radio = 10;
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / radio, bitmap.getHeight() / radio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        RectF rectF = new RectF(0, 0, bitmap.getWidth() / radio, bitmap.getHeight() / radio);
        //将原图画在缩放之后的矩形上
        canvas.drawBitmap(bitmap, null, rectF, null);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        try {
            FileOutputStream fos = new FileOutputStream(newfile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     尺寸压缩（通过缩放图片像素来减少图片占用内存大小）
     * 使用场景：缓存缩略图的时候（头像处理）
     * 通过减少图片的像素来降低图片的磁盘空间大小和内存大小，可以用于缓存缩略图
     */
    public static Bitmap sizeCompress(Bitmap bmp, File file) {
        // 尺寸压缩倍数,值越大，图片尺寸越小
        int ratio = 8;
        // 压缩Bitmap到对应尺寸
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 可以对一些没有必要原图展示的图片进行压缩
     *
     * @param imageUri
     * @param activity
     */
    public static void bitmapFactory(Uri imageUri, Activity activity) {
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(imageUri, filePathColumns, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePathColumns[0]);
        String imagePath = c.getString(columnIndex);
        c.close();
        // 配置压缩的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        //inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = caculateSampleSize(options, 500, 50);
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
    }

    /**
     * 计算出所需要压缩的大小
     *
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private static int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    /**
     * 这里我们生成了一个Pic文件夹，在下面放了我们质量压缩后的图片，用于和原图对比
     * 压缩图片使用Bitmap.compress()，这里是质量压缩
     */
    public static Bitmap bitmapCompress(Uri uriClipUri, File file, Bitmap photoBitmap, Activity activity) {
        try {
            //裁剪后的图像转成BitMap
            //photoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriClipUri));
            photoBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uriClipUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //创建路径
        String path = Environment.getExternalStorageDirectory().getPath() + "/Pic";
        //获取外部储存目录
        file = new File(path);
        //创建新目录, 创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。
        file.mkdirs();
        //以当前时间重新命名文件
        long i = System.currentTimeMillis();
        //生成新的文件
        file = new File(file.toString() + "/" + i + ".png");
        Log.e("fileNew", file.getPath());
        //创建输出流
        OutputStream out = null;
        try {
            out = new FileOutputStream(file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //压缩文件，返回结果，参数分别是压缩的格式，压缩质量的百分比，输出流
        boolean bCompress = photoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

        try {
            photoBitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoBitmap;
    }


    /**
     * 第一：我们先看下质量压缩方法
     */
    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法。这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环推断假设压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都降低10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 第二：图片按比例大小压缩方法（依据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    private static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //開始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //如今主流手机比較多是800*480分辨率。所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。因为是固定比例缩放，仅仅用高或者宽当中一个数据进行计算就可以
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//假设宽度大的话依据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//假设高度高的话依据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //又一次读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 第三：图片按比例大小压缩方法（依据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    private static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//推断假设图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //開始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //如今主流手机比較多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。因为是固定比例缩放。仅仅用高或者宽当中一个数据进行计算就可以
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//假设宽度大的话依据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//假设高度高的话依据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //又一次读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }


    /**
     * 本地手机目录路径
     */
    public static File dataDir() {
        //以当前时间重新命名文件
        long name = System.currentTimeMillis();
        String filename = String.format("%s.png", name);
        //创建自定义路径目录
        String path = Environment.getExternalStorageDirectory().getPath() + "/paixide";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            path = CacheUtil.getCacheDirectory(BaseActivity.mcontext, null).getAbsolutePath();
        }

        //获取外部储存目录
        File dir = new File(path);
        if (!dir.exists()) {
            //创建新目录, 创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。
            dir.mkdirs();
        }
        //创建了文件
        File dataDir = new File(path, filename);
        return dataDir;
    }

  /**
     * 本地手机目录路径
     */
    public static File dataDir(String name) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/paixide";  //创建自定义路径目录
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            path = CacheUtil.getCacheDirectory(BaseActivity.mcontext, null).getAbsolutePath();
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dataDir = new File(path, name);
        return dataDir;
    }

}