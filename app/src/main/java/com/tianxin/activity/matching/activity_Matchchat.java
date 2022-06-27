/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/19 0019
 */


package com.tianxin.activity.matching;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;

import butterknife.BindView;

/**
 * 进入随机匹配中界面
 */
public class activity_Matchchat extends BasActivity2 {
    private final String TAG = activity_Matchchat.class.getSimpleName();
    private int TYPE;
    @BindView(R.id.diagnoseradarwaiting)
    DiagnoseRadarView diagnoseradarwaiting;

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        return R.layout.activity_matchchat;
    }

    @Override
    public void iniview() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            finish();
            return;
        }
        TYPE = getIntent().getIntExtra(Constants.TYPE, -1);
        diagnoseradarwaiting.setType(TYPE);
    }

    @Override
    public void initData() {
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }

    /**
     * =============== 下徜为测试结果 =============================
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow连接到窗口:22 ");

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow从窗口分离: 44");
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            Log.d(TAG, "onWindowFocusChanged:窗口焦点已更改 33 ");
        }
    }


}
