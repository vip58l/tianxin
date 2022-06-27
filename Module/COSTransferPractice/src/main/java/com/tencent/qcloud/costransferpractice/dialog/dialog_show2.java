package com.tencent.qcloud.costransferpractice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.qcloud.costransferpractice.R;

public class dialog_show2 extends Dialog implements View.OnClickListener {
    Callback callback;
    Context context;
    TextView tv2, send11, send22;
    int TYPE;

    public dialog_show2(@NonNull Context context, String msg, Callback callback) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialogshow);
        this.context = context;
        this.callback = callback;
        send11 = findViewById(R.id.send11);
        send11.setOnClickListener(this);
        send22 = findViewById(R.id.send22);
        send22.setOnClickListener(this);
        tv2 = findViewById(R.id.tv2);
        tv2.setText(msg);

    }

    public dialog_show2(@NonNull Context context, String msg, String bntmsg, int TYPE, Callback callback) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialogshow);
        this.context = context;
        this.callback = callback;
        this.TYPE = TYPE;
        send11 = findViewById(R.id.send11);
        send11.setOnClickListener(this);
        send22 = findViewById(R.id.send22);
        send22.setOnClickListener(this);
        send22.setText(bntmsg);
        tv2 = findViewById(R.id.tv2);
        tv2.setText(msg);

    }

    public dialog_show2(@NonNull Context context, String title, String msg1, String msg2, Callback callback) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialogshow);
        this.context = context;
        this.callback = callback;

        tv2 = findViewById(R.id.tv2);
        send11 = findViewById(R.id.send11);
        send22 = findViewById(R.id.send22);
        if (!TextUtils.isEmpty(title)) {
            tv2.setText(title);
        }
        if (!TextUtils.isEmpty(msg1)) {
            send11.setText(msg1);
        }
        if (!TextUtils.isEmpty(msg2)) {
            send22.setText(msg2);
        }
        send11.setOnClickListener(this::onClick);
        send22.setOnClickListener(this::onClick);
    }

    public static void mshow(Context context, String msg, Callback callback) {
        dialog_show2 dialog_ms = new dialog_show2(context, msg, callback);
        dialog_ms.show();
    }

    public static void mshow(Context context, String msg, String bntmsg, int TYPE, Callback callback) {
        dialog_show2 dialog_ms = new dialog_show2(context, msg, bntmsg, TYPE, callback);
        dialog_ms.show();
    }

    public static void mshow(Context context, String title, String msg1, String msg2, Callback callback) {
        dialog_show2 dialog_ms = new dialog_show2(context, title, msg1, msg2, callback);
        dialog_ms.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        dismiss();
        if (callback == null) {
            return;
        }
        if (id == R.id.send11) {
            callback.onFall();
        } else if (id == R.id.send22) {
            if (TYPE == 3) {
                callback.onappeal();
            } else {
                callback.onSuccess();
            }
        }
    }
}
