package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.activity.Withdrawal.acitivity_settlement;

public class dialog_item_view_yhk extends Dialog {
    public dialog_item_view_yhk(@NonNull Context context) {
        super(context,R.style.fei_style_dialog);
        setContentView(R.layout.dialog_item_view_yhk);
        findViewById(R.id.send).setOnClickListener(v -> OnClickListener());
    }

    public void OnClickListener() {
        getContext().startActivity(new Intent(getContext(), acitivity_settlement.class));
    }

    public static void yhkshow(Context context) {
        dialog_item_view_yhk dialog_item_view_yhk = new dialog_item_view_yhk(context);
        dialog_item_view_yhk.show();
    }
}
