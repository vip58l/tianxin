package com.tianxin.activity.game;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.tencent.opensource.model.Gametitle;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 游戏发布增加
 */
public class Game_Activity_add extends BasActivity2 {
    private static final String TAG = Game_Activity_add.class.getSimpleName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.game_title)
    TextView game_title;
    @BindView(R.id.money)
    EditText money;
    @BindView(R.id.msg_content)
    EditText msg_content;
    private Gametitle gametitle;

    @Override
    protected int getview() {
        return R.layout.activity_game_add;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tm22));
        image.setVisibility(View.GONE);
        game_title.setText(R.string.tm24);
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.toolbar_title, R.id.buttion})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_title:
                tostartActivity(Game_Activity_title.class, 0, Constants.requestCode);
                break;
            case R.id.buttion:
                send();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.requestCode && data != null) {
            String json = data.getStringExtra(Constants.JSON);
            gametitle = gson.fromJson(json, Gametitle.class);
            if (!TextUtils.isEmpty(gametitle.getPic())) {
                Glideload.loadImage(image, gametitle.getPic());
                image.setVisibility(View.VISIBLE);
            }
            game_title.setText(gametitle.getName());
        }
    }

    /**
     * 写入游戏技能
     */
    private void send() {
        String msg = msg_content.getText().toString().trim();
        String moneys = money.getText().toString().trim();

        if (userInfo.getState() == 1 || userInfo.getState() == 0) {
            Toashow.show(getString(R.string.nametoashow));
            return;
        }

        if (userInfo.getState() == 3) {
            Toashow.show(getString(R.string.tv_msg21));
            return;
        }

        if (gametitle == null) {
            Toashow.show(context, getString(R.string.tm26));
            return;
        }

        if (TextUtils.isEmpty(moneys)) {

            Toashow.show(context, getString(R.string.tm25));
            return;
        }

        if (Integer.parseInt(moneys) <= 0) {
            Toashow.show(context, getString(R.string.tm25));
            return;
        }

        if (TextUtils.isEmpty(msg)) {
            Toashow.show(context, getString(R.string.tv_msg_tm11));
            return;
        }

        showDialog();
        datamodule.gameupdate(gametitle, moneys, msg, paymnets);
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            dismissDialog();
            Toashow.show(context, getString(R.string.eorrfali2));
        }

        @Override
        public void onSuccess(String msg) {
            dismissDialog();
            Toashow.show(context, msg);

        }

        @Override
        public void onSuccess() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toashow.show(getString(R.string.tm27));
                    setResult(Constants.REQUEST_CODE);
                    finish();
                }
            }, 1000);

        }

        @Override
        public void onFail() {
            dismissDialog();
            Toashow.show(context, getString(R.string.eorrfali2));
        }
    };

}