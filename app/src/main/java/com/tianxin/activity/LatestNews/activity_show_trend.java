package com.tianxin.activity.LatestNews;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.adapter.itemdecoration.MyDecoration;
import com.tianxin.adapter.Radapter;
import com.tianxin.dialog.dialog_sayhello;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.MTextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.trend;
import com.tencent.opensource.model.trend_comment;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_show_trend extends BasActivity2 {
    private static final String TAG = activity_show_trend.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.tv_title)
    MTextView tv_title;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.pinglun)
    TextView pinglun;
    @BindView(R.id.editinupt)
    EditText editinupt;
    @BindView(R.id.itemback)
    com.tianxin.widget.itembackTopbr itemback;
    private String trendid;
    private String suserid;
    private trend trends;

    public static void starsetAction(Context context, String strend, String suserid) {
        Intent intent = new Intent(context, activity_show_trend.class);
        intent.putExtra(Constants.trendid, strend);
        intent.putExtra(Constants.suserid, suserid);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_show_trend;
    }

    @Override
    public void iniview() {
        Intent intent = getIntent();
        trendid = intent.getStringExtra(Constants.trendid);
        suserid = intent.getStringExtra(Constants.suserid);
        itemback.settitle(getString(R.string.tm149));
        try {
            trends = gson.fromJson(trendid, trend.class);
            trendid = String.valueOf(trends.getId());
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(radapter = new Radapter(context, list, Radapter.item_trend, callback));
            recyclerView.addItemDecoration(new MyDecoration(context, LinearLayoutManager.VERTICAL));
            smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                    loaddate();
                }
            });
            smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                    totalPage = 0;
                    list.clear();
                    radapter.notifyDataSetChanged();
                    loaddate();

                }
            });
            if (!TextUtils.isEmpty(trends.getImage())) {
                String[] arr = trends.getImage().split(",");
                clslist.addAll(Arrays.asList(arr));
                recycler.setLayoutManager(new GridLayoutManager(context, 3));
                recycler.setAdapter(radapter2 = new Radapter(context, clslist, Radapter.IMGRES));
            } else {
                recycler.setVisibility(View.GONE);
            }

            //内容字体设置
            String date = !TextUtils.isEmpty(trends.getDatetime()) ? Config.timestamp(trends.getDatetime()) : "";
            tv_title.setText(trends.getTitle());
            tv_title.setTextSize(15);                                                          //字体大小14
            tv_title.setMaxLine(2);                                                            //超出最大2行隐藏文本
            tv_title.setButtontextColor(context.getResources().getColor(R.color.textCol2));    //全文/收起 字体色
            tv_title.settextColor(context.getResources().getColor(R.color.teal006));           //展示字体色
            tv2.setText(String.format(getString(R.string.tvv_msg257), date));
            editinupt.setFocusable(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        loaddate();
    }

    public void loaddate() {
        totalPage++;
        datamodule.quertrendcomment(totalPage, trendid, paymnets);
    }

    @Override
    @OnClick({R.id.postsend})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.postsend:
                KeyboardUtil.hideSoftInput(activity);
                String conters = editinupt.getText().toString().trim();
                if (TextUtils.isEmpty(conters)) {
                    Toashow.show(getString(R.string.chat_s3));
                    return;
                }
                if (trends == null) {
                    Toashow.show(getString(R.string.chat_s4));
                    return;
                }
                if (userInfo.getUserId().equals(String.valueOf(trends.getUserid()))) {
                    Toashow.show(getString(R.string.chat_s5));
                    return;
                }
                if (userInfo.getState() == 3) {
                    Toashow.show(getString(R.string.chat_s1));
                }
                if (userInfo.getState() != 2) {
                    Toashow.show(getString(R.string.caht_s2));
                    return;
                }
                datamodule.addtrendcomment(String.valueOf(trends.getId()), String.valueOf(trends.getUserid()), conters, paymnets2);

                //IM发送评论 '\r'是回车，'\n'是换行
                dialog_sayhello.senddmessage(String.valueOf(trends.getUserid()), String.format("%s\r\n[评论] %s", trends.getTitle(), conters));

                break;
        }


    }

    @Override
    public void OnEorr() {

    }


    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            List<trend_comment> trend_comments = (List<trend_comment>) object;
            if (trend_comments.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    Toashow.show(getString(R.string.eorrtext));
                }
            }
            if (trend_comments.size() > 0) {
                list.addAll(trend_comments);
                radapter.notifyDataSetChanged();
                pinglun.setText(String.format("%s 条评论", trend_comments.get(0).getCount()));
            }

        }

    };

    private Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess() {
            editinupt.setText(null);
            list.clear();
            totalPage = 0;
            radapter.notifyDataSetChanged();
            loaddate();
        }

    };

    private Callback callback = new Callback() {
        @Override
        public void onFall() {

        }

        @Override
        public void OnClickListener(int position) {
            trend_comment trendComment = (trend_comment) list.get(position);
            datamodule.deletecomment(String.valueOf(trendComment.getId()), new Paymnets() {
                @Override
                public void isNetworkAvailable() {
                    Toashow.show(getString(R.string.eorrfali2));
                }

                @Override
                public void onFail() {
                    Toashow.show(getString(R.string.eorrfali3));

                }

                @Override
                public void onSuccess() {
                    list.remove(position);
                    radapter.notifyItemRemoved(position);
                    radapter.notifyItemRangeChanged(position, radapter.getItemCount());
                    pinglun.setText(String.format("%s条评论", radapter.getItemCount()));
                    editinupt.clearFocus();
                    KeyboardUtil.hideSoftInput(activity);
                }
            });

        }
    };


}