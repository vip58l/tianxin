package com.tianxin.activity.party;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.activity.Aboutus.activity_viecode;
import com.tianxin.activity.edit.activity_updateedit;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.party_weide;
import com.tencent.opensource.model.Party;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布聚会
 */
public class activity_party extends BasActivity2 {
    @BindView(R.id.e1)
    EditText e1;
    @BindView(R.id.e2)
    EditText e2;
    @BindView(R.id.t5)
    TextView t5;
    @BindView(R.id.t6)
    TextView t6;
    @BindView(R.id.t8)
    TextView t8;
    @BindView(R.id.t1)
    party_weide t1;
    @BindView(R.id.t2)
    party_weide t2;
    @BindView(R.id.t21)
    party_weide t21;
    @BindView(R.id.t7)
    party_weide t7;
    @BindView(R.id.check_login)
    CheckBox check_login;
    @BindView(R.id.clockwise)
    ImageView clockwise;
    Party party;
    String s1 = null;

    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_party.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_party_item;
    }

    @Override
    public void iniview() {
        party = new Party();
        party.setUserid(Integer.parseInt(UserInfo.getInstance().getUserId()));
        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                t8.setText(String.format("%s/50", s.length()));
                int Start = e2.getSelectionStart();
                int End = e2.getSelectionEnd();
                if (s.length() > 50) {
                    s.delete(Start - 1, End);
                    e2.setText(s);
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg140));
                    return;
                }
                clockwise.setVisibility(s.length() > 1 ? VISIBLE : GONE);
            }


        });

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6, R.id.t7, R.id.button, R.id.t9, R.id.t21, R.id.clockwise,})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.clockwise:
                e2.setText(null);
                break;
            case R.id.t1:
                activity_edit_party.setAction(activity, t1.getMtitle(), t1.getMtag(), 1);
                break;
            case R.id.t2:
                //activity_edit_party.setAction(activity, t2.getMtitle(), t2.getMtag(), 2);
                //star开始时间

                activity_updateedit.initTimePicker4(context, getString(R.string.s1date), 0, new Paymnets() {
                    @Override
                    public void payonItemClick(String msg, int type) {
                        if (type == 0) {
                            //end结束时间
                            s1 = msg;
                            activity_updateedit.initTimePicker4(context, getString(R.string.s2date), 1, this);
                        } else {
                            t2.setMtag(String.format("%s-%s", s1, msg));
                            party.setPartytime(String.format("%s-%s", s1, msg));
                        }
                    }

                    @Override
                    public void onFail() {

                    }
                });
                break;
            case R.id.t3: {
                mCurrentItem--;
                if (mCurrentItem <= 1) {
                    mCurrentItem = 1;
                    Toashow.show(getString(R.string.party_a2));
                }
                e1.setText(String.valueOf(mCurrentItem));
            }
            break;
            case R.id.t4: {
                mCurrentItem++;
                if (mCurrentItem >= 100) {
                    mCurrentItem = 100;
                    Toashow.show(getString(R.string.party_a1));
                }
                e1.setText(String.valueOf(mCurrentItem));
            }
            break;
            case R.id.t5:
                Background(1);
                break;
            case R.id.t6:
                Background(0);
                break;
            case R.id.t7:
                activity_edit_party.setAction(activity, t7.getMtitle(), t7.getMtag(), 4);
                break;
            case R.id.button: {
                KeyboardUtil.hideSoftInput(activity);
                if (TextUtils.isEmpty(party.getTitle())) {
                    Toashow.show(getString(R.string.ta_t1));
                    return;
                }
                if (TextUtils.isEmpty(party.getPartytime())) {
                    Toashow.show(getString(R.string.ta_t2));
                    return;
                }
                if (TextUtils.isEmpty(party.getAddress())) {
                    Toashow.show(getString(R.string.ta_t3));
                    return;
                }
                if (TextUtils.isEmpty(party.getTdesc())) {
                    Toashow.show(getString(R.string.ta_t311));
                    return;
                }
                String s1 = e1.getText().toString();
                String s = e2.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    Toashow.show(getString(R.string.party_a3));
                    return;
                }
                if (!check_login.isChecked()) {
                    Toashow.show(getString(R.string.patry_a4));
                    return;
                }
                party.setPartynumbe(Integer.valueOf(s1));
                party.setMsg(s);
                if (UserInfo.getInstance().getState() != 2) {
                    Toashow.show(getString(R.string.ss1));
                    return;
                }
                if (UserInfo.getInstance().getVip() == 0) {
                    Toashow.show(getString(R.string.ss2));
                    return;
                }
                if (UserInfo.getInstance().getJinbi() < 100) {
                    Toashow.show(getString(R.string.ss3));
                    return;
                }
                if (TextUtils.isEmpty(UserInfo.getInstance().getAvatar())) {
                    Toashow.show(getString(R.string.ss4));
                    return;
                }

                showDialog();
                datamodule.addpartyl(party, new Paymnets() {
                    @Override
                    public void onSuccess(String date) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dismissDialog();
                                        if (!TextUtils.isEmpty(date)) {
                                            party.setId(Integer.parseInt(date));
                                        }
                                        Toashow.show(getString(R.string.serview));
                                        activity_user_party.setAction(context, gson.toJson(party), 0);
                                        finish();
                                    }
                                },500);

                            }
                        });
                    }

                    @Override
                    public void onFail(String msg) {
                        Toashow.show(msg);
                        dismissDialog();
                    }
                });

            }
            break;
            case R.id.t9:
                activity_viecode.WebbookUrl(context, Webrowse.tips);
                break;
            case R.id.t21:
                activity_edit_party.setAction(activity, t21.getMtitle(), t21.getMtag(), 3);
                break;
            case R.id.check_login:
                check_login.setChecked(check_login.isChecked() ? false : true);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    private void Background(int type) {
        this.TYPE = type;
        party.setCode(type);
        t5.setBackground(context.getDrawable(R.drawable.activity014));
        t6.setBackground(context.getDrawable(R.drawable.activity014));
        t5.setTextColor(getResources().getColor(R.color.teal006));
        t6.setTextColor(getResources().getColor(R.color.teal006));

        if (type == 1) {
            t5.setBackground(context.getDrawable(R.drawable.activity011));
            t5.setTextColor(getResources().getColor(R.color.white));
        } else {
            t6.setBackground(context.getDrawable(R.drawable.activity011));
            t6.setTextColor(getResources().getColor(R.color.white));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && requestCode == Config.sussess) {
            int intExtra = data.getIntExtra(Constants.TYPE, 0);
            String msg = data.getStringExtra(Constants.Msg);
            switch (intExtra) {
                case 1:
                    t1.setMtag(msg);
                    party.setTitle(msg);
                    break;
                case 2:
                    t2.setMtag(msg);
                    party.setPartytime(msg);
                    break;
                case 3:
                    t21.setMtag(msg);
                    party.setAddress(msg);
                    break;
                case 4:
                    t7.setMtag(msg);
                    party.setPartyadvanced(msg);
                    party.setTdesc(msg);
                    break;
            }


        }


    }
}
