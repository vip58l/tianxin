package com.tianxin.activity.video.live.frags;

import androidx.fragment.app.Fragment;

/**
 * 用户交互页
 *
 * @author 刘洋巴金
 * @date 2017-5-3
 */
public class LayerFrag extends Fragment {

/*

    @Override
    public void initView() {
        super.initView();
        lv_message = (ListView) view.findViewById(R.id.lv_message);
        hlv_audience = (HorizontalListView) view.findViewById(R.id.hlv_audience);
        ll_gift_group = (LinearLayout) view.findViewById(R.id.ll_gift_group);
        ll_inputparent = (LinearLayout) view.findViewById(R.id.ll_inputparent);
        tv_chat = (Button) view.findViewById(R.id.tv_chat);
        et_chat = (EditText) view.findViewById(R.id.et_chat);
        ll_anchor = (LinearLayout) view.findViewById(R.id.ll_anchor);
        rl_num = (RelativeLayout) view.findViewById(R.id.rl_num);
    }



 if (ll_inputparent.getVisibility() == View.VISIBLE) {
        tv_chat.setVisibility(View.VISIBLE);
        ll_inputparent.setVisibility(View.GONE);
        hideKeyboard();
    }

        // 软键盘监听
        SoftKeyBoardListener.setListener(getActivity(), new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {

            @Override
            public void keyBoardShow(int height) {*/
    /*软键盘显示：执行隐藏title动画，并修改listview高度和装载礼物容器的高度*//*


                // 输入文字时的界面退出动画
                AnimatorSet animatorSetHide = new AnimatorSet();
                ObjectAnimator leftOutAnim = ObjectAnimator.ofFloat(rl_num, "translationX", 0, -rl_num.getWidth());
                ObjectAnimator topOutAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", 0, -ll_anchor.getHeight());
                animatorSetHide.playTogether(leftOutAnim, topOutAnim);
                animatorSetHide.setDuration(300);
                animatorSetHide.start();

                // 改变listview的高度
                dynamicChangeListviewH(90);
                dynamicChangeGiftParentH(true);
            }

            @Override
            public void keyBoardHide(int height) {*/
    /*软键盘隐藏：隐藏聊天输入框并显示聊天按钮，执行显示title动画，并修改listview高度和装载礼物容器的高度*//*


                tv_chat.setVisibility(View.VISIBLE);
                ll_inputparent.setVisibility(View.GONE);

                // 输入文字时的界面进入时的动画
                AnimatorSet animatorSetShow = new AnimatorSet();
                ObjectAnimator leftInAnim = ObjectAnimator.ofFloat(rl_num, "translationX", -rl_num.getWidth(), 0);
                ObjectAnimator topInAnim = ObjectAnimator.ofFloat(ll_anchor, "translationY", -ll_anchor.getHeight(), 0);
                animatorSetShow.playTogether(leftInAnim, topInAnim);
                animatorSetShow.setDuration(300);
                animatorSetShow.start();

                // 改变listview的高度
                dynamicChangeListviewH(150);
                dynamicChangeGiftParentH(false);
            }
        });




    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_chat:
                // 聊天
                tv_chat.setVisibility(View.GONE);
                ll_inputparent.setVisibility(View.VISIBLE);
                ll_inputparent.requestFocus();
                // 获取焦点
                showKeyboard();
                break;
            case R.id.tv_send:
                // 发送消息
                String chatMsg = et_chat.getText().toString();
                if (!TextUtils.isEmpty(chatMsg)) {
                    messageData.add("刘洋巴金: " + chatMsg);
                    et_chat.setText("");
                    messageAdapter.NotifyAdapter(messageData);
                    lv_message.setSelection(messageData.size());
                }
                hideKeyboard();
                break;
            case R.id.btn_gift01:
                // 礼物1,送香皂
                showGift("gift01");
                break;
            case R.id.btn_gift02:
                // 礼物2,送玫瑰
                showGift("gift02");
                break;
            case R.id.btn_gift03:
                // 礼物3,送爱心
                showGift("gift03");
                break;
            case R.id.btn_gift04:
                // 礼物4,送蛋糕
                showGift("gift04");
                break;
        }
    }





    */
/**
 * 隐藏软键盘
 *//*

    public void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_chat.getWindowToken(), 0);
    }

    */
/**
 * 显示软键盘
 *//*

    private void showKeyboard() {

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_chat, InputMethodManager.SHOW_FORCED);
    }

    */
/**
 * 动态的修改listview的高度
 *//*

    private void dynamicChangeListviewH(int heightPX) {
        ViewGroup.LayoutParams layoutParams = lv_message.getLayoutParams();
        layoutParams.height = DisplayUtil.dip2px(getActivity(), heightPX);
        lv_message.setLayoutParams(layoutParams);
    }

    */
/**
 * 动态修改礼物父布局的高度
 *//*

    private void dynamicChangeGiftParentH(boolean showhide) {

        if (showhide) {// 如果软键盘显示中

            if (ll_gift_group.getChildCount() != 0) {

                // 判断是否有礼物显示，如果有就修改父布局高度，如果没有就不作任何操作
                ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
                layoutParams.height = ll_gift_group.getChildAt(0).getHeight();
                ll_gift_group.setLayoutParams(layoutParams);
            }
        } else {
            // 如果软键盘隐藏中
            // 就将装载礼物的容器的高度设置为包裹内容
            ViewGroup.LayoutParams layoutParams = ll_gift_group.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ll_gift_group.setLayoutParams(layoutParams);
        }
    }
}*/
}
