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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.activity.video2.activity.activitymyplay;
import com.tianxin.adapter.setAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.dialog.dialog_videotitle;
import com.tencent.opensource.model.videolist;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.dialog_delete_video;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_Config;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Config.getFileName;
import static com.tianxin.tencent.cos.MySessionCredentialProvider.DELETEObject;
import static com.tencent.qcloud.costransferpractice.object.ObjectActivity.ACTIVITY_VIDEO;

/**
 * 我的视频
 */
public class activity_list_Video extends BasActivity2 implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    @BindView(R.id.pullGridview)
    GridView pullGridview;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.imgback)
    itembackTopbr itemback;
    List<item> list = new ArrayList<>();
    String TAG = activity_list_Video.class.getSimpleName();

    public static void starsetAction(Context context) {
        Activity activity = (Activity) context;
        activity.startActivityForResult(new Intent(context, activity_list_Video.class), Config.sussess);
    }

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(activity, getResources().getColor(R.color.white));
        return R.layout.user_item_list_video;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.myvideo));
        itemback.sendbtn.setVisibility(View.VISIBLE);
        pullGridview.setAdapter(adappter = new setAdapter(context, list));
        pullGridview.setOnItemClickListener(this);
        pullGridview.setOnItemLongClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadgetdatehttp();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                getdatehttp();
            }
        });
    }

    @Override
    public void initData() {
        boolean checkpermissions = ActivityLocation.checkpermissions(this);//申请定位权限
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }
        loadgetdatehttp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //删除数据
        if (resultCode == Config.on && data != null) {
            deleteitem(data.getIntExtra(Constants.PATHVIDEO, -1));
            return;
        }

        //刷新数据
        if (requestCode == Config.sussess) {
            totalPage = 0;
            list.clear();
            adappter.notifyDataSetChanged();
            getdatehttp();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<videolist> mlist = new ArrayList<>();
        for (item item : list) {
            if (item.object instanceof videolist) {
                videolist video = (videolist) item.object;
                mlist.add(video);
            }
        }

        //TYPE=0我的视频
        activitymyplay.starsetAction(context, mlist, position, totalPage, TYPE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        videolist videolist = (videolist) list.get(position).object;
        dialog_delete_video.video(context,videolist.getTop(), new Paymnets() {
            @Override
            public void onSuccess() {
                // deleteitem(position);
                deletevideo(position);
            }

            @Override
            public void onRefresh() {
                videolist videolist = (videolist) list.get(position).object;
                if (videolist == null) {
                    return;
                }
                dialog_videotitle.dialog_videotitle(context, videolist, new Paymnets() {
                    @Override
                    public void onSuccess() {
                        loadgetdatehttp();
                    }
                });
            }

            @Override
            public void onLoadMore() {
                Log.d(TAG, "onLoadMore: ");
            }

            @Override
            public void fall(int code) {
                //置顶处理
                videolist.setTop(code);
                adappter.notifyDataSetChanged();
                datamodule.senvideotop(videolist,code);
            }


        });
        return true;
    }

    @OnClick({R.id.eorr, R.id.sendbtn})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                totalPage = 0;
                getdatehttp();
                break;
            case R.id.sendbtn:
                sUploadActivity(ACTIVITY_VIDEO);
                break;
        }

    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }


    /**
     * 联网请求获取数据
     */
    public void getdatehttp() {
        totalPage++;
        datamodule.getvidel(totalPage, paymnets);
    }

    private void deletevideo(int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(context.getString(R.string.tv_msg221));
        dialogGame.setKankan(context.getString(R.string.btn_ok));
        dialogGame.setTextColor(context.getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(context.getResources().getColor(R.color.c_fu));
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
        videolist videolist = (videolist) item.object;
        list.remove(position);
        adappter.notifyDataSetChanged();

        //删除多个文件1到N个文件
        if (videolist.getTencent() == Constants.TENCENT) {
            String videofileName = getFileName(videolist.getPlayurl(), ".com/");
            String imagefileName = getFileName(videolist.getBigpicurl(), ".com/");
            List<String> objectList = Arrays.asList(videofileName, imagefileName);
            MySessionCredentialProvider.DELETEObject(objectList);
            MySessionCredentialProvider.setPaymnets(new Paymnets() {
                @Override
                public void onSuccess() {
                    deletevidel(videolist);
                }

                @Override
                public void onFail() {
                    Log.d(TAG, "onFail:");
                }
            });
        } else {
            deletevidel(videolist);
        }
    }

    private void deletevidel(videolist videolist) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                datamodule.delete(videolist, paymnets);
            }
        });

    }

    private void loadgetdatehttp() {
        totalPage = 0;
        list.clear();
        adappter.notifyDataSetChanged();
        getdatehttp();
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            OnEorr();
            totalPage--;
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            OnEorr();
            totalPage--;
        }

        @Override
        public void onSuccess() {
            ToastUtils.showShort(datamodule.mesresult.getMsg());
            OnEorr();
            totalPage--;
        }

        @Override
        public void onSuccess(Object object) {
            List<videolist> data = (List<videolist>) object;
            if (data.size() > 0) {
                for (videolist datum : data) {
                    item item = new item();
                    item.type = setAdapter.video;
                    item.object = datum;
                    list.add(item);
                }
                adappter.notifyDataSetChanged();
                itemback.settitle(String.format("我的视频(%s)", adappter.getCount()));
            }
            OnEorr();
        }

        @Override
        public void onSuccess(String msg) {
            if (totalPage > 1) {
                totalPage--;
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
            }
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            activity_list_Video.super.paymnets.ToKen(msg);
        }
    };
}
