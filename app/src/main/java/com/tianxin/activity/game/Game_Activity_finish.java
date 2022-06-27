package com.tianxin.activity.game;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.dialog.dialog_msg_svip;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.Activity_item_conter;
import com.tianxin.widget.Backtitle;
import com.tianxin.widget.item_chid_play;
import com.tencent.opensource.model.Gamefinish;
import com.tencent.opensource.model.Gametitle;
import com.tencent.opensource.model.videolist;

import butterknife.BindView;
import butterknife.OnClick;

/**订单状态
 *
 */
public class Game_Activity_finish extends BasActivity2 {
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.orderid)
    Activity_item_conter orderid;
    @BindView(R.id.name)
    Activity_item_conter name;
    @BindView(R.id.money)
    Activity_item_conter money;
    @BindView(R.id.quantity)
    Activity_item_conter quantity;
    @BindView(R.id.msg)
    Activity_item_conter msg;
    @BindView(R.id.datetime)
    Activity_item_conter datetime;
    @BindView(R.id.updatetime)
    Activity_item_conter updatetime;
    @BindView(R.id.book)
    Activity_item_conter book;
    @BindView(R.id.name1)
    TextView name1;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.toushu)
    Activity_item_conter toushu;
    @BindView(R.id.button)
    Button button;
    private String id;
    private Gamefinish gamefinish;

    @Override
    protected int getview() {
        return R.layout.activity_game_finish;
    }

    @Override
    public void iniview() {
        id = getIntent().getStringExtra(Constants.JSON);
        backtitle.setconter(getString(R.string.tm37));
        name.setTitle(getString(R.string.tm38));
        orderid.setTitle(getString(R.string.tm39));
        money.setTitle(getString(R.string.tm40));
        quantity.setTitle(getString(R.string.tm41));
        msg.setTitle(getString(R.string.tm42));
        datetime.setTitle(getString(R.string.tm43));
        book.setTitle(getString(R.string.tm16));
        updatetime.setTitle(getString(R.string.tm44));
        toushu.setTitle(getString(R.string.tm45));
        toushu.setImgshow();
    }

    @Override
    public void initData() {
        datamodule.GameActivityfinish(id, paymnets);
    }

    @Override
    @OnClick({R.id.button, R.id.toushu})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                dialog_msg_svip.dialogmsgsvip(context, getString(R.string.tm449), getString(R.string.btn_cancel), getString(R.string.btn_ok), dialog);
                break;
            case R.id.toushu:
                reportUser(gamefinish);
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onSuccess(Object obj) {
            gamefinish = (Gamefinish) obj;
            Gametitle gametitle = gamefinish.getGametitle();
            com.tencent.opensource.model.member member1 = gamefinish.getMember1();
            com.tencent.opensource.model.member member2 = gamefinish.getMember2();
            String ff = userInfo.getUserId().equals(String.valueOf(member1.getId())) ? "-" : "+";
            if (!TextUtils.isEmpty(gametitle.getPic())) {
                Glideload.loadImage(image, gametitle.getPic());
            }
            name1.setText(member2.getTruename() + "-" + gamefinish.getName());
            total.setText(ff + gamefinish.getTotal());
            ok.setText(gamefinish.getOk() == 0 ? getString(R.string.tm35) : getString(R.string.tm36));
            getuserid = String.valueOf(member2.getId());
            name.setConmp1(member2.getTruename() + "-" + gamefinish.getName());
            orderid.setConmp1(gamefinish.getOrderid());
            money.setConmp1(gamefinish.getMoney());
            quantity.setConmp1(String.valueOf(gamefinish.getQuantity()));
            msg.setConmp1(gamefinish.getMsg());
            book.setConmp1(Config.stampToDate(gamefinish.getBookdatetime()));
            datetime.setConmp1(Config.stampToDate(gamefinish.getDatetime()));
            updatetime.setConmp1(TextUtils.isEmpty(gamefinish.getUpdatetime()) ? "" : Config.stampToDate(gamefinish.getUpdatetime()));
            button.setVisibility(gamefinish.getOk() == 0 && userInfo.getUserId().equals(String.valueOf(member1.getId())) ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);

        }
    };

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
            datamodule.gamefinishid(gamefinish, new Paymnets() {
                @Override
                public void onSuccess(String msg) {
                    dismissDialog();
                    Toashow.show(msg);
                }

                @Override
                public void onSuccess() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            gamefinish.setOk(1);
                            ok.setText(gamefinish.getOk() == 0 ? getString(R.string.tm35) : getString(R.string.tm36));
                            updatetime.setConmp1(Config.DateTime());
                            button.setVisibility(View.GONE);
                            dismissDialog();
                        }
                    }, 500);
                }
            });
        }
    };


    /**
     * 举报投诉
     */
    public void reportUser(Gamefinish gamefinish) {
        videolist videolist = new videolist();
        videolist.setUserid(getuserid);
        videolist.setId(getString(R.string.tm46) + gamefinish.getOrderid());
        item_chid_play.reportUser(context, videolist, 2);
    }
}