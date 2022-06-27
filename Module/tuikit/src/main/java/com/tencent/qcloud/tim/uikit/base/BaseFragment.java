package com.tencent.qcloud.tim.uikit.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * BaseFragment父基类
 */
public class BaseFragment extends Fragment {

    public void forward(Fragment fragment, boolean hide) {
        forward(getId(), fragment, null, hide);
    }

    /**
     * add 配合 hide使用,能保存fragment状态不会每次刷新视图
     * @param viewId
     * @param fragment
     * @param name
     * @param hide
     */
    public void forward(int viewId, Fragment fragment, String name, boolean hide) {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        if (hide) {
            trans.hide(this);
            trans.add(viewId, fragment);
        } else {
            trans.replace(viewId, fragment);
        }

        trans.addToBackStack(name);
        trans.commitAllowingStateLoss();
    }

    /**
     * Android Fragment回退栈管理(popBackStack)
     */
    public void backward() {
        getFragmentManager().popBackStack();
    }
}
