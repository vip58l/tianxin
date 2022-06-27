/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/8 0008
 */


package com.tencent.qcloud.costransferpractice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.costransferpractice.R;
import com.tencent.qcloud.costransferpractice.utils.Constants;
import com.tencent.qcloud.costransferpractice.utils.JsonUitl;
import com.tencent.qcloud.costransferpractice.utils.myadapter;
import com.tencent.qcloud.costransferpractice.utils.videotitle;

import java.util.List;

public class dialog_show3 extends Dialog {
    private RecyclerView recyclerview;
    private com.tencent.qcloud.costransferpractice.utils.myadapter myadapter;

    public dialog_show3(@NonNull Context context, myadapter.showBackCall showBackCall) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        myadapter = new myadapter(getContext(), mygetdata(), showBackCall);
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(myadapter);
    }

    public static dialog_show3 dialoselecttext(Context context, myadapter.showBackCall showBackCall) {
        dialog_show3 dialog_show3 = new dialog_show3(context, showBackCall);
        dialog_show3.show();
        return dialog_show3;
    }

    /**
     * 读取保存的配置信息
     *
     * @return
     */
    public synchronized List<videotitle> mygetdata() {
        SharedPreferences shareInfo = getContext().getSharedPreferences(Constants.USERINFO, 0);
        String json = shareInfo.getString(Constants.THREE, "");
        List<videotitle> data = JsonUitl.stringToList(json, videotitle.class);
        return data;
    }


}