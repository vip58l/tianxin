/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/17 0017
 */


package com.tianxin.activity.matching;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BasActivity;
import com.tianxin.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 她想和你聊天哟
 */
public class activity_video_speed extends BasActivity {
    @BindView(R.id.circleImageView)
    ImageView circleImageView;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.mssge)
    TextView mssge;

    @Override
    protected int getview() {
        return R.layout.video_speed;
    }

    @Override
    public void iniview() {
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.cols, R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.senbnt:
                break;
            case R.id.cols:
                finish();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

}
