/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/28 0028
 */

package com.tianxin.Fragment.page1.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.utils.AES.Resultinfo;
import com.tianxin.adapter.setAdapter;
import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.item;
import com.tianxin.R;
import com.tianxin.activity.activity_home_page;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.amap.lbsamap;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_item_Avatar;
import com.tianxin.getHandler.PostModule;
import com.tianxin.widget.Banner;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import static com.tianxin.getHandler.JsonUitl.stringToList;

/**
 * 会员列表 心理咨询
 */
public class one3 extends BasFragment implements AdapterView.OnItemClickListener, Paymnets {
    @BindView(R.id.smartRefreshLayout)
    public SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.listview)
    public ListView listview;
    private final String TAG = one3.class.getName();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        type = arguments.getInt(Constants.TYPE);
        userInfo = UserInfo.getInstance();
        if (isVisibleToUser && list.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    smartRefreshLayout.autoRefresh();
                }
            }, 100);
        }
    }

    public static Fragment typeshow(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        one3 one3 = new one3();
        one3.setArguments(bundle);
        return one3;
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_one_main, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        item item = type == 1 ? list.get(position - 1) : list.get(position);
        member member = (member) item.object;
        if (member == null) {
            return;
        }
        Intent intent = new Intent(getContext(), activity_home_page.class);
        intent.putExtra(Constants.USERID, String.valueOf(member.getId()));
        startActivity(intent);
    }

    /**
     * 联网获取数据
     */
    private void getdate() {
        try {
            if (!Config.isNetworkAvailable()) {
                ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                oneorr();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String province = "";
        String city = "";
        String district = "";
        if (mapLocation != null) {
            province = mapLocation.getProvince();
            city = mapLocation.getCity();
            district = mapLocation.getDistrict();
        }

        if (TextUtils.isEmpty(userInfo.getSex())) {
            Log.d(TAG, "logout: one3");
            BaseActivity.logout(getContext());
            return;

        }
        String sex = !TextUtils.isEmpty(userInfo.getSex()) ? userInfo.getSex().equals("1") ? "2" : "1" : "1";
        totalPage++;
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", userInfo.getUserId())
                .add("page", String.valueOf(totalPage))
                .add("sex", sex)
                .add("province", province)
                .add("city", city)
                .add("district", district)
                .add("type", String.valueOf(type))
                .add("token", userInfo.getToken())
                .build();
        PostModule.postModule(Webrowse.memberlist, requestBody, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    String decrypt = Resultinfo.decrypt(date);
                    List<member> members = stringToList(decrypt, member.class);
                    if (members.size() == 0) {
                        totalPage--;
                        if (totalPage > 1) {
                            ToastUtil.toastShortMessage(getString(R.string.eorrtext));
                        }
                        oneorr();
                        return;
                    }
                    for (member member : members) {
                        item item = new item();
                        item.type = com.tianxin.adapter.setAdapter.getOne3;
                        item.object = member;
                        if (member != null) {
                            list.add(item);
                        }
                    }
                    setAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                oneorr();
            }

            @Override
            public void fall(int code) {
                oneorr();
            }
        });
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
        setAdapter = new setAdapter(context, list, type, this);
        listview.setAdapter(setAdapter);
        listview.setDividerHeight(0);
        listview.setOnItemClickListener(this);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
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
                if (!Config.isNetworkAvailable()) {
                    ToastUtil.toastShortMessage(getString(R.string.eorrfali2));
                    return;
                }
                getdate();
            }
        });
    }

    @Override
    public void initData() {
        if (type == Constants.TENCENT) {
            banner = new Banner(getContext());
            listview.addHeaderView(banner);
        }
        //刷新定位服务
        if (mapLocation == null) {
            lbsamap.getmyLocation(callback);
        } else {
            setAdapter.setSamapLocation(mapLocation);
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(Object object) {
        member member = (com.tencent.opensource.model.member) object;
        if (member != null) {
            startChatActivity(member);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (banner != null) {
            banner.onPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (banner != null) {
            banner.onStart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 销毁");
        if (banner != null) {
            banner.onPause();
        }
    }

    /**
     * 错误展示
     */
    private void oneorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * 私聊
     *
     * @param member
     */
    private void startChatActivity(member member) {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg82));
            return;
        }
        //这里需要用户上传头像才能聊天
        if (TextUtils.isEmpty(userInfo.getAvatar())) {
            dialog_item_Avatar.dialogitemmsgpic(getContext());
            return;
        }
        String getuserid = String.valueOf(member.getId());
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(getuserid);
        chatInfo.setChatName(member.getTruename());
        Intent intent = new Intent(DemoApplication.instance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        DemoApplication.instance().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.sussess) {
            setAdapter.setSamapLocation(mapLocation);
        }

    }
}
