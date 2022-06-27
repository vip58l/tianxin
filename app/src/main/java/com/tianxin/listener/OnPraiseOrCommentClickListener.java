package com.tianxin.listener;

/**
 * @author zhaojin
 * @date 2018/8/10
 */
public interface OnPraiseOrCommentClickListener {
    void onPraiseClick(int position);

    void onCommentClick(int position);

    void onClickFrendCircleTopBg(int position);

    void onDeleteItem(int position);
}
