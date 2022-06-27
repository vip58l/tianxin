/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/17 0017
 */


package com.tianxin.Fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.Module.api.serviceaccount;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tencent.opensource.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class fragment_presentation extends Fragment {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<Object> list = new ArrayList<>();
    private Radapter radapter;
    private String getuserid;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getuserid = arguments.getString(Constants.USERID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_list_item_it, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        radapter = new Radapter(getContext(), list, Radapter.fragment_presentation);
        LinearLayoutManager manager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter);

        //失败获取了焦点
        //取消recyclerview单独的滑动效果
        recyclerview.setFocusable(false);
        recyclerview.setHasFixedSize(true);
        recyclerview.setNestedScrollingEnabled(false);
        inidate();
    }

    private void inidate() {
        UserInfo userInfo = UserInfo.getInstance();
        String path = getuserid.equals(userInfo.getUserId()) ? "&token=" + userInfo.getToken() : "";
        PostModule.getModule(BuildConfig.HTTP_API + "/serviceaccount?userid=" + getuserid + path, new Paymnets() {
            @Override
            public void success(String date) {
                List<serviceaccount> serviceaccounts = JsonUitl.stringToList(date, serviceaccount.class);
                list.addAll(serviceaccounts);
                radapter.notifyDataSetChanged();
                if (radapter.getItemCount() == 0) {
                    recyclerview.setVisibility(View.GONE);
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }

    public static Fragment frpresentation(String userid) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USERID, userid);
        fragment_presentation fr = new fragment_presentation();
        fr.setArguments(bundle);
        return fr;
    }
}
