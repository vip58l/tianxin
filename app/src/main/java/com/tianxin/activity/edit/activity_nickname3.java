/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/2 0002
 */


package com.tianxin.activity.edit;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.Util.Config;
import com.tencent.opensource.model.Basicconfig;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.activity.DouYing.activity_jsonvideo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class activity_nickname3 extends BasActivity2 {
    String TAG = activity_nickname3.class.getSimpleName();
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.phone)
    EditText phone;
    @BindView(R.id.close1)
    TextView close1;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.endpadder)
    TextView endpadder;
    String trusername;
    Basicconfig basicconfig;

    public static void setAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_nickname3.class);
        //intent.setData(Uri.parse("http://www.baidu.com"));
        context.startActivity(intent);


    }

    @Override
    protected int getview() {
        return R.layout.activity_nickname2;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.ts_a1));
        itemback.righttext.setText(R.string.s111);
        nickname.setHint(R.string.tm67);
        endpadder.setText(R.string.tm69);
        phone.setVisibility(View.VISIBLE);

        itemback.righttext.setTextColor(getResources().getColor(R.color.colorAccent));
        TYPE = getIntent().getIntExtra(Constants.POSITION, -1);
        nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //编辑框内容变化之前会调用该方法，s为编辑框内容变化之前的内容
                int ss = start + 1;
                close1.setText(String.format("%s/50", ss));
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //只要编辑框内容有变化就会调用该方法，s为编辑框变化后的内容
                int ss = start + 1;
                close1.setText(String.format("%s/50", ss));
            }

            @Override
            public void afterTextChanged(Editable s) {
                //编辑框内容变化之后会调用该方法，s为编辑框内容变化后的内容
                int editStart = nickname.getSelectionStart();
                int editEnd = nickname.getSelectionEnd();
                if (s.length() > 50) {
                    Toashow.show("你输入的字数已经超过了限制");
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    nickname.setText(s);
                    return;
                }
            }
        });

    }

    @Override
    public void initData() {
        datamodule.basicconfig(0, new Paymnets() {
            @Override
            public void isNetworkAvailable() {
                Paymnets.super.isNetworkAvailable();
            }

            @Override
            public void onSuccess(Object object) {
                basicconfig = (Basicconfig) object;
            }
        });
    }

    @Override
    @OnClick({R.id.button, R.id.tv3title})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.button: {
                trusername = nickname.getText().toString().trim();
                String mphone = phone.getText().toString().trim();
                if (TextUtils.isEmpty(trusername)) {
                    Toashow.show(getString(R.string.tv_msg128));
                    return;
                }
                if (trusername.length() > 50) {
                    Toashow.show(getString(R.string.tv_msg34));
                    return;
                }
                if (!TextUtils.isEmpty(activity_jsonvideo.getCompleteUrl(trusername))) {
                    Toashow.show(getString(R.string.tv_msg35));
                    return;
                }
                //提交反馈意见
                datamodule.PostModule(trusername, mphone, paymnets);
            }
            break;
            case R.id.tv3title: {
                String kefu = basicconfig.getKefu();
                if (basicconfig != null && !TextUtils.isEmpty(kefu)) {
                    startChatActivity();
                } else {
                    Toashow.show(getString(R.string.taaa4));
                }

            }
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

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            mesresult = (Mesresult) object;
            ToastUtil.toastShortMessage(mesresult.getMsg());
            if (mesresult.getStatus().equals("1")) {
                Toashow.show(mesresult.getMsg());
                finish();
            }
        }
    };

    /**
     * 这里需要用户上传头像才能聊天
     */
    private void startChatActivity() {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }

        ChatActivity.setAction(context, basicconfig.getKefu(), getString(R.string.gfkf));
    }


}
