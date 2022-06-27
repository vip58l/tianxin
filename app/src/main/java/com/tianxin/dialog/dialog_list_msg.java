/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/9 0009
 */


package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.Module.Datamodule;
import com.tianxin.adapter.Radapter;
import com.tianxin.adapter.itemdecoration.RecycleViewDivider;
import com.tianxin.Module.api.message;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_list_msg extends BottomSheetDialog {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<Object> list = new ArrayList<>();
    private Radapter radapter;
    private Paymnets paymnets;
    private Datamodule datamodulel;

    public static dialog_list_msg showlistmsg(Context context, Paymnets paymnets) {
        dialog_list_msg dialog_list_msg = new dialog_list_msg(context, paymnets);
        dialog_list_msg.show();
        return dialog_list_msg;
    }

    public dialog_list_msg(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        View inflate = View.inflate(context, R.layout.dialog_list_recyvivew, null);
        setContentView(inflate);
        this.paymnets = paymnets;

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) inflate.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        params.height = (int) (params.width * 1.2f);
        inflate.setLayoutParams(params);
        ButterKnife.bind(this);

        //getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        iniview();
    }

    private void iniview() {
        datamodulel = new Datamodule(getContext());
        datamodulel.dialoglistmsg(paymnets1);

        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(getContext(), LinearLayoutManager.HORIZONTAL, 1, getContext().getResources().getColor(R.color.home3));
        radapter = new Radapter(getContext(), list, Radapter.dialoglistmsg);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(radapter);
        recyclerview.addItemDecoration(recycleViewDivider);
        radapter.setPaymnets(paymnets);


    }

    @OnClick({R.id.close})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
        }
    }

    private Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onSuccess(Object object) {
            List<message> messages = (List<message>) object;
            list.addAll(messages);
            radapter.notifyDataSetChanged();
        }

        @Override
        public void onFail() {

        }
    };

}
