/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/8 0008
 */


package com.tencent.qcloud.costransferpractice.dialog;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tencent.qcloud.costransferpractice.R;
import com.tencent.qcloud.costransferpractice.utils.videotitle;
import com.tencent.qcloud.tim.tuikit.live.component.gift.imp.DefaultGiftAdapter;

import static android.content.Context.CLIPBOARD_SERVICE;

public class dialog_show4 extends BottomSheetDialog {
    private final EditText edittext;                        //标题内容
    private final TextView mytitle;                         //分类标题
    private videotitle svideo;                              //分类对像

    public dialog_show4(@NonNull Context context) {
        super(context);
        setContentView(R.layout.bottomshowetdialog);
        edittext = findViewById(R.id.edittext);
        mytitle = findViewById(R.id.titlesend);
        show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5

        String shareText = getShareText();
        if (!TextUtils.isEmpty(shareText) && shareText.contains(" https://v.douyin.com/")) {
            if (edittext != null) {
                edittext.setText(shareText);
            }
        }
    }

    /**
     * 获取剪切版内容
     *
     * @return 剪切版内容
     */
    public String getShareText() {
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData data = null;
        if (cm != null) {
            data = cm.getPrimaryClip();
        }
        ClipData.Item item = null;
        if (data != null) {
            item = data.getItemAt(0);
        }
        String content = null;
        if (item != null) {
            content = item.getText().toString();
        }
        return content;
    }

}
