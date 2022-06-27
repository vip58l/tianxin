package com.tianxin.activity.game;

import android.view.View;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.widget.Backtitle;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 游戏分类
 */
public class Game_Activity_show extends BasActivity2 {
    @BindView(R.id.backtitle)
    Backtitle backtitle;

    @Override
    protected int getview() {
        return R.layout.activity_game_show;
    }

    @Override
    public void iniview() {
        backtitle.setconter(getString(R.string.ts123));

    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.lin1, R.id.lin2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.lin1:
                tostartActivity(Game_Activity_list.class, 1);
                break;
            case R.id.lin2:
                tostartActivity(Game_aActivity.class, 1);
                break;
        }
    }

    @Override
    public void OnEorr() {

    }
}