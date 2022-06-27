package com.tencent.qcloud.tim.tuikit.live.component.bottombar;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tencent.qcloud.tim.tuikit.live.R;
import com.tencent.qcloud.tim.tuikit.live.component.common.CircleImageView;
import com.tencent.qcloud.tim.tuikit.live.component.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.utils.UIUtil;

import java.util.List;

/**
 * //底部相关按钮
 */
public class BottomToolBarLayout extends ConstraintLayout {
    private static final String TAG = BottomToolBarLayout.class.getSimpleName();
    private Context mContext;
    private ConstraintLayout mLayoutRoot;
    private LinearLayout mLayoutFunction;
    private TextView mTextMessage;
    private InputTextMsgDialog mInputTextMsgDialog; // 消息输入框

    private LinearLayout.LayoutParams mFunctionLayoutParams;

    public BottomToolBarLayout(Context context) {
        super(context);
        initView(context);
    }

    public BottomToolBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public BottomToolBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mLayoutRoot = (ConstraintLayout) inflate(context, R.layout.live_layout_bottom_tool_bar, this);
        mLayoutFunction = mLayoutRoot.findViewById(R.id.ll_function);
        int iconSize = mContext.getResources().getDimensionPixelSize(R.dimen.live_bottom_toolbar_btn_icon_size);
        mFunctionLayoutParams = new LinearLayout.LayoutParams(iconSize, iconSize);  // 设置底部功能区按钮的添加样式 自定义图标
        mFunctionLayoutParams.rightMargin = UIUtil.dp2px(mContext, 5);
        initTextMessageView();
    }

    /**
     * 接收输入消息
     */
    private void initTextMessageView() {
        mInputTextMsgDialog = new InputTextMsgDialog(mContext, R.style.LiveInputDialog);
        mTextMessage = mLayoutRoot.findViewById(R.id.tv_message);
        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputMsgDialog();
            }
        }); //点击监听事件
    }

    /**
     * 弹窗
     */
    private void showInputMsgDialog() {
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();
        lp.width = UIUtil.getScreenWidth(mContext);
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    /**
     * 消息监听
     *
     * @param listener
     */
    public void setOnTextSendListener(InputTextMsgDialog.OnTextSendDelegate listener) {
        mInputTextMsgDialog.setTextSendDelegate(listener);
    }

    /**
     * 设置底部右按钮布局
     *
     * @param buttonList
     */
    public void setRightButtonsLayout(List<CircleImageView> buttonList) {
        mLayoutFunction.removeAllViews();
        for (CircleImageView button : buttonList) {
            mLayoutFunction.addView(button, mFunctionLayoutParams);
        }
    }

    public TextView getmTextMessage() {
        return mTextMessage;
    }
}
