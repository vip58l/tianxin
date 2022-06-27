package com.tianxin.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.listener.Paymnets;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 视频或图片选择
 */
public class dialog_new_item extends BaseDialog {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    public static void myshow(Context context, List<Object> list, Paymnets paymnets) {
        dialog_new_item dialog_new_item = new dialog_new_item(context, paymnets);
        dialog_new_item.init(list);
        dialog_new_item.show();
    }

    public dialog_new_item(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

    }

    private void init(List<Object> list) {
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(new Radapter(context, list, Radapter.dialog_item_show_home, callback));
    }

    @Override
    public int getview() {
        return R.layout.dialog_new_item_ress;
    }

    @Override
    @OnClick(R.id.close)
    public void OnClick(View v) {
        dismiss();
    }

    private Callback callback = new Callback() {
        @Override
        public void OnClickListener(int position) {
            dismiss();
            if (paymnets != null) {
                paymnets.status(position);
            }

        }

        @Override
        public void onFall() {

        }
    };
}
