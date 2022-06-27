package com.tianxin.IMtencent.scenes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.tianxin.Module.api.Config_Msg;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.dialog.Dialog_bottom;
import com.tianxin.listener.Paymnets;
import com.tianxin.alipay.cs_alipay;
import com.tencent.qcloud.tim.tuikit.live.base.Constants;
import com.tencent.qcloud.tim.tuikit.live.modules.liveroom.TUILiveRoomAudienceLayout;

/**
 * 观众进入房间
 */
public class LiveRoomAudienceActivity extends AppCompatActivity {
    private static final String TAG = "LiveAudienceActivity";
    private TUILiveRoomAudienceLayout mTUILiveRoomAudienceLayout;
    private cs_alipay csAlipay;
    private Context context;

    public static void start(Context context) {
        Intent starter = new Intent(context, LiveRoomAudienceActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        csAlipay = new cs_alipay(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
        }
        setContentView(R.layout.test_activity_live_room_audience);
        initLiveRoomAudienceLayout();
    }

    private void initLiveRoomAudienceLayout() {
        mTUILiveRoomAudienceLayout = findViewById(R.id.layout_room_audience);
        Intent intent = getIntent();
        int roomId = intent.getIntExtra(Constants.GROUP_ID, 0);
        String anchorId = intent.getStringExtra(Constants.ANCHOR_ID);

        //传入初始化评论提示内容
        String msg = Config_Msg.getInstance().getMsg();
        mTUILiveRoomAudienceLayout.setMsg(msg);
        mTUILiveRoomAudienceLayout.initWithRoomId(getSupportFragmentManager(), roomId, anchorId, false, "");
        //回调监听处理业务逻辑事件
        mTUILiveRoomAudienceLayout.setLiveRoomAudienceDelegate(new TUILiveRoomAudienceLayout.TUILiveRoomAudienceDelegate() {
            @Override
            public void onClose() {
                finish();
            }

            @Override
            public void onError(int errorCode, String message) {
                Toashow.show(errorCode + " " + message);
            }

            @Override
            public void onGiftItemClick() {
                // 礼物发送成功
            }

            @Override
            public void onChargeClick() {
                Dialog_bottom.dialogsBottom(context, paymnets);
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTUILiveRoomAudienceLayout.onBackPressed();
    }

    /**
     * 弹出充值金币
     */
    private final Paymnets paymnets = new Paymnets() {
        @Override
        public void onFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, getString(R.string.tv_msg166));
                }
            });

        }

        @Override
        public void activity(String str) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cs_alipay.showAlert(context, str);
                }
            });
        }

        @Override
        public void payonItemClick(moneylist moneylist, int TYPE) {
            csAlipay.Paymoney(moneylist);
        }
    };

}