package com.tianxin.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.Util.Toashow;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.Module.api.comment;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.input.InputTextMsgDialog;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 评论列表 BottomSheetDialog可以随意拖动高度
 */
public class dialog_pinglun extends BaseDialog {
    public String TAG = dialog_pinglun.class.getSimpleName();
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.tv_chat)
    TextView tv_chat;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout refreshlayout;
    @BindView(R.id.RecyclerView)
    RecyclerView RecyclerView;
    List<comment> list = new ArrayList<>();
    RecyclerViews adapter;
    private String uid;

    public dialog_pinglun(Context context, String uid) {
        super(context, null);
        this.uid = uid;
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.2f);
        getWindow().setAttributes(params);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.setAdapter(adapter = new RecyclerViews());
        refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                list.clear();
                totalPage = 0;
                loadMoreData();
            }
        });
        refreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshlayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                loadMoreData();
            }
        });
        loadMoreData();
    }

    public void loadMoreData() {
        totalPage++;
        datamodule.comment(totalPage, uid, paymnets);
    }

    @Override
    public int getview() {
        return R.layout.dialog_pinglun;
    }

    @OnClick({R.id.iv_back_btn, R.id.tv_send})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_btn:
                dismiss();
                break;
            case R.id.tv_send:
                String text = tv_chat.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toashow.show("请输入评论内容");
                    return;
                }
                tv_chat.setText("");
                datamodule.addcomment(text, uid, paymnets);

                //dialog_Config.dialoginutp(context, onTextSendDelegate);
                break;
        }
    }

    private class RecyclerViews extends RecyclerView.Adapter<RecyclerViews.sViewHolder> {

        @NonNull
        @Override
        public sViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new sViewHolder(View.inflate(getContext(), R.layout.item_msegs3, null));
        }

        @Override
        public void onBindViewHolder(@NonNull sViewHolder holder, int position) {
            comment comment = list.get(position);
            holder.bind(comment, position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        private class sViewHolder extends RecyclerView.ViewHolder {
            private ImageView icon;
            private TextView tv1, tv2, tv3;

            public sViewHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.icon);
                tv1 = itemView.findViewById(R.id.tv1);
                tv2 = itemView.findViewById(R.id.tv2);
                tv3 = itemView.findViewById(R.id.tv3);
            }

            public void bind(comment comment, int position) {
                try {
                    String path = comment.getTencent() == 1 ? DemoApplication.presignedURL(comment.getPicture()) : comment.getPicture();
                    Picasso.get().load(path).resize(100, 100).into(icon);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv1.setText(!TextUtils.isEmpty(comment.getTvname()) ? comment.getTvname() : comment.getTruename());
                tv2.setText(comment.getMcontent());
                if (userInfo.getUserId().equals(String.valueOf(comment.getUserid()))) {
                    tv3.setVisibility(View.VISIBLE);
                    tv3.setOnClickListener(v -> mydelete(comment, position));
                } else {
                    tv3.setVisibility(View.GONE);
                }

            }
        }
    }

    private InputTextMsgDialog.OnTextSendDelegate onTextSendDelegate = new InputTextMsgDialog.OnTextSendDelegate() {
        @Override
        public void onTextSend(String msg, boolean tanmuOpen) {
            datamodule.addcomment(msg, uid, paymnets);
        }
    };

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onSuccess(Object object) {
            List<comment> comments = (List<comment>) object;
            list.addAll(comments);
            if (comments.size() == 0) {
                totalPage--;
                if (totalPage > 1) {
                    ToastUtil.toastShortMessage(context.getString(R.string.eorrtext));
                }
            }

            if (comments.size() > 0) {
                adapter.notifyDataSetChanged();
                tv.setText(comments.get(0).getMcount() + getContext().getString(R.string.pingluntext));
            }
        }

        @Override
        public void onSuccess() {
            list.clear();
            datamodule.comment(1, uid, this);
        }

    };

    /**
     * 删除评论内容
     *
     * @param comment
     * @param position
     */
    private void mydelete(comment comment, int position) {
        datamodule.delcomment(String.valueOf(comment.getId()), String.valueOf(comment.getUid()), new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                Mesresult mesresult = (Mesresult) object;
                list.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                tv.setText(mesresult.getCode() + getContext().getString(R.string.pingluntext));

            }
        });
    }

    private void mysetContentView() {
        View inflate = View.inflate(context, R.layout.dialog_pinglun, null);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.5f);
        inflate.setLayoutParams(params);
        setContentView(inflate);
    }
}
