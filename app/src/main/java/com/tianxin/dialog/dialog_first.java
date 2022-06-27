package com.tianxin.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseDialog;
import com.tianxin.R;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.getHandler.Webrowse;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;

import butterknife.BindView;
import butterknife.OnClick;

public class dialog_first extends BaseDialog {
    @BindView(R.id.msg)
    TextView msg;

    public dialog_first(Context context, Paymnets paymnets) {
        super(context, paymnets);
        interceptHyperLink(msg); //点击转换并打开连接
    }

    @Override
    public int getview() {
        return R.layout.dialog_first;
    }

    @OnClick({R.id.send1, R.id.send2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.send1:
                paymnets.onFail();
                break;
            case R.id.send2:
                paymnets.onSuccess();
                break;
        }
        dismiss();
    }

    public static void key(Context c, Paymnets p) {
        dialog_first dialog_first = new dialog_first(c, p);
        dialog_first.setCanceledOnTouchOutside(false);
        dialog_first.show();
    }

    /**
     * 用于跳转隐私协议
     * 拦截超链接
     *
     * @param tv
     */
    private void interceptHyperLink(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
            //NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();
            //spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            // 循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                if (url.indexOf("https://") == 0 || url.indexOf("http://") == 0) {
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(getContext(), url);
                    //给链接设置样式等，例如链接处的下划线，字体颜色等，及其单击事件的添加
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri), spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.removeSpan(uri);//解决方法 不然ClickableSpan会无效无法拦截
                }
            }
            tv.setText(spannableStringBuilder);
        }
    }


    /**
     * // 在这里可以做任何自己想要的处理
     */
    public class CustomUrlSpan extends ClickableSpan {
        private final Context context;
        private final String url;

        public CustomUrlSpan(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 在这里可以做任何自己想要的处理
            //Intent intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(Uri.parse(url));
            //context.startActivity(intent);

            int intIndex1 = url.indexOf("abc1");
            int intIndex2 = url.indexOf("abc2");
            int type = 0;
            if (intIndex1 > 0) {
                type = 3;
            }
            if (intIndex2 > 0) {
                type = 4;
            }

            String format = String.format(Webrowse.invitefriends + "?type=%s&userid=%s", type, UserInfo.getInstance().getUserId());
            Intent intent = new Intent(context, DyWebActivity.class);
            intent.putExtra("videoUrl", format);
            getContext().startActivity(intent);
        }
    }

    /**
     * 去除超链接的下划线
     */
    public class NoUnderlineSpan extends UnderlineSpan {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }
    }


}
