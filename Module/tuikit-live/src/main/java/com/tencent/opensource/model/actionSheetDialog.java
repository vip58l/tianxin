/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/29 0029
 */


package com.tencent.opensource.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tencent.qcloud.tim.tuikit.live.component.common.ActionSheetDialog;
import com.tencent.qcloud.tim.tuikit.live.component.report.ReportController;

public class actionSheetDialog extends ActionSheetDialog {
    public String reportUserId;

    public actionSheetDialog(@NonNull final Context context, final String reportUserId) {
        super(context);
        this.reportUserId = reportUserId;
        final ReportController reportController = new ReportController();
        String[] reportItems = reportController.getReportItems();
        int itemColor = context.getResources().getColor(com.tencent.qcloud.tim.tuikit.live.R.color.live_action_sheet_blue);
        addSheetItems(reportItems, itemColor, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which, String text) {
                //观众本人的id mSelfUserId
                String mSelfUserId = UserInfo.getInstance().getUserId(); //举报人的ID
                if (mSelfUserId.equals(reportUserId)) {
                    Toast.makeText(context, "你不能举报自己", Toast.LENGTH_LONG).show();
                    return;
                }
                reportController.reportUser(mSelfUserId, reportUserId, text, reportUserId, 1);
            }
        });


    }

    public static void actionSheetshow(Context context, String reportUserId) {
        actionSheetDialog actionSheetDialog = new actionSheetDialog(context, reportUserId);
        actionSheetDialog.builder();
        actionSheetDialog.setCancelable(false);
        actionSheetDialog.show();
    }


    public actionSheetDialog(@NonNull final Context context, final String reportUserId, final String video) {
        super(context);
        this.reportUserId = reportUserId;
        final ReportController reportController = new ReportController();
        String[] reportItems = reportController.getReportItems();
        int itemColor = context.getResources().getColor(com.tencent.qcloud.tim.tuikit.live.R.color.live_action_sheet_blue);
        addSheetItems(reportItems, itemColor, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which, String text) {
                //观众本人的id mSelfUserId
                String mSelfUserId = UserInfo.getInstance().getUserId(); //举报人的ID
                if (mSelfUserId.equals(reportUserId)) {
                    Toast.makeText(context, "你不能举报自己", Toast.LENGTH_LONG).show();
                    return;
                }
                reportController.reportUser(mSelfUserId, reportUserId, text, video, 1);
            }
        });


    }

    public static void actionSheetshow(Context context, String reportUserId, String video) {
        actionSheetDialog actionSheetDialog = new actionSheetDialog(context, reportUserId, video);
        actionSheetDialog.builder();
        actionSheetDialog.setCancelable(false);
        actionSheetDialog.show();
    }
}
