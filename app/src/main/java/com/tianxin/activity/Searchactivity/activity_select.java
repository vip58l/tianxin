package com.tianxin.activity.Searchactivity;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.Test.MyOpenhelper;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.adapter.Radapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Dmselect;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 搜索好友
 */
public class activity_select extends BasActivity2 {
    private static final String TAG = activity_select.class.getSimpleName();
    public List<Object> searchlist = new ArrayList<>();
    public List<Object> rss = new ArrayList<>();
    @BindView(R.id.rsearch)
    RecyclerView rsearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rclose)
    TextView rclose;
    @BindView(R.id.dmselect)
    Dmselect dmselect;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.r1)
    RecyclerView r1;
    @BindView(R.id.hanguptip)
    LinearLayout hanguptip;
    @BindView(R.id.hangup)
    LinearLayout hangup;
    Radapter lradapter;

    public static void setAction(Context context) {
        context.startActivity(new Intent(context, activity_select.class));
    }

    /**
     * 监听事件回调处理
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            activity_select.super.paymnets.ToKen(msg);
        }

        @Override
        public void onSuccess(Object object) {
            List<member> members = (List<com.tencent.opensource.model.member>) object;
            list.addAll(members);
            radapter.notifyDataSetChanged();
        }

        @Override
        public void onSuccess(Object object, int type) {
            hanguptip.setVisibility(View.VISIBLE);
            hangup.setVisibility(View.GONE);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (rss.size() > 0) {
                        rss.clear();
                        lradapter.notifyDataSetChanged();
                    }
                    List<member> members = (List<member>) object;
                    rss.addAll(members);
                    r1.setLayoutManager(new LinearLayoutManager(context));
                    r1.setAdapter(lradapter = new Radapter(context, rss, Radapter.activity_t, new Callback() {
                        @Override
                        public void OnClickListener(int position) {
                            //详情页
                            member m = (com.tencent.opensource.model.member) rss.get(position);
                            activity_picenter.setActionactivity(context, String.valueOf(m.getId()));
                        }

                        @Override
                        public void OndeleteListener(int position) {
                            //chat聊天
                            member member = (com.tencent.opensource.model.member) rss.get(position);
                            startChatActivity(member);
                        }
                    }));
                }
            });
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onFail(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void search(String text) {

            if (TextUtils.isEmpty(text)) {
                Toashow.show(getString(R.string.selectaa));
                return;
            }
            //搜索结果回调展示
            try {
                //saver保存本地数据库sql
                searchlist.add(text);
                Collections.reverse(searchlist);
                radapter2.notifyDataSetChanged();

                ContentValues cValue = new ContentValues();
                cValue.put("title", text);
                cValue.put("type", 1);
                openhelper.insert(MyOpenhelper.search, cValue);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (searchlist.size() > 10) {
                searchlist.remove(0);
                try {
                    //超过了10条记录 删除较早旧数据
                    List<String> query = openhelper.Query(MyOpenhelper.search, 1);
                    //Collections.reverse(searchlist);
                    if (query.size() > 10) {
                        for (int i = 0; i < (query.size() - 10); i++) {
                            openhelper.delete(MyOpenhelper.search, query.get(i));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            rclose.setVisibility(View.VISIBLE);
            rclose.animate().alpha(1f).setDuration(200).start();
            footer.setVisibility(View.GONE);

            //搜索结果
            datamodule.selaercch(text, this);
        }

        @Override
        public void payonItemClick(String date, int TYPE) {
            dmselect.setEditselect(date);

        }

    };


    @Override
    protected int getview() {
        return R.layout.activity_select;
    }

    @Override
    public void iniview() {
        recyclerview.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerview.setAdapter(radapter = new Radapter(context, list, Radapter.activity_select));
        dmselect.setPaymnets(paymnets); //设置监听搜索事件

        List<String> array = openhelper.Query(MyOpenhelper.search, 1);
        if (array.size() > 0) {
            searchlist.addAll(array);
            Collections.reverse(searchlist);
            rclose.setVisibility(View.VISIBLE);
            rclose.animate().alpha(1f).setDuration(200).start();
        }
        rsearch.setLayoutManager(new LinearLayoutManager(context));
        rsearch.setAdapter(radapter2 = new Radapter(context, searchlist, Radapter.search1, new Callback() {
            @Override
            public void OnClickListener(int position) {

                //搜索结果
                String text = searchlist.get(position).toString();
                Toashow.show(text);
                datamodule.selaercch(text, paymnets);

            }
            @Override
            public void onFall() {

            }
        }));
    }

    @Override
    public void initData() {
        datamodule.activitselect(paymnets);
    }

    @Override
    @OnClick({R.id.restart, R.id.rclose, R.id.lclose})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.restart:
                toradapter();
                break;
            case R.id.rclose:
                footer.setVisibility(View.VISIBLE);
                openhelper.delete(MyOpenhelper.search);
                rclose.animate().alpha(0f).setDuration(200).start();

                searchlist.clear();
                radapter2.notifyDataSetChanged();
                break;
            case R.id.lclose:
                rss.clear();
                lradapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    private void toradapter() {
        list.clear();
        radapter.notifyDataSetChanged();
        initData();
    }

    /**
     * 这里需要用户上传头像才能聊天
     */
    private void startChatActivity(member member) {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg132));
            return;
        }
        if (userInfo.getSex().equals(String.valueOf(member.getSex()))) {
            Toashow.show(getString(R.string.picerter));
            return;
        }
        ChatActivity.setAction(context, member);
    }

}