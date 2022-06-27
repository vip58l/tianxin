package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.zxing.common.BitmapUtils;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.getHandler.Webrowse;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.personal;

public class dialog_item_qrcode extends Dialog {
    ImageView image, qrecode, circleimageview;
    TextView name, age, sxyh;
    member member;

    public dialog_item_qrcode(@NonNull Context context, member member) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item_qrcode);
        this.member = member;
        findViewById(R.id.back).setOnClickListener(v -> dismiss());

        image = findViewById(R.id.image);
        circleimageview = findViewById(R.id.circleimageview);
        qrecode = findViewById(R.id.qrecode);
        name = findViewById(R.id.name);
        sxyh = findViewById(R.id.sxyh);
        age = findViewById(R.id.age);

        Glideload.loadImage(image, member.getPicture(), 6);
        Glideload.loadImage(circleimageview, member.getPicture());
        name.setText(member.getTruename());
        setqrcode(Webrowse.register + "?qr=" + member.getId());
        String hashCode = String.format(getContext().getString(R.string.tv_msg189) + "", context.getString(R.string.app_name), String.valueOf(member.getId()));
        sxyh.setText(hashCode);
        personal p = member.getPersonal();

        if (p != null) {
            String textsex = member.getSex() == Constants.TENCENT ? getContext().getString(R.string.tv_msg187) : getContext().getString(R.string.tv_msg188);
            String city = p.getCity();
            if (TextUtils.isEmpty(city)) {
                city = "";
            }
            age.setText(String.format(getContext().getString(R.string.tv_msg217) + "", textsex, p.getAge(), city));
        }

    }

    public static void qrcodes(Context context, member member) {
        dialog_item_qrcode dialog_item_qrcode = new dialog_item_qrcode(context, member);
        dialog_item_qrcode.show();
    }

    /**
     * 4.2传入内容生成二维码
     *
     * @param content
     */
    private void setqrcode(String content) {
        try {
            Bitmap bitmap = BitmapUtils.create2DCode(content);
            qrecode.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
