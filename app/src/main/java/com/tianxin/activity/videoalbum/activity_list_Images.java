/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/16 0016
 */

package com.tianxin.activity.videoalbum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tianxin.Util.ActivityLocation;
import com.tianxin.activity.LatestNews.upfile.Fileupdate;
import com.tianxin.activity.Memberverify.activity_livebroadcast;
import com.tianxin.activity.edit.activity_nickname3;
import com.tianxin.activity.picenter.activity_picture;
import com.tianxin.adapter.setAdapter;
import com.tianxin.listener.Listeningstate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.steven.selectimage.model.Image;
import com.steven.selectimage.ui.SelectImageActivity;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.opensource.model.item;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.tianxin.BasActivity.BasActivity2;
import com.tencent.opensource.model.imglist;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_delete_img;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_Config;
import com.tencent.qcloud.costransferpractice.dialog.Callback;
import com.tencent.qcloud.costransferpractice.dialog.dialog_show2;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Config.getFileName;
import static com.tianxin.tencent.cos.MySessionCredentialProvider.DELETEObject;
import static com.tencent.qcloud.costransferpractice.utils.Constants.REQ_PERMISSION_CODE;
import static com.tencent.qcloud.costransferpractice.utils.FileUtils.isNetworkAvailable;


/**
 * 我的照片/我的相册
 */
public class activity_list_Images extends BasActivity2 implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = activity_list_Images.class.getName();
    @BindView(R.id.pullGridview)
    GridView pullGridview;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.imgback)
    itembackTopbr itemback;
    @BindView(R.id.text)
    TextView text;
    List<item> list = new ArrayList<>();
    List<Image> mSelectImages = new ArrayList<>();
    StringBuffer stringBuffer = new StringBuffer();

    public static void starsetAction(Context context) {
        Activity activity = (Activity) context;
        activity.startActivityForResult(new Intent(context, activity_list_Images.class), Config.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.user_item_list_video;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.album));
        itemback.sendbtn.setVisibility(View.VISIBLE);
        adappter = new setAdapter(context, list);
        pullGridview.setAdapter(adappter);
        pullGridview.setOnItemClickListener(this);
        pullGridview.setOnItemLongClickListener(this);
        pullGridview.setNumColumns(3);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadindate();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                toinitData();
            }
        });
    }

    @Override
    public void initData() {
        boolean checkpermissions = ActivityLocation.checkpermissions(activity);//申请定位权限
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }
        toinitData();
    }

    public void toinitData() {
        totalPage++;
        datamodule.getdatehttp(totalPage, paymnets);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Config.on:
                //删除数据
                if (data != null) {
                    deleteitem(data.getIntExtra(Constants.PATHVIDEO, -1));
                }
                break;
            case Config.sussess:
                //刷新数据
                loadindate();
                break;
            case Constants.requestCode:
                //相册选择多图回调
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ArrayList<Image> selectImages = data.getParcelableArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                    mSelectImages.clear();
                    mSelectImages.addAll(selectImages);
                    //showDialog();
                    dialogshow(getString(R.string.tm153));
                    //清空以防止下次数据累加
                    Fileupdate.getFileupdate(context).mlist.clear();
                    text.setText("");
                    //设置线程池 每次只允许运行10条线程 其他线程列队等待执行
                    ExecutorService executorService = Executors.newFixedThreadPool(mSelectImages.size());
                    for (Image selectImage : mSelectImages) {
                        String path = selectImage.getPath();
                        stringBuffer.append(path + "\r\n");
                        executorService.execute(new myRunnable(path));
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    text.setText(stringBuffer);
                    executorService.shutdown();
                }
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION_CODE) {
            for (String permission : permissions) {
                int granted = ContextCompat.checkSelfPermission(context, permission);
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "打开相册失败 请重新打开", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            //权限获取成功打开本地资源文件
            openselectactivity();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(context, activity_picture.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.dadelist, (Serializable) list);
        startActivityForResult(intent, Config.sussess);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        dialog_delete_img.dialogdeleteimg(context, new Paymnets() {
            @Override
            public void onSuccess() {
                deletevideo(position);
            }

            @Override
            public void onRefresh() {
                Toashow.show(getString(R.string.tv_msg222));
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: ");
            }
        });
        return true;
    }

    @OnClick({R.id.eorr, R.id.sendbtn})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                initData();
                break;
            case R.id.sendbtn:
                if (com.tencent.qcloud.costransferpractice.utils.Constants.checkPermission2(activity)) {
                    openselectactivity();
                }
                break;
        }
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(this);
        dialogGame.setTitle(getString(R.string.tv_msg261));
        dialogGame.setKankan(getString(R.string.tv_msg113));
        dialogGame.setTextColor(getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(new Paymnets() {
            @Override
            public void activity() {
                deleteitem(position);
            }
        });
    }

    /**
     * 删除文件
     *
     * @param position
     */
    private void deleteitem(int position) {
        item item = list.get(position);
        imglist imglist = (imglist) item.object;
        //移除list内容
        list.remove(position);
        adappter.notifyDataSetChanged();

        //删除平台数据
        if (imglist.getTencent() == Constants.TENCENT) {
            MySessionCredentialProvider.DELETEObject(getFileName(imglist.getPic(), ".com/"));
            MySessionCredentialProvider.setPaymnets(new Paymnets() {
                @Override
                public void onSuccess() {

                    deleteimglist(imglist);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "onFail:");
                }
            });
        } else {
            deleteimglist(imglist);
        }

    }

    private void deleteimglist(imglist imglist) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datamodule.delete(imglist, paymnets);
            }
        });
    }

    private void loadindate() {
        totalPage = 0;
        list.clear();
        adappter.notifyDataSetChanged();
        initData();
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Object object) {
            List<imglist> data = (List<imglist>) object;
            for (imglist imglist : data) {
                item item = new item();
                item.type = setAdapter.img;
                item.object = imglist;
                list.add(item);
            }
            if (data.size() > 0) {
                adappter.notifyDataSetChanged();
                itemback.settitle(String.format(getString(R.string.tv_msg269) + "", adappter.getCount()));
            }
            OnEorr();
        }

        @Override
        public void onSuccess() {
            Toashow.show(datamodule.mesresult.getMsg());
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            if (totalPage > 1) {
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            activity_list_Images.super.paymnets.ToKen(msg);
        }
    };

    private class myRunnable implements Runnable {
        private String url;

        public myRunnable(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            Fileupdate.getFileupdate(context).processingfile(url, Constants.picture, new Listeningstate() {
                @Override
                public void onStateChanged(TransferState state) {
                    Log.d(TAG, "onStateChanged: ");

                }

                @Override
                public void onProgress(long complete, long total, long progress, String size) {
                    Log.d(TAG, "onProgress: " + complete + " " + total + " " + progress + "" + size);
                }

                @Override
                public void onSuccess(List<String> m, String accessUrl) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stringBuffer.append(accessUrl + "\r\n");
                            text.setText(stringBuffer.toString());
                            datamodule.addvideolist(accessUrl, "01", null);
                        }
                    });
                    if (m.size() == mSelectImages.size()) {
                        Log.d(TAG, "onSuccess: 上传完成");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogLoadings();
                                Toashow.show(getString(R.string.tm154));
                                loadindate();
                            }
                        });
                    }
                }

                @Override
                public void onFail() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogLoadings();
                        }
                    });
                }
            });
        }
    }

    private com.tencent.qcloud.costransferpractice.dialog.Callback dialogcallback = new Callback() {
        @Override
        public void onFall() {

        }

        @Override
        public void onSuccess() {
            activity_livebroadcast.setAction(context);
        }

        @Override
        public void onappeal() {
            /**
             * 封号意见反馈
             */
            activity_nickname3.setAction(context);
        }
    };

    /**
     * 上传文件之前判断下是否已经实名认证
     */
    private void openselectactivity() {
        switch (userInfo.getState()) {
            case 0:
                dialog_show2.mshow(context, getString(com.tencent.qcloud.costransferpractice.R.string.t1), dialogcallback);
                break;
            case 1:
                dialog_show2.mshow(context, getString(com.tencent.qcloud.costransferpractice.R.string.t4), getString(com.tencent.qcloud.costransferpractice.R.string.t8), 1, dialogcallback);
                break;
            case 2:
                if (!isNetworkAvailable(context)) {
                    Toashow.show(getString(R.string.eorrfali2));
                    return;
                }
                SelectImageActivity.setAction(activity, mSelectImages, Constants.requestCode);
                break;
            case 3:
                dialog_show2.mshow(context, getString(com.tencent.qcloud.costransferpractice.R.string.t5), getString(com.tencent.qcloud.costransferpractice.R.string.t7), 3, dialogcallback);
                break;
            default:
                dialog_show2.mshow(context, getString(com.tencent.qcloud.costransferpractice.R.string.t6), dialogcallback);
                break;
        }
    }

}
