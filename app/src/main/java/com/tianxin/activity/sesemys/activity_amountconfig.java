package com.tianxin.activity.sesemys;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.dialog.Dialog_Exit;
import com.tianxin.listener.Paymnets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 代理调整比例设置
 */
public class activity_amountconfig extends BasActivity2 {

    private static String TAG = activity_amountconfig.class.getSimpleName();
    @BindView(R.id.agentw)
    TextView agentw;
    com.tencent.opensource.model.amountconfig amountconfig;
    String touserid;

    public static void starsetAction(Context context, String touserid) {
        Intent intent = new Intent(context, activity_amountconfig.class);
        intent.putExtra(Constants.touserid, touserid);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        return R.layout.activity_amountconfig;
    }

    @Override
    public void iniview() {
        touserid = getIntent().getStringExtra(Constants.touserid);
        datamodule.getamountconfig(touserid, paymnets);
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.dialog})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.dialog:
                if (amountconfig != null && amountconfig.getStatus() == 1) {
                    Toashow.show(getString(R.string.ntc));
                    return;
                }

                List<String> mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitemamount));
                Collections.reverse(mOptionsItems1);
                showinitTimePicker(mOptionsItems1);

                //Collections.sort(list)
                //Collections.reverse(list)
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
        public void onSuccess(Object object) {
            amountconfig = (com.tencent.opensource.model.amountconfig) object;
            agentw.setText("CPS " + amountconfig.getAgent() + "%");
        }

        @Override
        public void onSuccess() {
            //更新刷新UI
            datamodule.getamountconfig(touserid, this);
        }


    };

    /**
     * 条件选择器
     */
    private void showinitTimePicker(List<String> mOptionsItems1) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                Dialog_Exit.show(context, new Paymnets() {
                    @Override
                    public void activity() {
                        String money = mOptionsItems1.get(options1);
                        datamodule.editamountconfig(touserid, Integer.parseInt(money), paymnets);
                    }

                    @Override
                    public void payens() {

                    }
                }, getString(R.string.fig1), getString(R.string.fig2), getString(R.string.fig3));
            }
        })
                .setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.amount1))
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }
}