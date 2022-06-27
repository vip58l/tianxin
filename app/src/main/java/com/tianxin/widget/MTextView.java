/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/21 0021
 */


package com.tianxin.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianxin.R;

public class MTextView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    /**
     * 显示内容的文本控件
     */
    private TextView mContentTextView;

    /**
     * 全文/收起 按钮
     */
    private TextView mExpansionButton;

    /**
     * 最大显示行数（默认 6 行）
     */
    private int mMaxLine = 6;

    private int paddbottom = 30;

    /**
     * 显示的内容
     */
    private CharSequence mContent;

    /**
     * 当前是否已是 "全文" 状态
     */
    private boolean mIsExpansion;

    public MTextView(@NonNull Context context) {
        super(context);
        init();
    }

    public MTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.mtextviews, this);
        mContentTextView = findViewById(R.id.tv_content);
        mExpansionButton = findViewById(R.id.v_expansion);

        // 监听文本控件的布局绘制
        mContentTextView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        // "全文/收起" 按钮点击监听
        mExpansionButton.setOnClickListener(v -> toggleExpansionStatus());
        // 默认隐藏 "全文/收起" 按钮
        mExpansionButton.setVisibility(GONE);
    }

    /**
     * 切换 全文/收起 状态
     */
    private void toggleExpansionStatus() {
        // 切换状态
        mIsExpansion = !mIsExpansion;

        // 更新内容和切换按钮的显示
        if (mIsExpansion) {
            mExpansionButton.setText("收起");                       // 全文状态, 按钮显示 "收起"
            mContentTextView.setMaxLines(Integer.MAX_VALUE);        // 全文状态, 行数设置为最大
        } else {
            mExpansionButton.setText("全文...");                       // 收起状态, 按钮显示 "全文"
            mContentTextView.setMaxLines(mMaxLine);                 // 收起状态, 最大显示指定的行数
        }
    }

    /**
     * 设置行数和字符大小
     * @param Size
     */
    public void setTextSize(int Size) {
        mContentTextView.setTextSize(Size);
    }

    /**
     * 全文/收起 字体色
     */
    public void setButtontextColor(int color) {
        mExpansionButton.setTextColor(color);
    }


    /**
     * 展示文本内容
     */
    public void settextColor(int color) {
        mContentTextView.setTextColor(color);
    }

    /**
     * 设置收起状态最大允许显示的行数（超过该行数显示 "收起" 按钮）
     */
    public void setMaxLine(int maxLine) {
        this.mMaxLine = maxLine;
        setText(mContent);                                      // 更新状态, 重新显示文本
    }

    /**
     * 设置需要显示的文本
     */
    public void setText(CharSequence text) {
        this.mContent = text;
        if (mContentTextView.getWidth() == 0) {
            return;
        }
        mContentTextView.setMaxLines(Integer.MAX_VALUE);        // 默认先设置最大行数为最大值（即不限制行数）
        mContentTextView.setText(mContent);                     // 设置文本
        int lineCount = mContentTextView.getLineCount();        // 设置完文本后, 获取显示该文本所需要的行数
        if (lineCount > mMaxLine) {
            //行数超过显示, 显示 "全文" 按钮
            mExpansionButton.setVisibility(VISIBLE);
            mExpansionButton.setText("全文...");
            mContentTextView.setMaxLines(mMaxLine);             // 设置文本控件的最大允许显示行数
            mIsExpansion = false;
        } else {
            //行数没有超过限制, 不需要显示 "全文/收起" 按钮
            mExpansionButton.setVisibility(GONE);

        }

    }

    @Override
    public void onGlobalLayout() {

        if (mContentTextView.getWidth() == 0) {
            return;
        }
        mContentTextView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        // 文本控件绘制成功后, 更新文本
        setText(mContent);
    }
}
