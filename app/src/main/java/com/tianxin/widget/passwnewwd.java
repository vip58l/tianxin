package com.tianxin.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tianxin.Module.Datamodule;
import com.tianxin.R;
import com.tianxin.Util.KeyboardUtil;
import com.tianxin.listener.Paymnets;

public class passwnewwd extends FrameLayout implements View.OnClickListener {
    private static final String TAG = passwnewwd.class.getSimpleName();
    private Backtitle backtitle;
    private EditText username;
    private EditText password;
    private EditText password2;
    private ImageView editshow0, editshow1, editshow2;
    private Datamodule datamodule;
    private String token;
    private String mobile;
    private Paymnets paymnets;

    public passwnewwd(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public passwnewwd(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public passwnewwd(Context context) {
        super(context);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.newpwd, this);
        backtitle = findViewById(R.id.backtitle);
        backtitle.setconter(getContext().getString(R.string.tv_ss));
        datamodule = new Datamodule(getContext());
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        editshow0 = findViewById(R.id.editshow0);
        editshow1 = findViewById(R.id.editshow1);
        editshow2 = findViewById(R.id.editshow2);
        password.addTextChangedListener(new MyTextWatcher(editshow1));
        password2.addTextChangedListener(new MyTextWatcher(editshow2));
        editshow0.setVisibility(GONE);
        editshow1.setVisibility(GONE);
        editshow2.setVisibility(GONE);
        username.setEnabled(false);
        editshow0.setOnClickListener(this::onClick);
        editshow1.setOnClickListener(this::onClick);
        editshow2.setOnClickListener(this::onClick);
        findViewById(R.id.regs).setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editshow0:
                break;
            case R.id.editshow1:
                password.setText(null);
                break;
            case R.id.editshow2:
                password2.setText(null);
                break;
            case R.id.regs:{
                paymnets.dismiss();
                //提交密码修改
                KeyboardUtil.hideSoftInput(getContext(), this); //隐藏软键盘
                datamodule.editpwd(mobile, token, password.getText().toString(), password2.getText().toString(), paymnets);
                break;}
        }

    }

    /**
     * 编辑框输入监听事件
     */
    public class MyTextWatcher implements TextWatcher {
        private final View view;

        public MyTextWatcher(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            view.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 设置手机号和监听事件
     *
     * @param mobile
     * @param token
     * @param paymnets
     */
    public void setinfo(String mobile, String token, Paymnets paymnets) {
        username.setText(mobile);
        this.mobile = mobile;
        this.token = token;
        this.paymnets = paymnets;

    }

    public String getUsername() {
        return username.getText().toString();
    }

    public String getPassword() {
        return password.getText().toString();
    }
}
