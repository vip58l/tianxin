package com.tianxin.activity.party;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.adapter.Radapter;
import com.tianxin.listener.Callback;
import com.tianxin.widget.Backtitle;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 编辑内容
 */
public class activity_edit_party extends BasActivity2 {
    @BindView(R.id.e1)
    EditText e1;
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.clockwise)
    ImageView clockwise;
    @BindView(R.id.conversation_unread)
    TextView conversation_unread;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    List<String> s = new ArrayList<>();

    public static void setAction(Context context) {
        Intent intent = new Intent(context, activity_edit_party.class);
        context.startActivity(intent);
    }

    public static void setAction(Activity context) {
        Intent intent = new Intent(context, activity_edit_party.class);
        context.startActivityForResult(intent, Config.sussess);
    }

    public static void setAction(Activity context, String msg, int type) {
        Intent intent = new Intent(context, activity_edit_party.class);
        intent.putExtra(Constants.Msg, msg);
        intent.putExtra(Constants.TYPE, type);
        context.startActivityForResult(intent, Config.sussess);
    }

    public static void setAction(Activity context, String title, String msg, int type) {
        Intent intent = new Intent(context, activity_edit_party.class);
        intent.putExtra(Constants.Msg, msg);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.TYPE, type);
        context.startActivityForResult(intent, Config.sussess);
    }

    @Override
    protected int getview() {
        return R.layout.activity_edit_party;
    }

    @Override
    public void iniview() {
        TYPE = getIntent().getIntExtra(Constants.TYPE, 0);
        String msg = getIntent().getStringExtra(Constants.Msg);
        String TITLE = getIntent().getStringExtra(Constants.TITLE);
        e1.setText(msg);
        backtitle.setconter(TITLE);
        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                conversation_unread.setText(String.format("%s/50", s.length()));
                int Start = e1.getSelectionStart();
                int End = e1.getSelectionEnd();
                if (s.length() > 50) {
                    s.delete(Start - 1, End);
                    e1.setText(s);
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg140));
                    return;
                }
                clockwise.setVisibility(s.length() > 1 ? VISIBLE : GONE);
            }


        });
        switch (TYPE) {
            case 1:
                list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.tab002)));
                ssrecycler();
                break;
            case 2:
                address.setText(Config.stampToDate(String.valueOf(System.currentTimeMillis() / 1000)));
                break;
            case 3:
                if (ActivityLocation.checkpermissions(activity)) {
                    lbsamap.getmyLocation(callback);
                }
                list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.tab003)));
                ssrecycler();

                break;
            case 4:
                list.addAll(Arrays.asList(context.getResources().getStringArray(R.array.tab001)));
                recycler.setLayoutManager(new GridLayoutManager(context, 4));
                recycler.setAdapter(radapter = new Radapter(context, list, Radapter.party, new Callback() {
                    @Override
                    public void OnClickListener(int position) {
                        String msg = (String) list.get(position);
                        if (s.size() >= 4) {
                            Toashow.show(getString(R.string.taaa));
                            return;
                        }
                        s.add(msg);
                        e1.setText(!TextUtils.isEmpty(e1.getText().toString()) ? e1.getText().toString().trim() + "," + msg : msg);
                    }

                    @Override
                    public void OndeleteListener(int position) {

                    }
                }));
                break;
        }
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.button, R.id.address, R.id.clockwise})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                String s = e1.getText().toString();
                if (TextUtils.isEmpty(s)) {
                    Toashow.show(getString(R.string.msg_a11));
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.Msg, s);
                intent.putExtra(Constants.TYPE, TYPE);
                setResult(Config.setResult, intent);
                finish();
            }
            break;
            case R.id.address:
                e1.setText(address.getText().toString());
                break;
            case R.id.clockwise:
                e1.setText(null);
                s.clear();
                break;
        }


    }

    @Override
    public void OnEorr() {

    }

    private Callback callback = new Callback() {
        @Override
        public void onSuccess(AMapLocation amapLocation) {
            Callback.super.onSuccess(amapLocation);
            if (!TextUtils.isEmpty(amapLocation.getAddress())) ;
            {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        address.setText(amapLocation.getAddress());
                        //list.add(0, amapLocation.getAddress());
                        //radapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onFall() {

        }
    };

    private void ssrecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(radapter = new Radapter(context, list, Radapter.party, new Callback() {
            @Override
            public void OnClickListener(int position) {
                String msg = (String) list.get(position);
                e1.setText(msg);
            }

            @Override
            public void OndeleteListener(int position) {

            }
        }));
    }


}