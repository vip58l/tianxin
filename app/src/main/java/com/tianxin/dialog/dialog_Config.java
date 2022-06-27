/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2020/12/27 0027
 */
package com.tianxin.dialog;

import android.content.Context;

import com.tianxin.listener.Paymnets;
import com.tianxin.R;
import com.tianxin.dialog.input.InputTextMsgDialog;
import com.tencent.qcloud.tim.tuikit.live.component.gift.GiftPanelDelegate;

//弹出种用消息内容
public class dialog_Config {

    private static final String TAG = dialog_Config.class.getSimpleName();

    public static void fenxing(Context context) {
        Dialog_fenxing dialogFenxing = new Dialog_fenxing(context);
        dialogFenxing.show();
    }

    public static dialog_pinglun Pinglun(Context context, String uid) {
        dialog_pinglun dialog_pinglun = new dialog_pinglun(context, uid);
        dialog_pinglun.show();
        return dialog_pinglun;

    }

    public static void dialog_jibao(Context context) {
        dialog_jibao dialogJibao = new dialog_jibao(context);
        dialogJibao.show();
    }

    /**
     * 红娘申请
     *
     * @param context
     * @return
     */
    public static Dialog_mesges2 Dialogexit2(Context context) {
        Dialog_mesges2 dialogMesges2 = new Dialog_mesges2(context);
        dialogMesges2.setCancelable(false);
        dialogMesges2.setTv1("申请当红娘,希望官方审核通过");
        dialogMesges2.setTv2("");
        dialogMesges2.setKankan("申请");
        dialogMesges2.setExit("取消");
        dialogMesges2.show();
        return dialogMesges2;
    }

    /**
     * 金币充值
     *
     * @param context
     */
    public static void dialogBottom(Context context) {
        Dialog_bottom dialogBottom = new Dialog_bottom(context);
        dialogBottom.setCanceledOnTouchOutside(false);
        dialogBottom.show();

    }

    /**
     * 金币充值
     *
     * @param context
     * @param paymnets
     */
    public static void dialogBottom(Context context, Paymnets paymnets) {
        Dialog_bottom dialogBottom = new Dialog_bottom(context);
        dialogBottom.setPaymnets(paymnets);
        dialogBottom.show();

    }

    /**
     * 确认提示
     *
     * @param context
     */
    public static void dialog_paymnet(Context context) {
        dialog_paymnet dialogPaymnet = new dialog_paymnet(context);
        dialogPaymnet.setCanceledOnTouchOutside(false);
        dialogPaymnet.show();

    }

    public static void dialoginutp(Context context, InputTextMsgDialog.OnTextSendDelegate onTextSendDelegate) {
        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.LiveInputDialog);
        inputTextMsgDialog.setCanceledOnTouchOutside(false);
        inputTextMsgDialog.setTextSendDelegate(onTextSendDelegate);
        inputTextMsgDialog.show();
    }

    /**
     * 礼物
     *
     * @param context
     * @param onGiftItemClick
     * @return
     */
    public static dialog_item_gift dialogitemactyvity(Context context, GiftPanelDelegate onGiftItemClick) {
        dialog_item_gift dialog_item_gift = new dialog_item_gift(context);
        dialog_item_gift.setGiftPanelDelegate(onGiftItemClick);
        dialog_item_gift.setCanceledOnTouchOutside(false);
        dialog_item_gift.show();
        return dialog_item_gift;
    }

    /**
     * 游戏显示1
     *
     * @param context
     * @return
     */
    public static dialog_game dialog_game(Context context) {
        dialog_game dialogGame = new dialog_game(context);
        return dialogGame;
    }

    /**
     * 退出直播间
     *
     * @param context
     * @return
     */
    public static dialog_game exitdialoggame(Context context) {
        dialog_game dialogGame = new dialog_game(context);
        return dialogGame;
    }

}
