/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/28 0028
 */

package com.tianxin.Fragment.page1.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.api.moneylist;
import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.activity_svip;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.adapter.Radapter;
import com.tianxin.amap.lbsamap;
import com.tianxin.dialog.dialog_show;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;
import com.tianxin.wxapi.WXpayObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.opensource.model.member;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员列表首页 交友列表展示
 */
public class one4 extends BasFragment {
    private static final String TAG = one4.class.getName();
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private cs_alipay csAlipay;

    /**
     * @param date_type 请类分类
     * @param style     模板样式
     * @return
     */
    public static Fragment show(int date_type, int style) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, date_type);
        bundle.putInt(Constants.style, style);
        one4 one4 = new one4();
        one4.setArguments(bundle);
        return one4;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        datamodule = new Datamodule(context, paymnets);
        type = arguments.getInt(Constants.TYPE);
        style = arguments.getInt(Constants.style);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Bundle arguments = getArguments();
        if (arguments != null) {
            datamodule = new Datamodule(context, paymnets);
            type = arguments.getInt(Constants.TYPE);
            style = arguments.getInt(Constants.style);
        }

        if (isVisibleToUser && list2.size() == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (smartRefreshLayout != null) {
                        smartRefreshLayout.autoRefresh();
                    }

                }
            }, 200);
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home_nain, null);
    }

    @Override
    public void iniview() {
        csAlipay = new cs_alipay(context, play);
        switch (style) {
            case 1:
                style = Radapter.item_video_show1;
                break;
            case 2:
                style = Radapter.item_video_show2;//同城     方格模板
                break;
            case 3:
                style = Radapter.item_video_show3;//附近人
                break;
            case 4:
                style = Radapter.item_video_show4;//视频聊天
                break;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(radapter = new Radapter(context, list2, style, callvideo));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh(100/*,false*/);//传入false表示刷新失败
                totalPage = 0;
                list2.clear();
                radapter.notifyDataSetChanged();
                loadMoreData();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                loadMoreData();
            }
        });
        if (!userInfo.isPermission()) {
            ActivityLocation.checkpermissions(getActivity());
        }
        if(ActivityLocation.checkPermissions(getActivity())) {
            lbsamap.getmyLocation(callback);
        }

    }

    @Override
    public void initData() {
        if (mlist.size() == 0) {
            mlist.add(Manifest.permission.CAMERA);                            //相机权限
            mlist.add(Manifest.permission.RECORD_AUDIO);                      //SD卡写入
        }
        if (getUserVisibleHint() && list2.size() == 0) {
            showDialogFrag();
        }
    }

    public void loadMoreData() {
        totalPage++;
        datamodule.one2list(totalPage, mapLocation, type, paymnets1);
        datamodule.getbalance(balance);   //获取金币
        datamodule.getallcharge(charge); //聊天聊天配置
    }

    @Override
    @OnClick({R.id.eorr})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.eorr:
                totalPage = 0;
                smartRefreshLayout.autoRefresh();
                break;
        }
    }

    @Override
    public void OnEorr() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setVisibility(list2.size() > 0 ? View.VISIBLE : View.GONE);
        }
        dismissDialogFrag();
    }

    @Override
    public void onRefresh() {

    }

    /**
     * 点击通话回调
     */
    private Callback callvideo = new Callback() {
        @Override
        public void OnClickListener(int position) {
            member = (member) list2.get(position);
            if (false) {
                if (getres()) {
                    return;
                }
                //选判断是否已拿到权限
                if (requestPermissions()) {
                    openvideo();
                }
            } else {
                activity_picenter.setActionactivity(context, String.valueOf(member.getId()));
            }
        }
    };

    private boolean getres() {

        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getContext().getResources().getString(R.string.eorrfali2));
            return true;
        }

        //金币小于50个币 无法拨打视频或语音通话或我不是【主播1】
        if (userInfo.getJinbi() < allcharge.getVideo() && userInfo.gettRole() == 0) {
            dialog_show.dialogshow(context, null, play);
            return true;
        }
        return false;

    }

    private Paymnets paymnets1 = new Paymnets() {
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
        public void onSuccess(Object object) {
            List<member> members = (List<member>) object;
            list2.addAll(members);
            radapter.notifyDataSetChanged();
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
            one4.super.paymnets.ToKen(msg);
        }


        @Override
        public void returnltonItemClick(Object object, int TYPE) {
            switch (TYPE) {
                case 1:
                    info = (com.tencent.opensource.model.info) object;
                    break;
                case 2:
                    allcharge = (Allcharge) object;
                    break;
            }


        }
    };

    private Paymnets balance = new Paymnets() {
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
        public void onSuccess(Object object) {
            info = (com.tencent.opensource.model.info) object;
            userInfo.setJinbi(info.getMoney());
        }
    };

    private Paymnets charge = new Paymnets() {
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
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
        }
    };

    private Paymnets play = new Paymnets() {
        @Override
        public void onFail() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });
        }

        @Override
        public void activity(String msg) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, msg);
                }
            });
        }

        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess: ");
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            switch (TYPE) {
                case activity_svip.zfb:
                    //发起支付请求
                    csAlipay.Paymoney(moneylist);
                    break;
                case activity_svip.wx:
                    //发起微信支付
                    WXpayObject.getsWXpayObject().WXpay(moneylist);
                    break;
            }
        }

    };

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
            radapter.setaMapLocation(mapLocation);
        }
    }


    /**
     * 申请权限
     */
    private boolean requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : mlist) {
                int star = ContextCompat.checkSelfPermission(getContext(), permission);
                if (star != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(mlist.toArray(new String[mlist.size()]), Constants.requestCode);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ActivityLocation.OPEN_SET_REQUEST_CODE:
                permissionsto.setPgs(1);
                if (ActivityLocation.checkPermissions(getActivity())){
                    lbsamap.getmyLocation(callback);
                }

                break;
            case Constants.requestCode:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    for (String permission : mlist) {
                        int star = ContextCompat.checkSelfPermission(getContext(), permission);
                        if (star != PackageManager.PERMISSION_GRANTED) {
                            Toashow.show(getString(R.string.ts_ss_aa1));
                            return;
                        }
                    }
                }
                //打开视频通话
                openvideo();
                break;

        }

    }

    /**
     * 拨打视频
     */
    private void openvideo() {
        if ((member != null && allcharge.getVideo() > 0 && info.getMoney() > 0) || userInfo.gettRole() == 1) {
            datamodule.searchContactsByPhone(String.valueOf(member.getId()), 2, member);
        } else {
            dialog_show.dialogshow(context, member, play);
        }

    }

}
