/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.activity.matching;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tianxin.Util.Glideload;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.BasActivity.BasActivity;
import com.tianxin.R;
import com.tianxin.dialog.dialog_Blocked;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.uikit.utils.Allcharge;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class activity_audio_speed extends BasActivity {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.myConstraintLayout)
    ConstraintLayout myConstraintLayout;
    String TAG = activity_audio_speed.class.getSimpleName();

    public static void starsetAction(Context context){
        context.startActivity(new Intent(context, activity_audio_speed.class));
    }


    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(this);
        return R.layout.audio_speed;
    }

    @Override
    public void iniview() {
        if (allcharge != null) {
            setTypeAudioCall(allcharge);
        }
        datamodule.getallcharge(paymnets);
    }

    private void setTypeAudioCall(Allcharge allcharge) {
        if (userInfo.gettRole() == 1) {
            tv1.setText(String.format(getString(R.string.tv_jb1) + "", 0));
            tv2.setText(String.format(getString(R.string.tv_jb1) + "", 0));
        } else {
            tv1.setText(String.format(getString(R.string.tv_jb1) + "", allcharge.getVideo()));
            tv2.setText(String.format(getString(R.string.tv_jb1) + "", allcharge.getAudio()));
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.video, R.id.audio, R.id.view_diagnose_radar_back})
    public void OnClick(View v) {
        if (userInfo.getState() == 3) {
            dialog_Blocked.myshow(context);
            return;
        }

        switch (v.getId()) {
            case R.id.video:
                tostartActivity(activity_Matchchat.class, 1);
                break;
            case R.id.audio:
                tostartActivity(activity_Matchchat.class, 2);
                break;
            case R.id.view_diagnose_radar_back:
                finish();
                break;

        }
    }

    @Override
    public void OnEorr() {

    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
            if (allcharge != null) {
                setTypeAudioCall(allcharge);
            } else {
                allcharge = new Allcharge(80, 60);
                setTypeAudioCall(allcharge);
            }
        }

        @Override
        public void onSuccess(Object object) {
            allcharge = (Allcharge) object;
            setTypeAudioCall(allcharge);
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void ToKen(String msg) {
            activity_audio_speed.super.paymnets.ToKen(msg);
        }

        @Override
        public void onFail() {
            if (allcharge != null) {
                setTypeAudioCall(allcharge);
            }

        }
    };

    /**
     * 返回背景图
     *
     * @return
     */
    private void getURL() {
        String[] arrayimg = getResources().getStringArray(R.array.array_img);
        Random r = new Random();
        int i = r.nextInt(arrayimg.length);

        new Thread() {
            @Override
            public void run() {
                super.run();
                Drawable drawableGlide = Glideload.getDrawableGlide(arrayimg[i]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myConstraintLayout.setBackground(drawableGlide);
                    }
                });

            }
        }.start();
    }

}
