package com.tencent.opensource.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.model.TRTCLiveRoomDef;
import com.tencent.qcloud.tim.tuikit.live.utils.GlideEngine;

public class dialog_live_item_end extends Dialog implements View.OnClickListener {
    private ImageView image, circleimageview;
    private TextView name, userName;
    private TextView msg;
    private final dialogBottomSheetDialog sheetDialog;
    private TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo;

    public dialog_live_item_end(@NonNull Context context, dialogBottomSheetDialog sheetDialog) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.live_item_end);
        this.sheetDialog = sheetDialog;
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    private void inidate() {
        image = findViewById(R.id.image);
        name = findViewById(R.id.name);
        msg = findViewById(R.id.msg);
        userName = findViewById(R.id.userName);
        circleimageview = findViewById(R.id.circleimageview);
        name.setText(mAnchorInfo.userName);
        msg.setText(mAnchorInfo.userName);
        userName.setText(mAnchorInfo.userName);
        findViewById(R.id.cilicke).setOnClickListener(this);
        GlideEngine.loadImage(circleimageview, mAnchorInfo.avatarUrl);
        GlideEngine.loadImages(image, mAnchorInfo.avatarUrl, 10, 25);
    }

    public void setmAnchorInfo(TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo) {
        this.mAnchorInfo = mAnchorInfo;
        inidate();
    }

    public static void dialogliveend(Context context, TRTCLiveRoomDef.LiveAnchorInfo mAnchorInfo, dialogBottomSheetDialog sheetDialog) {
        dialog_live_item_end dialogLiveItemEnd = new dialog_live_item_end(context, sheetDialog);
        dialogLiveItemEnd.setmAnchorInfo(mAnchorInfo);
        dialogLiveItemEnd.show();

    }

    @Override
    public void dismiss() {
        super.dismiss();
        sheetDialog.ClickListener();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == 1) {
            sheetDialog.ClickListener();
        } else if (id == 2) {
            sheetDialog.LongClickListener();
        } else if (id == R.id.cilicke) {
            dismiss();
        }
    }


    public interface dialogBottomSheetDialog {

        void ClickListener();

        void LongClickListener();

    }
}


