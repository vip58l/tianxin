package com.tianxin.activity.activity_item;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Fragment.page2.fragment.list_video_page;
import com.tianxin.Fragment.page3.fragment.find;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.StatusBarUtil;

import butterknife.BindView;

/**
 * 发现
 */
public class fragment_load extends BasActivity2 {
    @BindView(R.id.title)
    TextView title;

    public static void starsetAction(Context context) {
        Intent intent = new Intent(context, fragment_load.class);
        intent.putExtra(Constants.POSITION, 1);
        intent.putExtra(Constants.JSON, context.getString(R.string.play_videotitle));
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.transparencyBar(activity);
        StatusBarUtil.setStatusBar(activity, getResources().getColor(R.color.home3));
        return R.layout.fragment_meun;
    }

    @Override
    public void iniview() {
        mPlayingPosition = getIntent().getIntExtra(Constants.POSITION, 0);
        String mtitle = getIntent().getStringExtra(Constants.JSON);
        title.setText(mtitle);

        //通过mPlayingPosition 来确定加载那个加载 fragment
        if (fragments.size()==0){
            fragments.add(new find());
            fragments.add( new list_video_page());
        }
        Fragment fragment = fragments.get(mPlayingPosition);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }


    @Override
    public void initData() {

    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void OnEorr() {

    }
}
