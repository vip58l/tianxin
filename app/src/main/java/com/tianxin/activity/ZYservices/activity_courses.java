/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/16 0016
 */


package com.tianxin.activity.ZYservices;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.IMtencent.scenes.LiveRoomAnchorActivity;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.widget.itembackTopbr;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 进入发布分类
 */
public class activity_courses extends BasActivity2 {
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    @BindView(R.id.layout1)
    RelativeLayout layout1;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.layout3)
    RelativeLayout layout3;
    @BindView(R.id.layout4)
    RelativeLayout layout4;
    @BindView(R.id.send)
    TextView send;
    int TYPE;

    @Override
    protected int getview() {
        return R.layout.activity_courser;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg4));
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.layout1, R.id.layout2, R.id.layout3, R.id.layout4, R.id.linboot})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.layout1:
                TYPE = 0;
                setbackgrounds(v);
                send.setText(getString(R.string.tv_msg79) + getString(R.string.tv_msg70));

                break;
            case R.id.layout2:
                TYPE = 1;
                setbackgrounds(v);
                send.setText(getString(R.string.tv_msg79) + getString(R.string.tv_msg72));

                break;
            case R.id.layout3:
                TYPE = 2;
                setbackgrounds(v);
                send.setText(R.string.tv_msg75);

                break;
            case R.id.layout4:
                TYPE = 3;
                setbackgrounds(v);
                send.setText(R.string.tv_msg75);
                break;
            case R.id.linboot:
                mgostartActivity();
                break;
        }

    }

    /**
     * 跳转到发布页
     */
    public void gostartActivity() {
        Intent intent = new Intent(this, activity_course.class);
        intent.putExtra(Constants.TYPE, TYPE);
        startActivity(intent);
    }

    /**
     * 跳转到我的课程页
     */
    private void gostartActivity3() {
        startActivity(new Intent(this, activity_mycurriculum.class));

    }

    /**
     * 跳转到直播页
     */
    public void LiveRoomAnchorActivity() {
        LiveRoomAnchorActivity.start(this, "");

    }

    @Override
    public void OnEorr() {

    }

    public void setbackgrounds(View v) {
        layout1.setBackground(getDrawable(R.drawable.diis_bg3));
        layout2.setBackground(getDrawable(R.drawable.diis_bg3));
        layout3.setBackground(getDrawable(R.drawable.diis_bg3));
        layout4.setBackground(getDrawable(R.drawable.diis_bg3));
        v.setBackground(getDrawable(R.drawable.diis_bg8));
    }

    public void mgostartActivity() {
        switch (TYPE) {
            case 2:
                LiveRoomAnchorActivity();
                break;
            case 3:
                gostartActivity3();
                break;
            default:
                gostartActivity();
                break;
        }
    }

    public void PostHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mgostartActivity();
            }
        }, 200);
    }
}
