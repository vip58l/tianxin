/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/2/9 0009
 */


package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianxin.R;
import com.tianxin.listener.Paymnets;

import butterknife.BindView;
import butterknife.ButterKnife;

public class dialog_game extends Dialog implements View.OnClickListener {
    @BindView(R.id.exit)
    TextView exit;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.kankan)
    TextView kankan;
    Paymnets paymnets;

    public dialog_game(@NonNull Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_msg);
        ButterKnife.bind(this);
        setTitle("是否确认退出直播间");
        setExit("取消");
        setKankan("好的");
        kankan.setOnClickListener(this);
        exit.setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        show();
    }

    public void setExit(String sexit) {
        exit.setText(sexit);
    }

    public void setTitle(String stitle) {
        title.setText(stitle);
    }

    public void setTextSize(float size) {
        title.setTextSize(size);
    }

    public void setexitSize(float size) {
        exit.setTextSize(size);
    }

    public void setkankanSize(float size) {
        kankan.setTextSize(size);
    }

    public void setTextColor(int size) {
        title.setTextColor(size);
    }

    public void setexitColor(int size) {
        exit.setTextColor(size);
    }

    public void setkankanColor(int size) {
        kankan.setTextColor(size);
    }

    public void setKankan(String skankan) {
        kankan.setText(skankan);
    }

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kankan:
                paymnets.activity();
            case R.id.exit:
                dismiss();
                break;
        }

    }

    public static void myshow(Context context, Paymnets paymnets, int position) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(context.getString(R.string.tv_msg221));
        dialogGame.setKankan(context.getString(R.string.tv_msg113));
        dialogGame.setTextColor(context.getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(context.getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(paymnets);
    }

    public static void myshow(Context context, String title, String msg,String exit, Paymnets paymnets) {
        dialog_game dialogGame = dialog_Config.dialog_game(context);
        dialogGame.setTitle(title);
        dialogGame.setKankan(msg);
        dialogGame.setExit(exit);
        dialogGame.setTextColor(context.getResources().getColor(R.color.half_transparent));
        dialogGame.setkankanColor(context.getResources().getColor(R.color.c_fu));
        dialogGame.setTextSize(14);
        dialogGame.setPaymnets(paymnets);
    }


}
