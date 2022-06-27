/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/21 0021
 */


package com.tianxin.activity.ZYservices;

import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.tianxin.getHandler.PostModule;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.Fragment.fragment.Fragment_videotitle;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tianxin.widget.itembackTopbr;
import com.tencent.opensource.model.Mesresult;
import com.tencent.opensource.model.UserInfo;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 请选择关注的领域
 */
public class activity_categories extends BasActivity2 implements Paymnets {
    private static final String TAG = "activity_categories";
    @BindView(R.id.itemback)
    itembackTopbr itemback;
    Map<String, String> params;

    @Override
    protected int getview() {
        return R.layout.activity_backitem;
    }

    @Override
    public void iniview() {
        itemback.settitle(getString(R.string.tv_msg31));
        Fragment_videotitle fragment_videotitle = new Fragment_videotitle();
        fragment_videotitle.setPaymnets(this);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment, fragment_videotitle);
        transaction.commit();
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.senbnt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.senbnt:
                dateview();
                break;
        }
    }

    @Override
    public void OnEorr() {

    }

    /**
     * 提交到服务端去
     */
    private void dateview() {
        if (params == null) {
            Toashow.show(getString(R.string.eorrfali3));
            return;
        }
        if (params.size() == 0) {
            Toashow.show(getString(R.string.tv_msg32));
            return;
        }
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            i++;
            if (i < params.size()) {
                sb.append("id[]=" + params.get(key) + "&");
            } else {
                sb.append("id[]=" + params.get(key));
            }
        }
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        UserInfo instance = UserInfo.getInstance();
    PostModule.getModule(Webrowse.videotypeadd + "/videotypeadd?userid=" + instance.getUserId() + "&token=" + instance.getToken() + "&" + sb.toString(), this);

    }

    @Override
    public void success(String date) {
        try {
            Mesresult mesresult = new Gson().fromJson(date, Mesresult.class);
            Toashow.show(mesresult.getMsg());
            if (mesresult.getStatus().equals("1")) {
                itemback.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fall(int code) {

    }

    @Override
    public void onClick(Object object) {
        params = (Map<String, String>) object;
    }

}
