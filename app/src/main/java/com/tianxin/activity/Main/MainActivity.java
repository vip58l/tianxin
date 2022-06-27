package com.tianxin.activity.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.tianxin.app.DemoApplication;
import com.tianxin.dialog.dialog_del_notice;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.utils.MySocket;
import com.tianxin.widget.Buttionfooter;
import com.tencent.liteav.callService;
import com.tencent.opensource.model.UserInfo;
import com.tianxin.IMtencent.BaseActivity;
;
import com.tianxin.R;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.log;
import com.tianxin.listener.Paymnets;
import com.tianxin.dialog.Dialog_mesges;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {
    String TAG = MainActivity.class.getSimpleName();
    private float mExitTime;
    private Buttionfooter buttonfooter;

    public static void setAction(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.transparencyBar(activity, 2);
        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int result = intent.getIntExtra("RESULT", -1);
        buttonfooter = findViewById(R.id.buttonfooter);
        buttonfooter.result(result);


    }

    /**
     * 初始数据
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        buttonfooter = findViewById(R.id.buttonfooter);
        buttonfooter.setPaymnets(paymnets);
        buttonfooter.findViewById(R.id.lin_1).performClick();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                ToastUtil.toastShortMessage(getResources().getString(R.string.toastShor));
                mExitTime = System.currentTimeMillis();//获取BACK返回当前时间
                return true;
            }
            Dialog_mesges.Dialogs(context, paymnets);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            log.d("KEYCODE_MENU键");
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            log.d("KeyEvent.KEYCODE_BACK键");
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            log.d("KeyEvent.KEYCODE_HOME键");
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (buttonfooter != null) {
            buttonfooter.funbind();
        }
    }

    /**
     * 底部导航页面切换显示
     */
    private Paymnets paymnets = new Paymnets() {
        @Override
        public void onSuccess(Object object) {
            Fragment fragment = (Fragment) object;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
        }

        @Override
        public void onError() {
            //主播设置了自动呼叫停止服务
            callService.callstopService();
            MySocket.unInit();
            finish();
        }

        @Override
        public void onRefresh() {
            //帐户封禁通知
            dialog_Blocked.myshow(context);
        }

        @Override
        public void onLoadMore() {
            //信息已失效，请重新登录
            ToastUtil.toastLongMessage(DemoApplication.instance().getString(R.string.tv_msg1933));
            BaseActivity.logout(context);
        }

        @Override
        public void cancellation() {
            dialog_del_notice.myshow(context, new Paymnets() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccess() {
                    BaseActivity.logout(context);
                }
            });
        }

        @Override
        public void onSuccess() {
            //直播开闭
            buttonfooter.findViewById(R.id.lin_2).setVisibility(UserInfo.getInstance().getLive() == 1 ? View.GONE : View.VISIBLE);
            //短视频
            buttonfooter.findViewById(R.id.home6).setVisibility(UserInfo.getInstance().getLvideo() == 1 ? View.GONE : View.VISIBLE);
        }


    };


}