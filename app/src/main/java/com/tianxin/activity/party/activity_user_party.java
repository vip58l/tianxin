package com.tianxin.activity.party;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.dialog.dialog_game;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.party_weide;
import com.tencent.opensource.model.Party;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 待审核
 */
public class activity_user_party extends BasActivity2 {
    private static final String TAG = activity_user_party.class.getName();

    @BindView(R.id.t1)
    party_weide t1;
    @BindView(R.id.t2)
    party_weide t2;
    @BindView(R.id.t3)
    party_weide t3;
    @BindView(R.id.t4)
    party_weide t4;
    @BindView(R.id.t5)
    party_weide t5;
    @BindView(R.id.t6)
    party_weide t6;
    @BindView(R.id.t7)
    party_weide t7;
    @BindView(R.id.e2)
    TextView e2;
    @BindView(R.id.tips)
    TextView tips;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.tvsendbtn)
    Button tvsendbtn;
    Party party;

    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_user_party.class);
        context.startActivity(intent);
    }

    public static void setAction(Context context, String json, int type) {
        Intent intent = new Intent(context, activity_user_party.class);
        intent.putExtra(Constants.JSON, json);
        intent.putExtra(Constants.TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_user_party;
    }

    @Override
    public void iniview() {
        String json = getIntent().getStringExtra(Constants.JSON);
        int type = getIntent().getIntExtra(Constants.TYPE, 0);
        if (type == 0) {
            party = gson.fromJson(json, Party.class);
            ui(party);
        }
        if (type == 1) {
            datamodule.partyfind(json, new Paymnets() {
                @Override
                public void isNetworkAvailable() {

                }

                @Override
                public void onSuccess(Object object) {
                    party = (Party) object;
                    ui(party);

                }

                @Override
                public void onFail() {

                }
            });

        }
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.button, R.id.tvsendbtn, R.id.t6})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                dialog_game.myshow(context, getString(R.string.a1title), getString(R.string.a2title), getString(R.string.a3title), new Paymnets() {
                    @Override
                    public void activity() {
                        showDialog();
                        datamodule.patrfinish(String.valueOf(party.getId()), this);
                    }

                    @Override
                    public void onFail() {
                        dismissDialog();
                    }

                    @Override
                    public void isNetworkAvailable() {
                        dismissDialog();
                    }

                    @Override
                    public void onFail(String msg) {
                        dismissDialog();
                        Toashow.show(msg);
                    }

                    @Override
                    public void onSuccess() {
                        dismissDialog();
                        Toashow.show(getString(R.string.Toaaaaa));
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    }
                });
            }
            break;
            case R.id.tvsendbtn:
                finish();
                break;

            case R.id.t6:
                if (userInfo.getUserId().equals(String.valueOf(party.getUserid()))) {
                    if (party.getFinish() == 1) {
                        Toashow.show(getString(R.string.paysss1));

                    } else {
                        activity_party_user_list.setAction(context, String.valueOf(party.getId()));
                    }
                } else {
                    if (party.getFinish() == 1) {
                        Toashow.show(getString(R.string.paysss1));
                    } else {
                        Toashow.show(getString(R.string.toaslog));
                    }
                }
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    private void ui(Party party) {
        if (party != null) {
            t1.setMtag(party.getTitle());
            t2.setMtag(party.getPartytime());
            t3.setMtag(party.getAddress());
            t4.setMtag(String.format("限%s人", party.getPartynumbe()));
            if (userInfo.getUserId().equals(String.valueOf(party.getUserid()))) {
                t5.setMtag(party.getCode() == 0 ? getString(R.string.ta116) : getString(R.string.ta17));
            } else {
                t5.setMtag(party.getCode() == 0 ? getString(R.string.ta16) : getString(R.string.ta17));
            }
            t6.setMtag(String.format("%s人", party.getPartyenumbe()));
            t7.setMtag(String.format("%s", party.getTdesc()));
            e2.setText(party.getMsg());
            switch (party.getStatus()) {
                case 0:
                    tips.setText(getString(R.string.ta94));
                    break;
                case 1:
                    tips.setText(getString(R.string.ta95));
                    tips.setTextColor(getResources().getColor(R.color.live_color_send_btn));
                    button.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tips.setText(getString(R.string.tm33));
                    tips.setTextColor(getResources().getColor(R.color.teal006));
                    break;
                case 3:
                    tips.setText(getString(R.string.paysss1));
                    tips.setTextColor(getResources().getColor(R.color.teal006));
                    break;
            }
            if (party.getFinish() == 1) {
                button.setVisibility(View.GONE);
                tvsendbtn.setVisibility(View.VISIBLE);
            } else {
                tvsendbtn.setVisibility(View.GONE);
            }
        }
    }


}