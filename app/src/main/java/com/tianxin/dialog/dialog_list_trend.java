package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BaseBottomSheetDialog;
import com.tianxin.R;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.trend;
import com.tencent.opensource.model.trend_comment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_list_trend extends BaseBottomSheetDialog {
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_chat)
    EditText tv_chat;
    @BindView(R.id.tv_send)
    TextView tv_send;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.RecyclerView)
    RecyclerView recyclerView;
    int totalPage = 0;
    int uid = 0;
    Radapter radapter;
    trend trends;

    public static void show(Context context, int uid, int userid) {
        dialog_list_trend dialogListTrend = new dialog_list_trend(context, uid, userid);
        dialogListTrend.show();
    }

    public dialog_list_trend(Context context, int uid, int userid) {
        super(context);
        this.uid = uid;
        this.trends = new trend();
        trends.setId(uid);
        trends.setUserid(userid);
        radapter = new Radapter(context, objectList, Radapter.IMG22, callback);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(radapter);
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                objectList.clear();
                totalPage = 0;
                radapter.notifyDataSetChanged();
                loadMoreData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败
                loadMoreData();
            }
        });
        tv_chat.setFocusable(true);
        loadMoreData();
    }

    private void loadMoreData() {
        totalPage++;
        datamodule.quertrendcomment(totalPage, String.valueOf(uid), paymnets);
    }

    @Override
    protected int layoutview() {
        return R.layout.dialog_pinglun;
    }

    @Override
    @OnClick({R.id.tv_send,R.id.iv_back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                //隐藏软键盘
                KeyboardUtil.hideSoftInput(context, tv_chat);
                String tvchat = tv_chat.getText().toString().trim();
                if (TextUtils.isEmpty(tvchat)) {
                    Toashow.show("请输入评论内容");
                } else {
                    if (UserInfo.getInstance().getUserId().equals(String.valueOf(trends.getUserid()))) {
                        Toashow.show("你不能评论自己的内容");
                        return;
                    }
                    if (UserInfo.getInstance().getState() == 3) {
                        Toashow.show("封号无权发表评论");
                        return;
                    }
                    if (UserInfo.getInstance().getState() != 2) {
                        Toashow.show("未认证无法发表评论");
                        return;
                    }

                    //提交评论内容
                    datamodule.addtrendcomment(String.valueOf(trends.getId()), String.valueOf(trends.getUserid()), tvchat, paymnets2);
                }
                break;
            case R.id.iv_back_btn:
                dismiss();
                break;

        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            List<trend_comment> trend_comments = (List<trend_comment>) object;
            objectList.addAll(trend_comments);
            radapter.notifyDataSetChanged();
            tv.setText(String.format(context.getString(R.string.tm151) + "", trend_comments.get(0).getCount()));
            if (trend_comments.size() > 0) {
                refreshlayout.setEnableRefresh(true);
            } else {
                refreshlayout.setEnableRefresh(false);
            }
            refreshlayout.finishRefresh();
        }

        @Override
        public void onFail() {

        }

        @Override
        public void isNetworkAvailable() {

        }
    };

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(context.getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(context.getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess() {
            tv_chat.setText(null);
            objectList.clear();
            totalPage = 0;
            radapter.notifyDataSetChanged();
            loadMoreData();
        }

    };

    private Callback callback = new Callback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFall() {

        }

        @Override
        public void OndeleteListener(int position) {
            trend_comment trendComment = (trend_comment) objectList.get(position);
            datamodule.deletecomment(String.valueOf(trendComment.getId()), new Paymnets() {
                @Override
                public void isNetworkAvailable() {
                    Toashow.show(context.getString(R.string.eorrfali2));
                }

                @Override
                public void onFail() {
                    Toashow.show(context.getString(R.string.eorrfali3));
                }

                @Override
                public void onSuccess() {
                    objectList.remove(position);
                    radapter.notifyItemRemoved(position);
                    radapter.notifyItemRangeChanged(position, radapter.getItemCount());
                    tv.setText(String.format(context.getString(R.string.tm151) + "", radapter.getItemCount()));
                }

            });
        }
    };


}
