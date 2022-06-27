/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/13 0013
 */


package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.adapter.Radapter;
import com.tianxin.getHandler.PostModule;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tianxin.getHandler.JsonUitl;
import com.tencent.opensource.model.getservice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_item_recy_view extends BottomSheetDialog {
    private static final String TAG = "dialog_item_recy_view";
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<Object> list = new ArrayList<>();
    Radapter radapter;
    String getuserid;

    public dialog_item_recy_view(@NonNull Context context, String getuserid) {
        super(context, R.style.fei_style_dialog);
        this.getuserid = getuserid;
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.item_recy_view, null);
        setContentView(inflate);
        ButterKnife.bind(this, inflate);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.5f);
        inflate.setLayoutParams(params);

        // getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        inidivew();
    }

    public static void dialogshow(Context context, String getuserid) {
        dialog_item_recy_view diashow = new dialog_item_recy_view(context, getuserid);
        diashow.show();

    }

    private void inidivew() {
        radapter = new Radapter(getContext(), list, Radapter.dialog_item_recy_view);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(radapter);
        radapter.setPaymnets(new Paymnets() {
            @Override
            public void status(int result) {
                cancel();
                getservice object = (getservice) list.get(result);
                dialog_item_cn1.dialogitemcn1(getContext(), object, 0);
            }
        });

        getdate();
    }

    @OnClick({R.id.back})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                dismiss();
                break;
        }


    }

    public void getdate() {
        PostModule.getModule(BuildConfig.HTTP_API + "/getservice?userid=" + getuserid, new Paymnets() {
            @Override
            public void success(String date) {
                try {
                    List<getservice> getservices = JsonUitl.stringToList(date, getservice.class);
                    list.addAll(getservices);
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

}
