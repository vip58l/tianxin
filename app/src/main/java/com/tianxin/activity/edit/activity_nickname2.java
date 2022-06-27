/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/2 0002
 */


package com.tianxin.activity.edit;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改签名
 */
public class activity_nickname2 extends BasActivity2 {
    String TAG = activity_nickname2.class.getName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.close1)
    TextView close1;
    int TYPE;
    String trusername;

    public static void starsetAction(Activity context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, activity_nickname2.class);
        intent.putExtra(Constants.POSITION, type);
        context.startActivityForResult(intent, Config.sussess);
    }

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(activity, getResources().getColor(R.color.transparent));
        return R.layout.activity_nickname2;
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        itemback.setHaidtopBackgroundColor(true);
        TYPE = getIntent().getIntExtra(Constants.POSITION, -1);
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                close1.setText(String.format("%s/50", s.length()));
                int editStart = nickname.getSelectionStart();
                int editEnd = nickname.getSelectionEnd();
                if (s.length() > 50) {
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    nickname.setText(s);
                    ToastUtil.toastLongMessage(getString(R.string.tv_msg140));
                    return;
                }
            }


        });

        if (TYPE == 21) {
            itemback.settitle(getString(R.string.tass));
            nickname.setHint(R.string.tass3);
        } else {
            itemback.settitle(getString(R.string.tv_msg118));
            nickname.setHint(R.string.taaa1);
        }
    }

    @Override
    public void initData() {
        datamodule.mesresultsss(new Paymnets() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {
                Mesresult mesresult = (Mesresult) object;
                nickname.setText(mesresult.getMsg());
            }
        }, TYPE == 21 ? 2 : 1);
    }

    @Override
    @OnClick({R.id.endpadder, R.id.button})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.endpadder:
                List<String> mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem));//征友条件
                initTimePicker(mOptionsItems1, 14);
                break;
            case R.id.button:
                trusername = nickname.getText().toString();
                if (TextUtils.isEmpty(trusername)) {
                    if (TYPE == 21) {
                        ToastUtils.showShort(R.string.tv_msg1157);
                    } else {
                        ToastUtils.showShort(R.string.tv_msg119);
                    }
                    return;
                }
                if (trusername.length() > 50) {
                    Toashow.show(getString(R.string.tv_msg120));
                    return;
                }

                datamodule.myPostModule(trusername, TYPE, new Paymnets() {
                    @Override
                    public void onSuccess(Object object) {
                        Mesresult mesresult = (Mesresult) object;
                        setResultback();
                    }

                    @Override
                    public void onSuccess(String msg) {
                        Toashow.show(msg);
                    }
                });
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        // 在 Android Q（10）中，应用在前台的时候才可以获取到剪切板内容。
        // https://www.jianshu.com/p/8f2100cd1cc5

        String shareText = getShareText();
        if (!TextUtils.isEmpty(shareText) && shareText.contains(" https://v.douyin.com/")) {
            // nickname.setText(shareText);
        }


    }


    /**
     * 获取剪切版内容
     *
     * @return 剪切版内容
     */
    public String getShareText() {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString;
                }
            }
        }
        return "";
    }


    /**
     * 条件选择器
     */
    private void initTimePicker(List<String> mOptionsItems1, int result) {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String msg = mOptionsItems1.get(options1);
                nickname.setText(msg);
            }
        })
                .setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.tms1) + "")
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    /**
     * 回调上一页刷新内容
     */
    private void setResultback() {
        Intent intent = new Intent();
        intent.putExtra(Constants.POSITION, TYPE);
        intent.putExtra(Constants.truename, trusername);
        setResult(Config.setResult, intent);
        finish();
    }


}
