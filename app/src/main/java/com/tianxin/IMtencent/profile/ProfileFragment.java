package com.tianxin.IMtencent.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianxin.IMtencent.BaseActivity;
import com.tianxin.R;
import com.tianxin.app.DemoApplication;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.BaseFragment;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

/**
 * 我的主页
 */
public class ProfileFragment extends BaseFragment {

    private View mBaseView;
    private ProfileLayout mProfileLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.profile_fragment, container, false);
        initView();
        return mBaseView;
    }

    private void initView() {
        mProfileLayout = mBaseView.findViewById(R.id.profile_view);
        mBaseView.findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TUIKitDialog(getActivity())
                        .builder()
                        .setCancelable(true)
                        .setCancelOutside(true)
                        .setTitle("您确定要退出登录么？")
                        .setDialogWidth(0.75f)
                        .setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TUIKit.logout(new IUIKitCallBack() {

                                    @Override
                                    public void onSuccess(Object data) {
                                        logout();
                                    }

                                    @Override
                                    public void onError(String module, int errCode, String errMsg) {
                                        ToastUtil.toastLongMessage("logout fail: " + errCode + "=" + errMsg);
                                        logout();
                                    }

                                    private void logout() {
                                        BaseActivity.logout(DemoApplication.instance());
                                        if (getActivity() != null) {
                                            getActivity().finish();
                                        }
                                    }
                                } );
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .show();

            }
        });

    }
}
