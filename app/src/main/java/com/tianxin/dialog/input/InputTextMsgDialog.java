package com.tianxin.dialog.input;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.tim.tuikit.live.R;

public class InputTextMsgDialog extends Dialog {
    private static final String TAG = InputTextMsgDialog.class.getName();

    private final TextView mTextConfirm;
    private final EditText mEditMessage;
    private final RelativeLayout mRelativeLayout;
    private final LinearLayout mBarrageArea;
    private final Context mContext;
    private final InputMethodManager mInputMethodManager;
    private OnTextSendDelegate mOnTextSendDelegate;
    private int mLastDiff = 0;
    private boolean mDanmuOpen = false;

    public interface OnTextSendDelegate {
        void onTextSend(String msg, boolean tanmuOpen);
    }

    public InputTextMsgDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        setContentView(R.layout.live_dialog_input_text);
        mEditMessage = (EditText) findViewById(R.id.et_input_message);
        mEditMessage.setInputType(InputType.TYPE_CLASS_TEXT);
       // mEditMessage.getBackground().setColorFilter(context.getResources().getColor(R.color.transparent), PorterDuff.Mode.CLEAR);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(com.tianxin.R.style.BottomDialog_Animation);

        //发送
        mTextConfirm = (TextView) findViewById(R.id.confirm_btn);
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mTextConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        //弹窗开启或关闭
        Button barrageBtn = (Button) findViewById(R.id.barrage_btn);
        barrageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.live_barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.live_barrage_slider_off);
                }
            }
        });
        mBarrageArea = (LinearLayout) findViewById(R.id.barrage_area);
        mBarrageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.live_barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.live_barrage_slider_off);
                }
            }
        });
        mEditMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        sendMessage();
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();
                        return false;
                    default:
                        return false;
                }
            }
        });

        //监听编辑框
        mEditMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("My test", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        //点击关闭
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_outside_view);
        mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() != R.id.rl_inputdlg_view)
                    dismiss();
            }
        });

        final LinearLayout rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    //imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    dismiss();
                }
                mLastDiff = heightDifference;
            }
        });
        rldlgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
                dismiss();
            }
        });
    }

    /**
     * 发送消息
     */
    private void sendMessage() {
        String msg = mEditMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(msg)) {
            //接口回调数据
            mOnTextSendDelegate.onTextSend(msg, mDanmuOpen);
            mInputMethodManager.showSoftInput(mEditMessage, InputMethodManager.SHOW_FORCED);
            mInputMethodManager.hideSoftInputFromWindow(mEditMessage.getWindowToken(), 0);
            mEditMessage.setText("");
            dismiss();
        } else {
            Toast.makeText(mContext, R.string.live_warning_not_empty, Toast.LENGTH_LONG).show();
        }
        mEditMessage.setText(null);
    }

    public void setTextSendDelegate(OnTextSendDelegate onTextSendDelegate) {
        this.mOnTextSendDelegate = onTextSendDelegate;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

    @Override
    public void show() {
        super.show();
        mEditMessage.setFocusable(true);
        mEditMessage.setFocusableInTouchMode(true);
        mEditMessage.requestFocus();
    }
}
