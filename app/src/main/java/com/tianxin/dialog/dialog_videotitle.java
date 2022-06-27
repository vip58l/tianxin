/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/4/28 0028
 */


package com.tianxin.dialog;

import static com.blankj.utilcode.util.StringUtils.getString;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.Module.api.videotitle;
import com.tianxin.Module.Datamodule;
import com.tianxin.Util.Constants;
import com.tencent.opensource.model.videolist;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.Toashow;
import com.tianxin.listener.Paymnets;
import com.tianxin.app.DemoApplication;
import com.tencent.opensource.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class dialog_videotitle extends BottomSheetDialog {
    String TAG = dialog_videotitle.class.getSimpleName();
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.edittext1)
    EditText editText1;
    @BindView(R.id.edittext2)
    EditText editText2;
    @BindView(R.id.gt1)
    TextView gt1;
    @BindView(R.id.gt2)
    TextView gt2;
    @BindView(R.id.gt3)
    TextView gt3;

    private String name, nametitle;
    private int checked, TYPE, jinbi;
    private Paymnets payments;
    private videolist videolist;
    private Activity activity;
    private List<videotitle> list;
    private UserInfo userInfo;
    private Datamodule datamodule;
    private List<String> alert = new ArrayList<>();

    public void setVideolist(videolist videolist) {
        this.videolist = videolist;
    }

    public void setPayments(Paymnets payments) {
        this.payments = payments;
    }

    public dialog_videotitle(@NonNull Context context, videolist videolist) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_msg_view);
        activity = (Activity) context;
        this.videolist = videolist;
        datamodule = new Datamodule(context);
        ButterKnife.bind(this);
        userInfo = UserInfo.getInstance();
        Glideload.loadImage(icon, videolist.getTencent() == Constants.TENCENT ? DemoApplication.presignedURL(videolist.getBigpicurl()) : videolist.getBigpicurl(), 6);

        editText1.setText(videolist.getTitle());
        editText2.setText(videolist.getAlias());
        gt1.setText(videolist.getFltitle());
        gt2.setText(videolist.getType() == 0 ? activity.getString(R.string.tv_msg25) : activity.getString(R.string.tv_msg26));
        gt3.setText(String.valueOf(videolist.getJinbi()));
        datamodule.dialogvideotitle(paymnets2);
    }

    @OnClick({R.id.sendbtn, R.id.layout1, R.id.layout2, R.id.layout3, R.id.img_close})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.sendbtn:
                datamodule.dialogvideotitle(editText1.getText().toString(), editText2.getText().toString(), videolist, paymnets1);
                break;
            case R.id.layout1:
                showSingleAlertDialog2();
                break;
            case R.id.layout2:
                showSingleAlertDialog1();
                break;
            case R.id.layout3:
                showSingleAlertDialog3();
                break;
        }
    }

    public static void dialog_videotitle(Context context, videolist videolist, Paymnets payments) {
        dialog_videotitle dialogVideotitle = new dialog_videotitle(context, videolist);
        dialogVideotitle.setPayments(payments);
        dialogVideotitle.show();
    }

    /**
     * 条件选择器
     */
    private void initTimePicker(List<String> mOptionsItems1, int result) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(activity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String s = mOptionsItems1.get(options1);

                if (result == 0) {
                    gt2.setText(s);
                }

            }
        })
                .setSubmitColor(activity.getResources().getColor(R.color.c_fu))
                .setCancelColor(activity.getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(activity.getResources().getColor(R.color.c_fu))
                .setTitleText(activity.getString(R.string.tv_msg24))
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    /**
     * 单选弹窗提示消息
     */
    public void showSingleAlertDialog1() {
        TYPE = videolist.getType();
        final String[] items = {activity.getString(R.string.tv_msg25), activity.getString(R.string.tv_msg26)};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle(activity.getString(R.string.tv_msg27));
        alertBuilder.setCancelable(false);
        alertBuilder.setSingleChoiceItems(items, TYPE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TYPE = i;
                name = items[i];
            }
        });
        alertBuilder.setPositiveButton(activity.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                gt2.setText(name);
                videolist.setType(TYPE);
                Log.d(TAG, "onClick: " + i);
            }
        });
        alertBuilder.setNegativeButton(activity.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Log.d(TAG, "onClick: 取消" + i);

            }
        });
        alertBuilder.show();
    }

    /**
     * 单选弹窗提示消息
     */
    public void showSingleAlertDialog2() {
        if (list == null) {
            return;
        }
        int checkedItem = 0;
        int fenleijb = 0;
        alert.clear();
        for (videotitle videotitle : list) {
            alert.add(videotitle.getTitle());
            if (videolist.getFenleijb() == videotitle.getId()) {
                checkedItem = fenleijb;
            }
            fenleijb++;
        }
        String[] items = alert.toArray(new String[alert.size()]);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle(activity.getString(R.string.tv_msg28));
        alertBuilder.setCancelable(false);
        alertBuilder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nametitle = items[i];
                checked = list.get(i).getId();
            }
        });
        alertBuilder.setPositiveButton(activity.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                gt1.setText(nametitle);
                videolist.setFenleijb(checked);
            }
        });
        alertBuilder.setNegativeButton(activity.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Log.d(TAG, "onClick: 取消" + i);

            }
        });
        alertBuilder.show();
    }

    /**
     * 单选弹窗提示消息
     */
    public void showSingleAlertDialog3() {
        int ii = 0;
        String[] items = getContext().getResources().getStringArray(R.array.tabs2);
        for (String sitem : items) {
            if (sitem.equals(String.valueOf(videolist.getJinbi()))) {
                break;
            }
            ii++;
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setTitle(activity.getString(R.string.tv_msgs277));
        alertBuilder.setCancelable(false);
        alertBuilder.setSingleChoiceItems(items, ii, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "onClick: 选中" + items[i]);
                jinbi = Integer.valueOf(items[i]);

            }
        });
        alertBuilder.setPositiveButton(activity.getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                videolist.setJinbi(jinbi);
                gt3.setText(String.valueOf(videolist.getJinbi()));
                Log.d(TAG, "onClick: 确定" + i);

            }
        });
        alertBuilder.setNegativeButton(activity.getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Log.d(TAG, "onClick: 取消" + i);

            }
        });
        alertBuilder.show();
    }

    public Paymnets paymnets1 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess() {
            dialog_videotitle.this.dismiss();
            payments.onSuccess();


        }

        @Override
        public void onError() {
            Toashow.show("标题内容不能空");
        }
    };

    public Paymnets paymnets2 = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(Object object) {
            list = (List<videotitle>) object;
        }
    };


}
