package com.steven.selectimage.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.steven.selectimage.R;
import com.steven.selectimage.model.Image;
import com.steven.selectimage.model.ImageFolder;
import com.steven.selectimage.ui.adapter.ImageAdapter;
import com.steven.selectimage.ui.adapter.ImageFolderAdapter;
import com.steven.selectimage.utils.StatusBarUtil;
import com.steven.selectimage.utils.TDevice;
import com.steven.selectimage.widget.ImageFolderView;
import com.steven.selectimage.widget.recyclerview.MultiTypeSupport;
import com.steven.selectimage.widget.recyclerview.SpaceGridItemDecoration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectImageActivity extends BaseActivity implements ImageFolderView.ImageFolderViewListener, ImageAdapter.onCameraClickListener {
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";
    public static final int MAX_SIZE = 9;
    private static final int PERMISSION_REQUEST_CODE = 88;
    private static final int TAKE_PHOTO = 99;
    TextView mback;
    TextView mTvSelectCount;
    RecyclerView mRvImage;
    TextView mTvPhoto;
    TextView mTvPreview;
    ImageFolderView mImageFolderView;
    private final boolean mHasCamera = true;
    //被选中图片的集合
    private List<Image> mSelectedImages = new ArrayList<>();
    private final List<Image> mImages = new ArrayList<>();
    private final List<ImageFolder> mImageFolders = new ArrayList<>();
    private ImageAdapter mImageAdapter;
    private ImageFolderAdapter mImageFolderAdapter;
    private Uri mImageUri;
    private File takePhotoImageFile;

    public static void setAction(Activity activity, List<Image> mSelectImages, int SELECT_IMAGE_REQUEST) {
        Intent intent = new Intent(activity, SelectImageActivity.class);
        intent.putParcelableArrayListExtra("selected_images", (ArrayList<? extends Parcelable>) mSelectImages);
        activity.startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_image;
    }

    @Override
    protected void init() {
        mRvImage = findViewById(R.id.rv);
        mback = findViewById(R.id.tv_back);
        mTvSelectCount = findViewById(R.id.tv_ok);
        mTvPhoto = findViewById(R.id.tv_photo);
        mTvPreview = findViewById(R.id.tv_preview);
        mImageFolderView = findViewById(R.id.image_folder_view);

        mback.setOnClickListener(v -> finish());             //返回
        mTvSelectCount.setOnClickListener(v -> RESULT_OK()); //完成
        mTvPhoto.setOnClickListener(v -> tvphoto());         //选择相册
        mTvPreview.setOnClickListener(v -> tvpreview());     //预览

        //设置状态栏的颜色
        StatusBarUtil.statusBarTintColor(this, ContextCompat.getColor(this, R.color.color_black));
        setupSelectedImages();
        mRvImage.setLayoutManager(new GridLayoutManager(this, 3));
        mRvImage.addItemDecoration(new SpaceGridItemDecoration((int) TDevice.dipToPx(getResources(), 1))); //分割线
        getSupportLoaderManager().initLoader(0, null, mLoaderCallbacks);//异步加载图片
        mImageFolderView.setListener(this);


    }

    private void setupSelectedImages() {
        Intent intent = getIntent();
        ArrayList<Image> selectImages = intent.getParcelableArrayListExtra("selected_images");
        mSelectedImages.addAll(selectImages);

        if (mSelectedImages.size() > 0 && mSelectedImages.size() <= MAX_SIZE) {
            mTvPreview.setClickable(true);
            mTvPreview.setText(String.format("预览(%d/9) ", mSelectedImages.size()));
            mTvPreview.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        }


    }


    /**
     * 完成返回
     */
    public void RESULT_OK() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(EXTRA_RESULT, (ArrayList<? extends Parcelable>) mSelectedImages);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 选择相册
     */
    public void tvphoto() {
        if (mImageFolderView.isShowing()) {
            mImageFolderView.hide();
        } else {
            mImageFolderView.show();
        }
    }

    /**
     * 预览
     */
    public void tvpreview() {
        Intent previewIntent = new Intent(this, PreviewImageActivity.class);
        previewIntent.putParcelableArrayListExtra("preview_images", (ArrayList<? extends Parcelable>) mSelectedImages);
        startActivity(previewIntent);
    }

    private void addImageFoldersToAdapter() {
        if (mImageFolderAdapter == null) {
            mImageFolderAdapter = new ImageFolderAdapter(this, mImageFolders, R.layout.item_list_folder);
            mImageFolderView.setAdapter(mImageFolderAdapter);
        } else {
            mImageFolderAdapter.notifyDataSetChanged();
        }
    }

    private void addImagesToAdapter(ArrayList<Image> images) {
        mImages.clear();
        mImages.addAll(images);
        if (mImageAdapter == null) {
            mImageAdapter = new ImageAdapter(this, mImages, mSelectedImages, mMultiTypeSupport);
            mRvImage.setAdapter(mImageAdapter);
        } else {
            mImageAdapter.notifyDataSetChanged();
        }
        mImageAdapter.setSelectImageCountListener(mOnSelectImageCountListener);
        mImageAdapter.setOnCameraClickListener(this);
    }

    private final MultiTypeSupport<Image> mMultiTypeSupport = image -> {
        if (TextUtils.isEmpty(image.getPath())) {
            return R.layout.item_list_camera;
        }
        return R.layout.item_list_image;
    };
    /*************************************已选择的图片回调的方法************************************************/

    private final ImageAdapter.onSelectImageCountListener mOnSelectImageCountListener = new ImageAdapter.onSelectImageCountListener() {
        @Override
        public void onSelectImageCount(int count) {
            if (count == 0) {
                mTvPreview.setClickable(false);
                mTvPreview.setText("预览");
                mTvPreview.setTextColor(ContextCompat.getColor(context, R.color.colorAccentGray));
            } else if (count > 0 && count <= MAX_SIZE) {
                mTvPreview.setClickable(true);
                mTvPreview.setText(String.format("预览(%d/9) ", count));
                mTvPreview.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            }
        }

        @Override
        public void onSelectImageList(List<Image> images) {
            mSelectedImages = images;
        }
    };

    /*************************************异步加载相册图片************************************************/

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MINI_THUMB_MAGIC,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        //创建一个CursorLoader，去异步加载相册的图片
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            return new CursorLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                ArrayList<Image> images = new ArrayList<>();
                //是否显示照相图片
                if (mHasCamera) {
                    //添加到第一个的位置（默认）
                    images.add(new Image());
                }
                ImageFolder defaultFolder = new ImageFolder();
                defaultFolder.setName("全部照片");
                defaultFolder.setPath("");
                mImageFolders.add(defaultFolder);

                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int id = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        String thumbPath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        String bucket = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));

                        Image image = new Image();
                        image.setPath(path);
                        image.setName(name);
                        image.setDate(dateTime);
                        image.setId(id);
                        image.setThumbPath(thumbPath);
                        image.setFolderName(bucket);
                        images.add(image);
                        //如果是被选中的图片
                        if (mSelectedImages.size() > 0) {
                            for (Image i : mSelectedImages) {
                                if (i.getPath().equals(image.getPath())) {
                                    image.setSelect(true);
                                }
                            }
                        }
                        //设置图片分类的文件夹
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        ImageFolder folder = new ImageFolder();
                        folder.setName(folderFile.getName());
                        folder.setPath(folderFile.getAbsolutePath());
                        //ImageFolder复写了equal方法，equal方法比较的是文件夹的路径
                        if (!mImageFolders.contains(folder)) {
                            folder.getImages().add(image);
                            //默认相册封面
                            folder.setAlbumPath(image.getPath());
                            mImageFolders.add(folder);
                        } else {
                            ImageFolder imageFolder = mImageFolders.get(mImageFolders.indexOf(folder));
                            imageFolder.getImages().add(image);
                        }
                    } while (data.moveToNext());
                }
                addImagesToAdapter(images);
                //全部照片
                defaultFolder.getImages().addAll(images);
                if (mHasCamera) {
                    defaultFolder.setAlbumPath(images.size() > 1 ? images.get(1).getPath() : null);
                } else {
                    defaultFolder.setAlbumPath(images.size() > 0 ? images.get(0).getPath() : null);
                }
                //删除掉不存在的，在于用户选择了相片，又去相册删除
                if (mSelectedImages.size() > 0) {
                    List<Image> rs = new ArrayList<>();
                    for (Image i : mSelectedImages) {
                        File f = new File(i.getPath());
                        if (!f.exists()) {
                            rs.add(i);
                        }
                    }
                    mSelectedImages.removeAll(rs);
                }
            }
            mImageFolderView.setImageFolders(mImageFolders);
            addImageFoldersToAdapter();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        }
    };


    @Override
    public void onSelectFolder(ImageFolderView imageFolderView, ImageFolder imageFolder) {
        addImagesToAdapter(imageFolder.getImages());
        mRvImage.scrollToPosition(0);
        mTvPhoto.setText(imageFolder.getName());
    }

    @Override
    public void onDismiss() {

    }

    @Override
    public void onShow() {

    }

    /*************************************相机拍照************************************************/

    @Override
    public void onCameraClick() {
        //首先申请下相机权限
        int isPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (isPermission == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                Toast.makeText(this, "需要您的相机权限!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * //用来打开相机的Intent
     */
    private void takePhoto() {
        //用来打开相机的Intent
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
            takePhotoImageFile = createImageFile();
            if (takePhotoImageFile != null) {
                Log.i("take photo", takePhotoImageFile.getAbsolutePath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ///7.0以上要通过FileProvider将File转化为Uri
                    mImageUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", takePhotoImageFile);
                } else {
                    //7.0以下则直接使用Uri的fromFile方法将File转化为Uri
                    mImageUri = Uri.fromFile(takePhotoImageFile);
                }
                //将用于输出的文件Uri传递给相机
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                //启动相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            //缩略图信息是储存在返回的intent中的Bundle中的，对应Bundle中的键为data，因此从Intent中取出 Bundle再根据data取出来Bitmap即可
            // Bundle extras = data.getExtras();
            // Bitmap bitmap = (Bitmap) extras.get("data");
//            BitmapFactory.decodeFile(this.getContentResolver().)
//            galleryAddPictures(mImageUri);
//            getSupportLoaderManager().restartLoader(0, null, mLoaderCallbacks);
            galleryAddPictures();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(mImageUri));
                Log.i("take photo", bitmap + "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    /**
     * 将拍的照片添加到相册
     */
    private void galleryAddPictures() {
        //把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(), takePhotoImageFile.getAbsolutePath(), takePhotoImageFile.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //通知图库更新
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takePhotoImageFile));
        sendBroadcast(mediaScanIntent);
    }
}
