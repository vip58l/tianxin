package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.Module.api.videotitle;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.costransferpractice.utils.JsonUitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_follow_view extends BottomSheetDialog {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    Radapter radapter;
    List<Object> list = new ArrayList<>();
    UserInfo userInfo;
    //https://www.cnblogs.com/huameitang/p/8707458.html
    Map<String, String> params = new HashMap<String, String>();

    public dialog_follow_view(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_follow_view);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        iniview();
        inidate();
    }

    private void iniview() {
        ButterKnife.bind(this);
        userInfo = UserInfo.getInstance();
        radapter = new Radapter(getContext(), list, Radapter.dialog_follow_view);
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerview.setAdapter(radapter);
        radapter.setPaymnets(new spaymnets());
    }

    public static void mydialog(Context context) {
        dialog_follow_view dialogFollowView = new dialog_follow_view(context);
        dialogFollowView.setCanceledOnTouchOutside(false);
        dialogFollowView.show();
    }

    @OnClick({R.id.close, R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.senbnt:
                dateview();
                break;
        }
    }

    public void inidate() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getContext().getString(R.string.eorrfali2));
            return;
        }
        PostModule.getModule(BuildConfig.HTTP_API + "/videotitle?type=1&userid=" + userInfo.getUserId(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<videotitle> data = JsonUitl.stringToList(date, videotitle.class);
                    list.addAll(data);
                    radapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });
    }


    /**
     * 回调接口监听
     */
    class spaymnets implements Paymnets {
        @Override
        public void status(int position) {
            videotitle videotitle = (videotitle) list.get(position);
            if (videotitle.getSelect() == 1) {
                videotitle.setSelect(0);
                params.remove(String.valueOf(videotitle.getId()));
            } else {
                videotitle.setSelect(1);
                //params.put(String.valueOf(videotitle.getId()), String.valueOf(videotitle.getId()));
                for (Object o : list) {
                    videotitle vs = (videotitle) o;
                    if (vs.getSelect() == 1) {
                        params.put(String.valueOf(vs.getId()), String.valueOf(vs.getId()));
                    }
                }
            }
            radapter.notifyDataSetChanged();
        }

    }


    /**
     * 提交到服务端去
     */
    private void dateview() {
        if (params == null) {
            Toashow.show(getContext().getString(R.string.eorrfali3));
            return;
        }
        if (params.size() == 0) {
            Toashow.show(getContext().getString(R.string.tv_msg32));
            return;
        }
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            i++;
            if (i < params.size()) {
                sb.append("id[]=" + params.get(key) + "&");
            } else {
                sb.append("id[]=" + params.get(key));
            }
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getContext().getString(R.string.eorrfali2));
            return;
        }
        PostModule.getModule(BuildConfig.HTTP_API + "/videotypeadd?userid=" + userInfo.getUserId() + "&token=" + userInfo.getToken() + "&" + sb.toString(), new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
                    Toashow.show(mesresult.getMsg());
                    if (mesresult.getStatus().equals("1")) {
                        dialog_follow_view.this.dismiss();
                        userInfo.setVideotype(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fall(int code) {

            }
        });

    }

}
