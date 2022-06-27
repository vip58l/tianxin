package com.tianxin.activity.Login;

import static com.tianxin.wxapi.WXpayObject.isWeixinAvilible;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.app.DemoApplication;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tianxin.wxapi.WXpayObject;
import com.tencent.opensource.model.UserInfo;

/**
 * 登录引导页
 */
public class LoginActivity extends FrameLayout implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ImageView bg_left, bg_right, iv_login_wechat, iv_login_phone, iv_login_qq;
    private CheckBox check_login;
    private UserInfo userInfo;
    private TextView openRegisterProtocal2, openRegisterProtocal3;
    private HorizontalScrollView horizontal_scroll_view;
    private Button login;
    private Paymnets paymnets;

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }

    public LoginActivity(@NonNull Context context) {
        super(context);
    }

    public LoginActivity(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoginActivity(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.activity_login_mj, this);
        userInfo = UserInfo.getInstance();
        bg_left = findViewById(R.id.bg_left);
        bg_right = findViewById(R.id.bg_right);
        iv_login_phone = findViewById(R.id.iv_login_phone);
        iv_login_wechat = findViewById(R.id.iv_login_wechat);
        iv_login_qq = findViewById(R.id.iv_login_qq);
        check_login = findViewById(R.id.check_login);
        openRegisterProtocal2 = findViewById(R.id.openRegisterProtocal2);
        openRegisterProtocal3 = findViewById(R.id.openRegisterProtocal3);
        horizontal_scroll_view = findViewById(R.id.horizontal_scroll_view);
        login = findViewById(R.id.login);
        listener();
        playanimation();
    }

    private void listener() {
        if (userInfo.isChecked()) {
            check_login.setChecked(true);
        }
        login.setOnClickListener(this);
        iv_login_phone.setOnClickListener(this);
        iv_login_wechat.setOnClickListener(this);
        iv_login_qq.setOnClickListener(this);
        check_login.setOnClickListener(this);
        openRegisterProtocal2.setOnClickListener(this);
        openRegisterProtocal3.setOnClickListener(this);
        horizontal_scroll_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void tostartActivity(int type) {
        String format = String.format(Webrowse.invitefriends + "?type=%s&userid=%s", type, userInfo.getUserId());
        Intent intent = new Intent(getContext(), DyWebActivity.class);
        intent.putExtra(Constants.VIDEOURL, format);
        getContext().startActivity(intent);
    }

    /**
     * 移动动画
     */
    private void playanimation() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.translate_anim);
                Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.translate_anim);
                bg_left.startAnimation(animation1);
                bg_right.startAnimation(animation2);

            }
        }, 200);
    }

    @Override
    public void onClick(View v) {
        boolean checked = check_login.isChecked();
        switch (v.getId()) {
            case R.id.login:
            case R.id.iv_login_wechat:
            case R.id.iv_login_phone:{
                setVisibility(GONE);
                userInfo.setChecked(true);
                break;}

//            case R.id.iv_login_wechat1:{
//                if (!checked) {
//                    Toashow.show(getContext().getString(R.string.tv_msg281));
//                } else {
//                    if (!isWeixinAvilible()) {
//                        Toashow.show(DemoApplication.instance().getString(R.string.tm80));
//                    } else {
//                        WXpayObject.getsWXpayObject().logdinwx();
//                        if (paymnets != null) {
//                            paymnets.loginqq();
//                        }
//                    }                }
//                break;}

            case R.id.iv_login_qq: {
                if (!checked) {
                    Toashow.show(getContext().getString(R.string.tv_msg281));
                } else {
                    Toashow.show(getContext().getString(R.string.tm79));
                }
                if (paymnets != null) {
                    paymnets.loginqq();
                }
                break;
            }

            case R.id.check_login:{
                userInfo.setChecked(checked?true:false);
                break;}

            case R.id.openRegisterProtocal2:{
                tostartActivity(3);  break;}

            case R.id.openRegisterProtocal3:{
                tostartActivity(4);
                break;}
        }


    }
}
