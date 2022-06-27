package com.tencent.qcloud.costransferpractice.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tencent.qcloud.costransferpractice.R;
import com.tencent.qcloud.costransferpractice.common.base.BaseModeul;
import com.tencent.qcloud.costransferpractice.utils.myadapter;
import com.tencent.qcloud.costransferpractice.utils.videotitle;

public class dialog_show1 extends BottomSheetDialog implements View.OnClickListener {

    private EditText edittext;
    private TextView mytitle;
    private TextView btn_left;
    private TextView btn_right;
    private LinearLayout fenle;
    private BaseModeul baseModeul;
    private dialog_show3 dialogShow3;

    public static void myshow(Context context, BaseModeul baseModeul) {
        dialog_show1 dialogshow = new dialog_show1(context, baseModeul);
        dialogshow.show();
    }

    public dialog_show1(@NonNull Context context, BaseModeul baseModeul) {
        super(context);
        this.baseModeul = baseModeul;
        setContentView(R.layout.bottomshowetdialog);
        edittext = findViewById(R.id.edittext);
        mytitle = findViewById(R.id.titlesend);
        fenle = findViewById(R.id.fenle);
        baseModeul.setSvideo(baseModeul.getvideotitle() == null ? baseModeul.mygetdata().get(0) : baseModeul.getvideotitle());
        edittext.setText(baseModeul.getClipContent(context));
        mytitle.setText(baseModeul.getSvideo().getTitle());
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);

        fenle.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5

        String shareText = baseModeul.getClipContent(getContext());
        if (!TextUtils.isEmpty(shareText) && shareText.contains(" https://v.douyin.com/")) {
            if (edittext != null) {
                edittext.setText(shareText);
            }
        }
    }

    public void etstring() {
        String conter = edittext.getText().toString();
        if (!TextUtils.isEmpty(conter)) {
            baseModeul.setTitle(conter);
            dismiss();
            baseModeul.getUploadActivity().upload();  //执行上传文件
        } else {
            baseModeul.getUploadActivity().toastMessage(getContext().getString(R.string.tv_udate_ok2));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_left) {
            etstring();

        }
        if (id == R.id.btn_right) {
            dismiss();
        }
        if (id == R.id.fenle) {
            dialogShow3 = dialog_show3.dialoselecttext(getContext(), showBackCall);

        }
    }

    private myadapter.showBackCall showBackCall = new myadapter.showBackCall() {
        @Override
        public void videotitle(videotitle video) {
            mytitle.setText(video.getTitle());
            baseModeul.addvideotitle(video);//保存分类信息
            dialogShow3.dismiss();
        }
    };
}
