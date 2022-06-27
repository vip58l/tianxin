/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/21 0021
 */


package com.tianxin.Fragment.fragment;

import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianxin.adapter.Radapter;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.Module.api.videotitle;
import com.tianxin.R;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.getHandler.PostModule;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.HttpUtils;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.costransferpractice.utils.JsonUitl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 分类选中状态
 */
public class Fragment_videotitle extends BasFragment implements Paymnets {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    List<Object> list = new ArrayList<>();
    private Radapter radapter;
    private Paymnets paymnets;
    UserInfo userInfo;
    String TAG = "Fragment_videotitle";
    //https://www.cnblogs.com/huameitang/p/8707458.html
    Map<String, String> params = new HashMap<String, String>();

    public void setPaymnets(Paymnets paymnets) {
        this.paymnets = paymnets;
    }


    @Override
    public void OnClick(View v) {

    }


    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.item_component,null);
    }

    @Override
    public void iniview() {
        userInfo = UserInfo.getInstance();
        radapter = new Radapter(getContext(), list, Radapter.Fragment_videotitle);
        radapter.setPaymnets(this);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(radapter);

        //失败获取了焦点
        //取消recyclerview单独的滑动效果
        //recyclerview.setFocusable(false);
        //recyclerview.setHasFixedSize(true);
        //recyclerview.setNestedScrollingEnabled(false);
    }

    @Override
    public void initData() {
        if (!Config.isNetworkAvailable()) {
            Toashow.show(getString(R.string.eorrfali2));
            return;
        }
        PostModule.getModule(HttpUtils.videotitle + "&userid=" + userInfo.getUserId(), this);
    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void status(int result) {
        videotitle videotitle = (videotitle) list.get(result);
        if (videotitle.getSelect() == 1) {
            videotitle.setSelect(0);
            params.remove(String.valueOf(videotitle.getId()));
        } else {
            videotitle.setSelect(1);
            //params.put(String.valueOf(videotitle.getId()), String.valueOf(videotitle.getId()));
            for (Object o : list) {
                videotitle vs = (videotitle) o;
                if (vs.getSelect() == 1) {
                    params.put(String.valueOf(vs.getId()), String.valueOf(vs.getId()));
                }
            }
        }
        radapter.notifyDataSetChanged();
        if (paymnets != null) {
            paymnets.onClick(params);
        }
                /*
                for (String key : params.keySet()) {
                    Log.d(TAG, "status: " + params.get(key));

                }*/
    }

    @Override
    public void success(String date) {
        try {
            List<videotitle> data = JsonUitl.stringToList(date, videotitle.class);
            list.addAll(data);
            radapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fall(int code) {

    }
}
