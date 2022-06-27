package com.tianxin.activity.LatestNews;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.LatestNews.upfile.FilePost;
import com.tianxin.activity.LatestNews.upfile.filevideo;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_new_item;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Listeningstate;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.itembackTopbr;
import com.steven.selectimage.model.Image;
import com.steven.selectimage.ui.PreviewImageActivity;
import com.steven.selectimage.ui.SelectImageActivity;
import com.steven.selectimage.ui.adapter.SelectedImageAdapter;
import com.steven.selectimage.utils.TDevice;
import com.steven.selectimage.widget.recyclerview.OnItemClickListener;
import com.steven.selectimage.widget.recyclerview.SpaceGridItemDecoration;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发表动态
 */
public class activity_news extends BasActivity2 {
    String TAG = activity_news.class.getName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.contentet)
    EditText contentet;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.drag_tip)
    TextView mDragTip;
    @BindView(R.id.loationimges)
    public LinearLayout loationimges;
    @BindView(R.id.filevideo)
    public filevideo filevideo;
    private List<Image> mSelectImages;
    private SelectedImageAdapter mAdapter;
    private com.steven.selectimage.model.Image image;
    private String mStringimage;
    private static int PERMISSION_REQUEST_CODE = 0;
    private static final int SELECT_IMAGE_REQUEST = 0x0011;
    private static int TYPE_TEXT = 0;   //文本内容
    private static int TYPE_iMGS = 1;   //图片内容
    private static int TYPE_VIDEO = 2;  //视频内容
    private List<Object> arraylist = new ArrayList<>();
    private FilePost filePost;
    public static String path;

    @Override
    protected int getview() {
        return R.layout.activity_release_news;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg163));
        itemback.setsendright(getString(R.string.tv_msg162));
        itemback.sendrightbg();
        contentet.addTextChangedListener(textWatcher);
        filePost = new FilePost(context, activity);
    }

    /**
     * 选择有多张图片
     */
    private void upmSelectImag() {
        for (Image image : mSelectImages) {
            if (TextUtils.isEmpty(image.getPath())) {
                mSelectImages.remove(image);
            }
        }

        //设置线程池 每次只允许运行10条线程 其他线程列队等待执行
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        for (Image selectImage : mSelectImages) {
            String path = selectImage.getPath();
            executorService.execute(new myThread(path, 1));
        }
        //执行线程池任务
        executorService.shutdown();

    }

    @Override
    public void initData() {
        path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        image = new Image();
        image.setPath("");
        mSelectImages = new ArrayList<>();
        mSelectImages.add(image);

        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerview.addItemDecoration(new SpaceGridItemDecoration((int) TDevice.dipToPx(getResources(), 1)));
        recyclerview.setAdapter(mAdapter = new SelectedImageAdapter(context, mSelectImages, 0)); //适配器加入布局
        mItemTouchHelper.attachToRecyclerView(recyclerview); //用于移动吸附着item
        mAdapter.setmItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PreviewImageActivity.setAction(context, mSelectImages, position);
            }

            @Override
            public void onAdd() {
                //实名认证后可发布视频
                if (userInfo.getState() == Constants.TENCENT2) {
                    showdialog(); //弹出选择视频或照片发布
                    return;
                }

                //未实名会员打开相册选择图片
                if (checkpermissions()) {
                    startActivity();
                }

            }

            @Override
            public void delonItemClick(int position) {
                mSelectImages.remove(position);
                mAdapter.notifyDataSetChanged();
                if (!fileupdate.isgetboolean(mSelectImages)) {
                    mSelectImages.add(image);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }


    @Override
    @OnClick({R.id.sendright})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.sendright:
                String message = Sendmessage();
                McallBack.query(context, message, callback);
                break;
        }

    }


    @Override
    public void OnEorr() {

    }


    /**
     * 申请权限
     */
    private boolean checkpermissions() {
        for (String permission : permissions) {
            int checkSel = ContextCompat.checkSelfPermission(context, permission);
            if (checkSel != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;

    }


    /**
     * 转到相册选择图片页
     */
    private void startActivity() {
        SelectImageActivity.setAction(activity, mSelectImages, SELECT_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toashow.show(getString(R.string.tts_ss));
            } else {
                startActivity();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMAGE_REQUEST:
                    if (data != null) {
                        List<Image> imageList = data.getParcelableArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                        mSelectImages.clear();
                        mSelectImages.addAll(imageList);
                        if (imageList.size() < 9) {
                            mSelectImages.add(image);
                        }
                        //提示左右滑动可以删除图片文本
                        mDragTip.setVisibility(mSelectImages.size() > 1 ? View.VISIBLE : View.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case Constants.requestCode:
                    if (data != null) {
                        //选择视频回调处理
                        filePost.videoediting(data, this);
                    }
                    break;
            }
        }


    }

    /**
     * 编辑框监听
     */
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int editStart = contentet.getSelectionStart();
            int editEnd = contentet.getSelectionEnd();
            if (s.length() > 500) {
                Toashow.show("你输入的字数已经超过了限制");
                s.delete(editStart - 1, editEnd);
                contentet.setText(s);

            }
            message.setText(s.length() > 0 ? String.format("%s/500", s.length()) : "0/500");
        }
    };

    /**
     * 图片拖动排列
     */
    private ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            // 获取触摸响应的方向   包含两个 1.拖动dragFlags 2.侧滑删除swipeFlags
            // 代表只能是向左侧滑删除，当前可以是这样ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            int dragFlags;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override //拖动的时候不断的回调方法
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //获取到原来的位置
            int fromPosition = viewHolder.getAdapterPosition();
            //获取到拖到的位置
            int targetPosition = target.getAdapterPosition();

            if (fromPosition < targetPosition) {
                for (int i = fromPosition; i < targetPosition; i++) {
                    Collections.swap(mSelectImages, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > targetPosition; i--) {
                    Collections.swap(mSelectImages, i, i - 1);
                }
            }

            //用于交换在指定列表中的指定位置的元素
            mAdapter.notifyItemMoved(fromPosition, targetPosition);
            return true;
        }

        @Override //侧滑删除后会回调的方法
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mSelectImages.remove(position);
            mAdapter.notifyDataSetChanged();

            if (!fileupdate.isgetboolean(mSelectImages)) {
                mSelectImages.add(image);
                mAdapter.notifyDataSetChanged();
            }
        }
    });

    /**
     * 获取文本内容
     *
     * @return
     */
    private String Sendmessage() {
        return contentet.getText().toString();
    }

    /**
     * 关闭弹窗加载
     */
    private void mydismiss() {
        if (myshow != null) {
            myshow.dismiss();
        }

    }

    /**
     * 监听是否有权发布内容
     * 或服务器是否正常
     */
    private Callback callback = new Callback() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            mydismiss();
        }

        @Override
        public void onSuccess() {
            myshow.show();
            //用于回调上一页刷新数据
            setResult(Config.setResult);

            //上传视频文件
            if (!TextUtils.isEmpty(filevideo.getPath())) {
                //设置线程池 每次只允许运行10条线程 其他线程列队等待执行
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                executorService.execute(new myThread(filevideo.getPath(), 2));
                executorService.shutdown();
                return;
            }

            //上传图片文件
            if (mSelectImages.size() > Constants.OnImages) {
                upmSelectImag();
                return;
            }

            //普通文本内容
            datamodule.upselecttext(paymnets, Sendmessage(), null, null);
        }

        @Override
        public void onSuccess(String msg) {
            ToastUtil.toastLongMessage(msg);
            mydismiss();
        }

        @Override
        public void onFall() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            mydismiss();
        }

        @Override
        public void Blockedaccount() {
            //帐户封禁通知
            mydismiss();
            dialog_Blocked.myshow(context);
        }
    };

    /**
     * 发送回文本内容回调
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            onSuccess(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            onSuccess(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess() {
            message.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onSuccess("发布成功");
                    finish();
                }
            }, 1000);
        }

        @Override
        public void onSuccess(String msg) {
            mydismiss();
            Toashow.show(msg);

        }

        @Override
        public void status(int position) {
            switch (position) {
                case 0:
                    if (checkpermissions()) {
                        //打开照片相册
                        startActivity();
                    }
                    break;
                case 1:
                    if (checkpermissions()) {
                        //打开视频相册
                        filePost.openfile();
                    }
                    break;
            }

        }
    };

    /**
     * 上传文件回调监听事件
     */
    private Listeningstate listeningstate = new Listeningstate() {
        @Override
        public void onStateChanged(TransferState state) {
            switch (state) {
                case WAITING:
                    break;
                case IN_PROGRESS:
                    break;
                case COMPLETED:
                    break;
                case FAILED:
                    break;
                case PAUSED:
                    break;
                case CANCELED:
                    break;
                case RESUMED_WAITING:
                    break;
                case CONSTRAINED:
                    break;
            }
        }

        @Override
        public void onProgress(long complete, long total, long progress, String size) {
            //上传进度
            Log.d(TAG, "onProgress:complete=" + complete + " total=" + total + " progress=" + progress + " size=" + size);
        }

        @Override
        public void onSuccess(List<String> list, String accessUrl) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //封面和视频文件保存到服务端
                    if (!TextUtils.isEmpty(filevideo.getPath())) {
                        String image = list.get(0);
                        datamodule.upselecttext(paymnets, Sendmessage(), image, accessUrl);
                        return;
                    }

                    //图片保存到服务端
                    if (mSelectImages.size() == list.size()) {
                        mStringimage = fileupdate.getStringBuffer(list);
                        datamodule.upselecttext(paymnets, Sendmessage(), mStringimage, "");
                    }

                }
            });
        }

        @Override
        public void onFail() {
            mydismiss();
        }

        @Override
        public void oncomplete() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mydismiss();
                    ToastUtil.toastLongMessage(getString(R.string.regs1upate));

                }
            });
        }
    };

    /**
     * 这里上传图片文件
     */
    private class myThread extends Thread {
        private String path;
        private int TYPE;

        public myThread(String path, int type) {
            this.path = path;
            this.TYPE = type;
        }

        @Override
        public void run() {
            super.run();
            switch (TYPE) {
                case 1:
                    //图片文件
                    fileupdate.processingfile(path, Constants.whatsnew, listeningstate);
                    break;
                case 2:
                    //视频封面
                    fileupdate.videocoverfile(filevideo.getBitmap(), Constants.whatsnew, listeningstate);
                    //视频文件
                    fileupdate.videofile(new File(path), Constants.WechatMoments, listeningstate);
                    break;
            }
        }
    }

    /**
     * 选择视频或照片
     */
    public void showdialog() {
        if (arraylist.size() == 0) {
            arraylist.add("照片");
            arraylist.add("视频");
        }
        dialog_new_item.myshow(context, arraylist, paymnets);
    }

}