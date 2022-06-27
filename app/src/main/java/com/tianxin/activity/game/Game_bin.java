package com.tianxin.activity.game;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Backtitle;
import com.tencent.opensource.model.Gamelist;
import com.tencent.opensource.model.Gametitle;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 游戏下单
 */
public class Game_bin extends BasActivity2 {
    private static final String TAG = Game_bin.class.getSimpleName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.ttitle)
    TextView ttitle;
    @BindView(R.id.msg_content)
    TextView msg_content;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.l2)
    TextView l2;
    @BindView(R.id.editmsg)
    EditText editmsg;
    private Gamelist gamelist;
    private com.tencent.opensource.model.member member;
    private Gametitle gametitle;
    private String json;
    private String bookdatetime;

    @Override
    protected int getview() {
        return R.layout.layout_game_bin;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.tv_msg_bin));
        json = getIntent().getStringExtra(Constants.JSON);
        gamelist = gson.fromJson(json, Gamelist.class);
        member = gamelist.getMember();
        gametitle = gamelist.getGametitle();
        Glide.with(context).load(gamelist.getMember().getPicture()).into(image);
        name.setText(gamelist.getMember().getTruename());
        msg.setText(String.format("ID:%s", member.getId()));
        ttitle.setText(gamelist.getName());
        msg_content.setText(R.string.tv_msg_tm12);
        msg.setText(String.format(getString(R.string.tv_msg_tmhdate) + "", gamelist.getMoney()));
        money.setText(String.format(getString(R.string.tv_msg_tmhdate) + "", gamelist.getMoney()));
        couneroo = 1;
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.onecon, R.id.buttin2, R.id.l1, R.id.l3, R.id.msg_content})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.msg_content:
                initTimePicker();
                break;
            case R.id.onecon:
                tostartActivity(activity_picenter.class, String.valueOf(member.getId()));
                break;
            case R.id.buttin2:
                String s = editmsg.getText().toString();
                if (TextUtils.isEmpty(bookdatetime)) {
                    Toashow.show(getString(R.string.tm57));
                    return;
                }
                if (TextUtils.isEmpty(s)) {
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg_tm11));
                    return;
                }
                if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
                    Toashow.show(getString(R.string.tm29));
                    return;
                }
                dialog_msg_svip.dialogmsgsvip(context, getString(R.string.tm4), getString(R.string.tv_msg_msg1), getString(R.string.tm14), dialog);
                break;
            case R.id.l1:
                couneroo--;
                if (couneroo <= 1) {
                    couneroo = 1;
                }
                l2.setText(String.valueOf(couneroo));
                money.setText(String.format(getString(R.string.tv_msg_tm10) + "", (gamelist.getMoney() * couneroo)));
                break;
            case R.id.l3:
                couneroo++;
                if (couneroo >= 100) {
                    couneroo = 50;
                }
                l2.setText(String.valueOf(couneroo));
                money.setText(String.format(getString(R.string.tv_msg_tm10) + "", (gamelist.getMoney() * couneroo)));
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 时间选择器
     */
    private void initTimePicker() {
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                long s = System.currentTimeMillis();
                Date mySystem = new Date(s);
                if (date.getTime() <= mySystem.getTime() + 30) {
                    Toashow.show(getString(R.string.tm58));
                    return;
                }

                msg_content.setText(getTime(date));
                bookdatetime = String.valueOf(date.getTime() / 1000);
            }
        }).setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(24)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.tm56))
                .build();

        pvTime.show();
    }

    /**
     * 提示回调
     */
    private Paymnets dialog = new Paymnets() {
        @Override
        public void onRefresh() {

        }

        @Override
        public void onSuccess() {
            showDialog();
            datamodule.game_order(gamelist, couneroo, editmsg.getText().toString(), bookdatetime, paymnets);
        }
    };

    /**
     * 发布回调
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            dismissDialog();
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void success(String id) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    Toashow.show(getString(R.string.tm48));
                    finish();
                    tostartActivity(Game_Activity_finish.class, "", id);

                }
            }, 1000);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
            dismissDialog();
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
            dismissDialog();
        }

        @Override
        public void onError() {
            Toashow.show(getString(R.string.eorrfali3));
            dismissDialog();
        }

    };

}
