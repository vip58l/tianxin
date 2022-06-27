package com.tianxin.Fragment.page3.fragment;

import static com.tianxin.Util.Config.getFileName;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.McallBack;
import com.tianxin.R;
import com.tianxin.Util.CommonUtils;
import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picbage;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_game;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.listener.Callback;
import com.tianxin.listener.OnItemChildClickListener;
import com.tianxin.listener.OnPraiseOrCommentClickListener;
import com.tianxin.listener.Paymnets;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.tianxin.widget.LikePopupWindow;
import com.tianxin.widget.item_chid_play;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.perimg;
import com.tencent.opensource.model.trend;
import com.tencent.opensource.model.videolist;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关注 动态朋友圈
 */
public class page3_2 extends BasFragment {
    private static final String TAG = page3_2.class.getName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.tv_send_comment)
    TextView tv_send_comment;
    int TYPE = 0;
    int delposition;
    trend trend;

    //点赞评论
    private LikePopupWindow likePopupWindow;
    private int isLike;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        if (arguments != null) {
            TYPE = arguments.getInt(Constants.TYPE);
            getUserId = arguments.getString(Constants.USERID);
            if (isVisibleToUser && list2.size() == 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        smartRefreshLayout.autoRefresh();
                    }
                }, 100);
            }

        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fmessage_view_item, null);
    }

    @Override
    @OnClick({R.id.eorr, R.id.fabudongtai})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.fabudongtai:
                //发布最新动态
                activitynews();
                break;
            case R.id.eorr:
                loadMoreData();
                break;
        }

    }

    @Override
    public void iniview() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter = new Radapter(context, list2, radapter.fmessage));
        recyclerview.addItemDecoration(new MyDecoration(context, 0));
        //点击隐藏评论
        recyclerview.setOnTouchListener((view, motionEvent) -> {
            if (llComment.getVisibility() == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE);

                return true;
            }
            return false;
        });//隐藏评评输入框
        //下拉刷新加载
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                getinidate();
            }
        });
        //item点击事件回调事件处理
        radapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(Object object, View view, int position) {
                trend = (com.tencent.opensource.model.trend) object;
                member = trend.getMember();
                delposition = position;
                switch (view.getId()) {
                    case R.id.title:
                    case R.id.icon:
                        //打开个人主页
                        tostartActivity(member);
                        break;
                    case R.id.showdelete:
                        //评论点赞
                        //showLikePopupWindow(view, position);

                        //点击弹窗提示举报或删除
                        dialog_item_rs dialogitemrs = dialog_item_rs.dialogitemrs(context, paymnets);
                        //如果是自己的内容显示 删除按键
                        dialogitemrs.Fmessage(String.valueOf(trend.getUserid()).equals(userInfo.getUserId()) ? View.VISIBLE : View.GONE);
                        break;
                    case R.id.show_img:
                        //打开视频或图片浏览
                        itemclick();
                        break;
                }
            }

            @Override
            public void OnClickListener(Object object, View view, int position) {
                onstartActivity((List<Object>) object, position);
            }
        });
    }

    @Override
    public void initData() {
        //AsyncTask<Void, Void, info> execute = new MyTask().execute();//创建启动线程
    }

    /**
     * 加载数据
     */
    public void getinidate() {
        totalPage++;
        datamodule.fmessages(getUserId, totalPage, TYPE, paymnets);
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 加载数据
     */
    public void loadMoreData() {
        totalPage = 0;
        list2.clear();
        radapter.notifyDataSetChanged();
        getinidate();
    }

    public static page3_2 showfmessage(int type, String userid) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USERID, userid);
        bundle.putInt(Constants.TYPE, type);
        page3_2 fmessage = new page3_2();
        fmessage.setArguments(bundle);
        return fmessage;
    }

    /**
     * 举报投诉
     */
    public static void reportUser(Context context, String userid, String uid) {
        videolist videolist = new videolist();
        videolist.setUserid(userid);
        videolist.setId(uid);
        item_chid_play.reportUser(context, videolist, 2);
    }

    private final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
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


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //获取到原来的位置
            int fromPosition = viewHolder.getAdapterPosition();
            //获取到拖到的位置
            int targetPosition = target.getAdapterPosition();
            if (fromPosition < targetPosition) {
                for (int i = fromPosition; i < targetPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > targetPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
            radapter.notifyItemMoved(fromPosition, targetPosition);
            return true;
        }


        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            list2.remove(position);
            radapter.notifyItemRemoved(position);
        }
    });

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.sussess) {
            loadMoreData();
        }
    }

    /**
     * 打开视频或图片浏览
     */
    public void itemclick() {
        if (TextUtils.isEmpty(trend.getVideo())) {
            onstartActivity(trend);
        } else {
            startvideoActivity(trend);
        }
    }

    /**
     * 评论弹框
     *
     * @param view
     * @param position
     */
    private void showLikePopupWindow(View view, int position) {
        //item 底部y坐标
        final int mBottomY = getCoordinateY(view) + view.getHeight();
        if (likePopupWindow == null) {
            likePopupWindow = new LikePopupWindow(context, 1);
        }
        //监听点赞 评论回调
        likePopupWindow.setOnPraiseOrCommentClickListener(new OnPraiseOrCommentClickListener() {
            @Override
            public void onPraiseClick(int position) {
                //调用点赞接口
                //getLikeData();
            }

            @Override
            public void onCommentClick(int position) {
                llComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                etComment.setHint("说点什么");
                //to_user_id = null;
                KeyboardUtil.showSoftInput(getContext());
                etComment.setText("");
                view.postDelayed(() -> {
                    int y = getCoordinateY(llComment) - 20;
                    //评论时滑动到对应item底部和输入框顶部对齐
                    recyclerview.smoothScrollBy(0, mBottomY - y);
                }, 300);
            }

            @Override
            public void onClickFrendCircleTopBg(int position) {
                page3_2.reportUser(context, String.valueOf(member.getId()), getString(R.string.tv_msg161) + trend.getId());
            }

            @Override
            public void onDeleteItem(int position) {
                delete(delposition); //删除数据
            }


        }).setTextView(isLike).setCurrentPosition(position);

        //显示状态关闭重新的开
        if (likePopupWindow.isShowing()) {
            likePopupWindow.dismiss();
        } else {
            likePopupWindow.showPopupWindow(view);
        }

    }

    /**
     * 滑动浏览图片
     */
    private void onstartActivity(trend trend) {
        List<perimg> array = new ArrayList<>();
        perimg imglist = new perimg();
        imglist.setId(String.valueOf(trend.getId()));
        imglist.setPic(trend.getImage());
        imglist.setBgpic(trend.getImage());
        array.add(imglist);
        tostartActivity(array);
    }

    /**
     * 滑动浏览图片
     */
    private void onstartActivity(List<Object> list, int position) {
        List<perimg> array = new ArrayList<>();
        for (Object o : list) {
            perimg imglist = new perimg();
            imglist.setId(String.valueOf(position));
            imglist.setPic(o.toString());
            imglist.setBgpic(o.toString());
            array.add(imglist);
        }
        Intent intent = new Intent();
        intent.setClass(getContext(), activity_picbage.class);
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.perimg, (Serializable) array);
        getContext().startActivity(intent);
    }

    /**
     * 打开视频播放
     *
     * @param trend
     */
    private void startvideoActivity(trend trend) {
        if (!TextUtils.isEmpty(trend.getVideotest())) {
            trend.setVideo(trend.getVideotest());
        } else {
            if (trend.getTencent() == Constants.TENCENT) {
                trend.setVideo(DemoApplication.presignedURL(trend.getVideo()));
            }
        }
        Intent intent = new Intent(getContext(), videoijkplayer0.class);
        intent.putExtra(Constants.POSITION, 0);
        intent.putExtra(Constants.TITLE, trend.getTitle());
        intent.putExtra(Constants.PATHVIDEO, trend.getVideo());
        intent.putExtra(Constants.PATHIMG, trend.getImage());
        intent.putExtra(Constants.Edit, false);
        startActivity(intent);
    }

    /**
     * 获取控件左上顶点Y坐标
     *
     * @param view
     * @return
     */
    private int getCoordinateY(View view) {
        int[] coordinate = new int[2];
        view.getLocationOnScreen(coordinate);
        return coordinate[1];
    }

    /**
     * 弹出键盘 隐藏键盘处理
     *
     * @param visibility
     */
    public void updateEditTextBodyVisible(int visibility) {
        llComment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            llComment.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(etComment.getContext(), etComment);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(etComment.getContext(), etComment);
        }
    }

    /**
     * 删除腾讯云数据
     *
     * @param position
     */
    public void delete(int position) {
        trend trend = (com.tencent.opensource.model.trend) list2.get(position);
        if (trend == null) {
            return;
        }

        String delete = trend.getImage();
        if (TextUtils.isEmpty(trend.getImage()) && TextUtils.isEmpty(trend.getVideo())) {
            McallBack.del(trend.getId());
            radapter.removenotifyDate(position);
            return;
        }
        int index = delete.indexOf(",");
        List<String> list = index > 0 ? Arrays.asList(delete.split(",")) : Arrays.asList(trend.getImage());
        List<String> deleName = new ArrayList<>();

        //删除图片文件
        for (String s : list) {
            deleName.add(getFileName(s, ".com/"));
        }

        //删除视频文件
        if (trend.getTencent() == Constants.TENCENT && !TextUtils.isEmpty(trend.getVideo())) {
            deleName.add(getFileName(trend.getVideo(), ".com/"));
        }

        //批量删除文件
        MySessionCredentialProvider.DELETEObject(deleName);
        MySessionCredentialProvider.setPaymnets(new Paymnets() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "MySessionCredentialProvideronSuccess: ");

            }

            @Override
            public void onFail() {
                Log.d(TAG, "MySessionCredentialProvider onFail: ");
            }
        });
        McallBack.del(trend.getId());
        radapter.removenotifyDate(position);

    }

    /**
     * Android 多线程：手把手教你使用AsyncTask
     */
    private class MyTask extends AsyncTask<Void, Void, info> {
        @Override
        protected info doInBackground(Void... voids) {
            info info = new info();
            info.setAvatar("test");
            return info;
        }

        /**
         * 多线程的应用在Android开发中是非常常见的，常用方法主要有：
         * 1 继承Thread类
         * 2 实现Runnable接口
         * 3 Handler
         * 4 AsyncTask
         * 5 HandlerThread
         */
        @Override
        protected void onPostExecute(info result) {
        }
    }

    private Paymnets paymnets = new Paymnets() {

        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFail() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(Object object) {
            List<trend> ts = (List<trend>) object;
            list2.addAll(ts);
            radapter.notifyDataSetChanged();
            OnEorr();
        }

        @Override
        public void status(int position) {
            switch (position) {
                case 3:
                    McallBack.query(context, callback);
                    break;
                case 4:
                    //举报
                    page3_2.reportUser(context, String.valueOf(member.getId()), getString(R.string.tv_msg161) + trend.getId());
                    break;

            }
        }

        @Override
        public void activity() {
            delete(delposition); //删除数据
        }

        @Override
        public void onSuccess(String msg) {
            totalPage--;
            ToastUtil.toastLongMessage(getString(R.string.eorrtext));
            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            page3_2.super.paymnets.ToKen(msg);
        }

    };

    private Callback callback = new Callback() {

        @Override
        public void isNetworkAvailable() {
        }

        @Override
        public void onSuccess() {
            dialog_game.myshow(context, paymnets, delposition);
        }

        @Override
        public void onFall() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
        }


    };

}




