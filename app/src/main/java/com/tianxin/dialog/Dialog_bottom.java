package com.tianxin.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseBottomSheetDialog;
import com.tianxin.activity.Aboutus.activity_viecode;
import com.tianxin.activity.Withdrawal.References;
import com.tianxin.adapter.setAdapter;
import com.tianxin.activity.Web.DyWebActivity;
import com.tianxin.utils.Constants;
import com.tencent.opensource.model.UserInfo;
import com.tencent.opensource.model.item;
import com.tianxin.Module.api.moneylist;
import com.tianxin.R;
import com.tianxin.activity.activity_svip;
import com.tianxin.listener.Paymnets;
import com.tencent.qcloud.tim.tuikit.live.BuildConfig;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

import androidx.annotation.NonNull;

/**
 * 购买VIP服务
 * BottomSheetDialogFragment
 * BottomSheetDialog
 * Dialog
 */
public class Dialog_bottom extends BaseBottomSheetDialog implements AdapterView.OnItemClickListener {
    @BindView(R.id.radio_alipay)
    RadioButton radio_alipay;
    @BindView(R.id.radio_weixin)
    RadioButton radio_weixin;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.title_template3)
    TextView title_template3;
    @BindView(R.id.alipay)
    RelativeLayout alipay;
    @BindView(R.id.weixing)
    RelativeLayout weixing;
    @BindView(R.id.payment)
    TextView payment;
    String TAG = Dialog_bottom.class.getSimpleName();

    public static void dialogsBottom(Context context, Paymnets paymnets) {
        Dialog_bottom dialog_bottom = new Dialog_bottom(context);
        dialog_bottom.setPaymnets(paymnets);
        dialog_bottom.show();
    }

    public Dialog_bottom(@NonNull Context context) {
        super(context);
        UserInfo userInfo = UserInfo.getInstance();
        if (userInfo.getZfboff() == 1) {
            alipay.setVisibility(View.GONE);
        }
        if (userInfo.getWxoff() == 1) {
            weixing.setVisibility(View.GONE);
        }
        if (userInfo.getZfboff() == 1 && userInfo.getWxoff() == 1) {
            payment.setVisibility(View.GONE);
            ToastUtil.toastLongMessage(context.getString(R.string.pay_m1) + "");
        }

        gridview.setAdapter(setAdapter = new setAdapter(getContext(), list));
        gridview.setOnItemClickListener(this);
        //点击转换并打开连接
        interceptHyperLink(findViewById(R.id.tv_privacy));
        moneylist();
    }

    @Override
    protected int layoutview() {
        return R.layout.activity_jbdialog;
    }

    @Override
    @OnClick({R.id.payment,
            R.id.radio_alipay,
            R.id.alipay,
            R.id.radio_weixin,
            R.id.weixing,
            R.id.title_template3,
            R.id.title_templateff,
            R.id.title_templatejb,
            R.id.tv_privacy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipay:
            case R.id.radio_alipay: {
                radio_alipay.setChecked(true);
                radio_weixin.setChecked(false);
                TYPE = activity_svip.zfb;
                break;
            }
            case R.id.weixing:
            case R.id.radio_weixin: {
                radio_alipay.setChecked(false);
                radio_weixin.setChecked(true);
                TYPE = activity_svip.wx;
                break;
            }
            case R.id.title_template3: {
                datetime();
                break;
            }
            case R.id.title_templateff: {
                context.startActivity(new Intent(getContext(), activity_svip.class));
                break;
            }
            case R.id.title_templatejb: {
                context.startActivity(new Intent(getContext(), References.class));
                break;
            }
            case R.id.payment: {
                dismiss();
                if (paymnets != null) {
                    if (moneylist != null) {
                        paymnets.payonItemClick(moneylist, TYPE);
                    }

                }
                break;
            }
            case R.id.tv_privacy:
                String url = BuildConfig.HTTP_API + "/invitefriends?type=%s&userid=" + UserInfo.getInstance().getUserId() + "&token=" + UserInfo.getInstance().getToken();
                activity_viecode.WebbookUrl(context, String.format(url, 7));
                break;

        }
    }

    /**
     * 用于跳转隐私协议
     *
     * @param tv
     */
    private void interceptHyperLink(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
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
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannableStringBuilder);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getnotifyDataSetChanged(position);
    }

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
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse(url));
//            context.startActivity(intent);

            Intent intent = new Intent(getContext(), DyWebActivity.class);
            intent.putExtra(Constants.VIDEOURL, url);
            startActivity(intent);
        }
    }

    private void datetime() {
        if (vlist == null) {
            return;
        }
        list.clear();
        if (!ok && vlist.size() > 3) {
            title_template3.setText(R.string.tv_msg179);
            ok = true;
            for (int i = 0; i < vlist.size(); i++) {
                moneylist moneylist = vlist.get(i);
                item item = new item();
                item.object = moneylist;
                item.type = com.tianxin.adapter.setAdapter.svip2;
                list.add(item);
            }
        } else {
            title_template3.setText(R.string.tv_msg178);
            ok = false;
            for (int i = 0; i < 3; i++) {
                moneylist moneylist = vlist.get(i);
                item item = new item();
                item.object = moneylist;
                item.type = com.tianxin.adapter.setAdapter.svip2;
                list.add(item);
            }

        }
        setAdapter.notifyDataSetChanged();

    }

    /**
     * 设置默认值
     *
     * @param position
     */
    private void getnotifyDataSetChanged(int position) {
        setAdapter.setClickPosition(position);
        setAdapter.notifyDataSetChanged();
        moneylist = vlist.get(position);
    }

    /**
     * 联网获取数据
     */
    private void moneylist() {
        datamodule.moneylist(new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(Object object) {
                vlist = (List<com.tianxin.Module.api.moneylist>) object;
                int conunt = vlist.size() >= 3 ? 3 : vlist.size(); //默认只显示3条记录
                for (int i = 0; i < conunt; i++) {
                    moneylist moneylist = vlist.get(i);
                    item item = new item();
                    item.object = moneylist;
                    item.type = com.tianxin.adapter.setAdapter.svip2;
                    list.add(item);
                }
                getnotifyDataSetChanged(2);//默认选择第2个itemview
            }
        });
    }
}

