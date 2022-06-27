package com.tianxin.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.Mesresult;

import butterknife.OnClick;

public class dialog_item_view_bain extends BaseDialog {
    private EditText editquery;
    private TextView textView;
    private TextView msgedit;

    public dialog_item_view_bain(@NonNull Context context, Paymnets paymnets) {
        super(context, paymnets);
        editquery = findViewById(R.id.editquery);
        msgedit = findViewById(R.id.msgedit);
        textView = findViewById(R.id.send);
        textView.setOnClickListener(v -> send());
        editquery.addTextChangedListener(watcher);
    }

    public static void myshow(Context context, Paymnets paymnets) {
        dialog_item_view_bain dialog_item_view_bain = new dialog_item_view_bain(context, paymnets);
        dialog_item_view_bain.show();
    }

    /**
     * 提交帮定内容
     */
    public void send() {
        String Parent = editquery.getText().toString().trim();
        if (TextUtils.isEmpty(Parent)) {
            msgedit.setText(getContext().getString(R.string.tv_msg203));
            msgedit.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            return;
        }

        //绑定推荐人
        datamodule.Gethttp(Parent, paynets11);
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.length();
            msgedit.setTextColor(getContext().getResources().getColor(R.color.homeback));
            if (length > 0) {
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.acitvity04));
                msgedit.setText(getContext().getString(R.string.tv_msg200));

            } else {
                textView.setBackground(getContext().getResources().getDrawable(R.drawable.activity011));
            }

        }
    };

    @Override
    public int getview() {
        return R.layout.dialog_item_view_bind;
    }

    @Override
    @OnClick({R.id.closeclick})
    public void OnClick(View v) {
        dismiss();
    }

    private Paymnets paynets11 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.toastMessage(R.string.eorrfali2);

        }

        @Override
        public void onFail() {
            Toashow.toastMessage(R.string.eorrfali3);
        }

        @Override
        public void onSuccess(String msg) {
            msgedit.setText(msg);
            msgedit.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        }

        @Override
        public void success(String date) {
            Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
            if (mesresult.getStatus().equals("1")) {
                dialog_item_view_bain.this.dismiss();
                if (paymnets != null) {
                    paymnets.onSuccess();
                    Toashow.show(mesresult.getMsg());
                }
            }
            msgedit.setText(mesresult.getMsg());
            msgedit.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        }
    };

}
