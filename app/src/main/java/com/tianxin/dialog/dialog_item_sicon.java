/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/5/5 0005
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.adapter.Radapter;
import com.tianxin.R;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.navigation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class dialog_item_sicon extends Dialog {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    List<Object> list = new ArrayList<>();
    Radapter radapter;
    Paymnets paymnets;

    public static void dialog_item_sicon(Context context, Paymnets p) {
        dialog_item_sicon dialog_item_sicon = new dialog_item_sicon(context, p);
        dialog_item_sicon.show();

    }

    public dialog_item_sicon(@NonNull Context context, Paymnets p) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.divlog_item_sicon);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        ButterKnife.bind(this);
        paymnets = p;
        inidate();
    }

    public void inidate() {
        radapter = new Radapter(getContext(), list, Radapter.dialog_item_sicon);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(radapter);
        getinidata();

    }

    /**
     * 初始化数据
     */
    private void getinidata() {
        navigation a1 = new navigation();
        navigation a2 = new navigation();
        navigation a3 = new navigation();
        a1.setTitle("发布视频");
        a2.setTitle("立即开播");
        a3.setTitle("上传图片");
        list.add(a1);
        list.add(a2);
        list.add(a3);
        radapter.notifyDataSetChanged();
    }

    public void OnClick(View v) {

    }
}
