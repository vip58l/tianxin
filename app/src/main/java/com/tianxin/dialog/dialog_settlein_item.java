/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/14 0014
 */


package com.tianxin.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianxin.Module.Datamodule;
import com.tianxin.Util.Glideload;
import com.tencent.opensource.model.Mesresult;
import com.tianxin.R;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;
import com.tencent.opensource.model.UserInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公会申请dialog
 */
public class dialog_settlein_item extends BottomSheetDialog {
    String TAG = dialog_settlein_item.class.getSimpleName();
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.editshow)
    EditText editshow;
    private final Paymnets paymnets;
    private final UserInfo userInfo;
    private int uid = 1;
    private int TYPE = 0;
    private final String[] guild;
    private Mesresult mesresult;
    private String name;

    public dialog_settlein_item(@NonNull Context context, Paymnets paymnets) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.dialog_settlein_iem);
        ButterKnife.bind(this);
        this.paymnets = paymnets;
        userInfo = UserInfo.getInstance();
        guild = getContext().getResources().getStringArray(R.array.guild);
        if (!TextUtils.isEmpty(userInfo.getAvatar())) {
            Glideload.loadImage(icon, userInfo.getAvatar(), 8);
        }

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        Datamodule.getInstance().getrmard(true, new Paymnets() {
            @Override
            public void onSuccess(Object object) {
                mesresult = (Mesresult) object;
                String data = mesresult.getData();
                String ok = mesresult.getOk();
                editshow.setText(data);
                if (!TextUtils.isEmpty(data)) {
                    editshow.setEnabled(false);
                }
                switch (Integer.parseInt(ok)) {
                    case 1:
                        tv1.setText(guild[0]);
                        uid = 1;
                        break;
                    case 2:
                        tv1.setText(guild[1]);
                        uid = 2;
                        break;
                    default:
                        tv1.setText(R.string.tm123);
                        break;
                }
            }

            @Override
            public void onFail() {

            }
        });
    }

    @OnClick({R.id.sendbtn, R.id.img_close, R.id.tv1})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.sendbtn:
                if (TextUtils.isEmpty(getstredit())) {
                    ToastUtil.toastShortMessage(getContext().getString(R.string.tv_msg30));
                    return;
                }
                gettext(getstredit());
                break;
            case R.id.tv1:
                showSingleAlertDialog1();
                break;
        }

    }

    /**
     * 单选弹窗提示消息
     */
    public void showSingleAlertDialog1() {
        final String[] items = getContext().getResources().getStringArray(R.array.arr);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder.setTitle(getContext().getString(R.string.tv_msg03));
        alertBuilder.setCancelable(false);
        alertBuilder.setSingleChoiceItems(items, (uid - 1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                name = items[i];
                TYPE = i;
            }
        });
        alertBuilder.setPositiveButton(getContext().getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                tv1.setText(name);
                uid = TYPE + 1;
                Log.d(TAG, "onClick: " + uid);
            }
        });
        alertBuilder.setNegativeButton(getContext().getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Log.d(TAG, "onClick: 取消" + i);
            }
        });
        alertBuilder.show();
    }

    /**
     * 同意失败
     *
     * @param context
     * @param p
     * @return
     */
    public static dialog_settlein_item dialogettleinitem(Context context, Paymnets p) {
        dialog_settlein_item dialog_settlein_item = new dialog_settlein_item(context, p);
        dialog_settlein_item.show();
        return dialog_settlein_item;
    }

    /**
     * 提交申请内容
     *
     * @param msg
     */
    private void gettext(String msg) {
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastShortMessage(getContext().getResources().getString(R.string.eorrfali2));
            return;
        }
        Datamodule.getInstance().addrmard(uid, msg, new Paymnets() {
            @Override
            public void isNetworkAvailable() {

            }

            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess() {
                dismiss();
            }
        });
    }

    /**
     * 提交数据
     *
     * @return
     */
    private String getstredit() {
        String trim = editshow.getText().toString().trim();
        return trim;
    }
}
