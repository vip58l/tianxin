/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/28 0028
 */

package com.tianxin.Fragment.page1.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.adapter.setAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.item;
import com.tencent.opensource.model.member;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Banner;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;


import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * 会员列表 交友列表展示
 */
public class one2 extends BasFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.listview)
    public ListView listview;
    public String TAG = one2.class.getName();
    private boolean isVisibleToUser;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE);
        if (isVisibleToUser && list.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (smartRefreshLayout!=null){
                        smartRefreshLayout.autoRefresh();
                    }

                }
            }, 200);
        }
    }

    public static Fragment show(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        one2 on2e = new one2();
        on2e.setArguments(bundle);
        return on2e;
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_one_main, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        toactivity(position);
    }

    /**
     * 转到个人主页
     *
     * @param position
     */
    private void toactivity(int position) {
        item item = type == 1 ? list.get(position - 1) : list.get(position);
        member member = (member) item.object;
        if (member == null) {
            paymnets.onFail();
            return;
        }
        if (!Config.isNetworkAvailable()) {
            paymnets.isNetworkAvailable();
            return;
        }
        activity_picenter.setActionactivity(context, String.valueOf(member.getId()));
    }


    /**
     * 转到个人主页
     *
     * @param position
     */
    private void toactivitypicenter(int position) {
        item item = type == 1 ? list.get(position - 1) : list.get(position);
        member member = (member) item.object;
        if (member == null) {
            paymnets.onFail();
            return;
        }
        if (!Config.isNetworkAvailable()) {
            paymnets.isNetworkAvailable();
            return;
        }

        //戳一下TA聊聊吗
        datamodule.getNotmessage(String.valueOf(member.getId()), new Paymnets() {
            @Override
            public void onSuccess() {
                Toashow.show(String.format(getString(R.string.tm95) + "", member.getTruename()));
                activity_picenter.setActionactivity(context, String.valueOf(member.getId()));
            }

            @Override
            public void onFail(String msg) {
                Toashow.show(msg);
            }
        });

    }


    /**
     * 转到对应用户聊天界面
     */
    private void startChatActivity(int position) {
        item item = type == 1 ? list.get(position - 1) : list.get(position);
        member member = (member) item.object;
        if (member == null) {
            paymnets.onFail();
            return;
        }

        if (!Config.isNetworkAvailable()) {
            paymnets.isNetworkAvailable();
            return;
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(String.valueOf(member.getId()));
        chatInfo.setChatName(member.getTruename());
        chatInfo.setIconUrlList(TextUtils.isEmpty(member.getPicture()) ? "" : member.getPicture());
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getdate() {
        totalPage++;
        datamodule.one2list(totalPage, mapLocation, type, paymnets);
    }

    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                getdate();
                break;
        }
    }

    @Override
    public void iniview() {
        setAdapter = new setAdapter(context, list, type);
        setAdapter.setPaymnet(paymnets);
        listview.setAdapter(setAdapter);
        listview.setOnItemClickListener(this);
        listview.setDivider(new ColorDrawable(getResources().getColor(R.color.home3)));
        listview.setDividerHeight(1);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                if (banner != null) {
                    banner.OnRefreshL();
                }
                totalPage = 0;
                list.clear();
                setAdapter.notifyDataSetChanged();
                getdate();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                getdate();
            }
        });
    }

    @Override
    public void initData() {
        if (isVisibleToUser && list.size() == 0) {
            showDialogFrag();
        }
        if (type == Constants.TENCENT) {
            banner = new Banner(context);
            //首页插入滚动图片banner
            listview.addHeaderView(banner);
        }
        if (!userInfo.isPermission()) {
            ActivityLocation.checkpermissions(getActivity());
        }

        GPSAMapLocation();
    }


    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
        dismissDialogFrag();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsto.setPgs(1);
        GPSAMapLocation();
    }

    /**
     * 初始化定位
     */
    public void GPSAMapLocation() {
        if (mapLocation == null) {
            /**
             * 初始化定位
             */
            lbsamap.getmyLocation(callback);
        } else {
            //刷新定位地址
            setAdapter.setSamapLocation(mapLocation);
        }
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        public void onFail() {
            try {
                ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }

        }

        @Override
        public void status(int position) {
            //转到个人聊天窗口页
            //startChatActivity(type == 1 ? position + 1 : position);

            //转到个人主页
            toactivitypicenter(type == 1 ? position + 1 : position);


        }

        @Override
        public void onSuccess(Object object) {
            List<member> members = (List<member>) object;
            for (member member : members) {
                item item = new item();
                item.type = com.tianxin.adapter.setAdapter.getOne2;
                item.object = member;
                if (member != null) {
                    list.add(item);
                }
            }
            setAdapter.notifyDataSetChanged();
            OnEorr();

        }

        @Override
        public void onError() {
            try {
                totalPage--;
                ToastUtil.toastShortMessage(getString(R.string.eorrtext));
                OnEorr();
            } catch (Exception e) {
                e.getMessage();
            }


        }

        @Override
        public void ToKen(String msg) {
            totalPage--;
            one2.super.paymnets.ToKen(msg);
            OnEorr();
        }

    };

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (banner != null) {
            banner.onStart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (banner != null) {
            banner.onDestroy();
        }
    }


}
