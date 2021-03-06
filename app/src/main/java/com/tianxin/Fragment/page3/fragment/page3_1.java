package com.tianxin.Fragment.page3.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.tianxin.Module.McallBack;
import com.tianxin.Util.CommonUtils;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picbage;
import com.tianxin.activity.video.videoijkplayer0;
import com.tianxin.activity.video2.activity.activitymyplay;
import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.itemdecoration.RecycleViewDivider;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.dialog.dialog_game;
import com.tianxin.widget.item_chid_play;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.givelist;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Util.Constants;
import com.tianxin.dialog.dialog_item_rs;
import com.tianxin.listener.Callback;
import com.tianxin.listener.OnItemChildClickListener;
import com.tianxin.listener.OnPraiseOrCommentClickListener;
import com.tianxin.listener.Paymnets;
import com.tianxin.tencent.cos.MySessionCredentialProvider;
import com.tianxin.widget.LikePopupWindow;
import com.tencent.opensource.model.info;
import com.tencent.opensource.model.perimg;
import com.tencent.opensource.model.trend;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;
import static com.tianxin.Util.Config.getFileName;

/**
 * ???????????????
 */
public class page3_1 extends BasFragment {
    private static final String TAG = page3_1.class.getName();
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

    /**
     * ????????????
     */
    private LikePopupWindow likePopupWindow;
    private int isLike;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
            Bundle arguments = getArguments();
            if (arguments != null) {
                TYPE = arguments.getInt(Constants.TYPE);
                getUserId = arguments.getString(Constants.USERID);

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
                //??????????????????
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
        recyclerview.addItemDecoration(new RecycleViewDivider(context, 0, 3, context.getResources().getColor(R.color.home3)));

        //??????item??????????????????item??????????????????
        ((DefaultItemAnimator) recyclerview.getItemAnimator()).setSupportsChangeAnimations(false);
        RecyclerView.ItemAnimator animator = recyclerview.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        recyclerview.setAdapter(radapter = new Radapter(context, list2, radapter.fmessage));
        //??????????????????
        recyclerview.setOnTouchListener((view, motionEvent) -> {
            if (llComment.getVisibility() == View.VISIBLE) {
                updateEditTextBodyVisible(View.GONE);
                return true;
            }
            return false;
        });//?????????????????????
        //??????????????????
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//??????false??????????????????
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//??????false??????????????????
                getinidate();
            }
        });
        //item??????????????????????????????
        radapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(Object object, View view, int position) {
                trend = (com.tencent.opensource.model.trend) object;
                member = trend.getMember();
                delposition = position;
                switch (view.getId()) {
                    case R.id.title:
                    case R.id.icon:
                        //??????????????????
                        tostartActivity(member);
                        break;
                    case R.id.showdelete:
                        //????????????
                        //showLikePopupWindow(view, position);

                        //?????????????????????????????????
                        dialog_item_rs dialogitemrs = dialog_item_rs.dialogitemrs(context, paymnets);

                        //?????????????????????????????? ????????????
                        if (!TextUtils.isEmpty(UserInfo.getInstance().getUserId())) {
                            dialogitemrs.Fmessage(String.valueOf(trend.getUserid()).equals(UserInfo.getInstance().getUserId()) ? View.VISIBLE : View.GONE);
                        }

                        break;
                    case R.id.show_img:
                        if (userInfo.getState() >= Constants.TENCENT3) {
                            dialog_Blocked.myshow(context);
                            return;
                        }
                        //???????????????????????????
                        itemclick();
                        break;
                }
            }

            @Override
            public void OnClickListener(Object object, View view, int position) {
                onstartActivity((List<Object>) object, position);
            }

            @Override
            public void onItemdefaultListener(Object object, View view, int position) {
                //???TA?????????
                trend trend = (com.tencent.opensource.model.trend) list2.get(position);
                datamodule.givelist(trend.getId(), trend.getUserid(), 1, new Paymnets() {
                    @Override
                    public void isNetworkAvailable() {

                    }

                    @Override
                    public void onSuccess(Object object) {
                        givelist givelist = (com.tencent.opensource.model.givelist) object;

                        if (Integer.parseInt(givelist.getId()) == 0) {
                            trend.setLove(trend.getLove() - 1);
                            Toashow.show(getString(R.string.tm1391));
                        } else {
                            trend.setLove(trend.getLove() + 1);
                            Toashow.show(getString(R.string.tm139));
                        }
                        radapter.notifyItemChanged(position);

                    }

                    @Override
                    public void onFail() {

                    }
                });

            }
        });

    }

    @Override
    public void initData() {
        //AsyncTask<Void, Void, info> execute = new MyTask().execute();//??????????????????
        if (list2.size() == 0&&isLike==0) {
            isLike=1;
            getinidate();
        }
    }

    /**
     * ????????????
     */
    public void getinidate() {
        totalPage++;
        datamodule.fmessage(getUserId, totalPage, TYPE, paymnets);
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
     * ????????????
     */
    public void loadMoreData() {
        totalPage = 0;
        list2.clear();
        radapter.notifyDataSetChanged();
        getinidate();
    }

    public static page3_1 showfmessage(int type, String userid) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USERID, userid);
        bundle.putInt(Constants.TYPE, type);
        page3_1 fmessage = new page3_1();
        fmessage.setArguments(bundle);
        return fmessage;
    }

    /**
     * ????????????
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

            // ???????????????????????????   ???????????? 1.??????dragFlags 2.????????????swipeFlags
            // ?????????????????????????????????????????????????????????ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT
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
            //????????????????????????
            int fromPosition = viewHolder.getAdapterPosition();
            //????????????????????????
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
     * ???????????????????????????
     */
    public void itemclick() {
        //????????????
        if (TextUtils.isEmpty(trend.getVideo())) {
            onstartActivity(trend);
        } else {
            //????????????
            starsetAction(trend);
        }
    }

    /**
     * ????????????
     *
     * @param view
     * @param position
     */
    private void showLikePopupWindow(View view, int position) {
        //item ??????y??????
        final int mBottomY = getCoordinateY(view) + view.getHeight();
        if (likePopupWindow == null) {
            likePopupWindow = new LikePopupWindow(context, 1);
        }
        //???????????? ????????????
        likePopupWindow.setOnPraiseOrCommentClickListener(new OnPraiseOrCommentClickListener() {
            @Override
            public void onPraiseClick(int position) {
                //??????????????????
                //getLikeData();
            }

            @Override
            public void onCommentClick(int position) {
                llComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
                etComment.setHint("????????????");
                //to_user_id = null;
                KeyboardUtil.showSoftInput(getContext());
                etComment.setText("");
                view.postDelayed(() -> {
                    int y = getCoordinateY(llComment) - 20;
                    //????????????????????????item??????????????????????????????
                    recyclerview.smoothScrollBy(0, mBottomY - y);
                }, 300);
            }

            @Override
            public void onClickFrendCircleTopBg(int position) {
                page3_1.reportUser(context, String.valueOf(member.getId()), getString(R.string.tv_msg161) + trend.getId());
            }

            @Override
            public void onDeleteItem(int position) {
                delete(delposition); //????????????
            }


        }).setTextView(isLike).setCurrentPosition(position);

        //??????????????????????????????
        if (likePopupWindow.isShowing()) {
            likePopupWindow.dismiss();
        } else {
            likePopupWindow.showPopupWindow(view);
        }

    }

    /**
     * ??????????????????
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
     * ??????????????????
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
     * ??????????????????
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
        intent.putExtra(com.tianxin.Util.Constants.Edit, false);
        startActivity(intent);
    }

    /**
     * ???????????????
     *
     * @param trend
     */
    private void starsetAction(trend trend) {
        videolist video = new videolist();
        video.setId(String.valueOf(trend.getId()));
        video.setBigpicurl(trend.getImage());
        video.setPlaytest(trend.getVideotest());
        video.setPlayurl(trend.getVideo());
        video.setTitle(trend.getTitle());
        video.setUserid(String.valueOf(trend.getUserid()));
        video.setAnum(String.valueOf(trend.getLove()));
        video.setPnum(String.valueOf(trend.getCount()));
        video.setFnum("520");
        video.setMember(trend.getMember());
        video.setTencent(trend.getTencent());
        List<videolist> mlist = new ArrayList<>();
        mlist.add(video);
        activitymyplay.starsetAction(context, mlist, 0, 0, 2);
    }

    /**
     * ????????????????????????Y??????
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
     * ???????????? ??????????????????
     *
     * @param visibility
     */
    public void updateEditTextBodyVisible(int visibility) {
        llComment.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            llComment.requestFocus();
            //????????????
            CommonUtils.showSoftInput(etComment.getContext(), etComment);
        } else if (View.GONE == visibility) {
            //????????????
            CommonUtils.hideSoftInput(etComment.getContext(), etComment);
        }
    }

    /**
     * ?????????????????????
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

        //??????????????????
        for (String s : list) {
            deleName.add(getFileName(s, ".com/"));
        }

        //??????????????????
        if (trend.getTencent() == Constants.TENCENT && !TextUtils.isEmpty(trend.getVideo())) {
            deleName.add(getFileName(trend.getVideo(), ".com/"));
        }

        //??????????????????
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
     * Android ?????????????????????????????????AsyncTask
     */
    private class MyTask extends AsyncTask<Void, Void, info> {
        @Override
        protected info doInBackground(Void... voids) {
            info info = new info();
            info.setAvatar("test");
            return info;
        }

        /**
         * ?????????????????????Android??????????????????????????????????????????????????????
         * 1 ??????Thread???
         * 2 ??????Runnable??????
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
        public void onSuccess(String msg) {
            totalPage--;
            if (totalPage > 1) {
                ToastUtil.toastLongMessage(getString(R.string.eorrtext));
            }

            OnEorr();
        }

        @Override
        public void ToKen(String msg) {
            page3_1.super.paymnets.ToKen(msg);
        }

        @Override
        public void status(int position) {
            switch (position) {
                case 3:
                    //??????
                    McallBack.query(context, callback);
                    break;
                case 4:
                    //??????
                    page3_1.reportUser(context, String.valueOf(member.getId()), getString(R.string.tv_msg161) + trend.getId());
                    break;

            }
        }

        @Override
        public void activity() {
            delete(delposition); //????????????
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




