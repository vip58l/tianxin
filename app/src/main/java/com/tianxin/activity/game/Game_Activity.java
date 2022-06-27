package com.tianxin.activity.game;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.IMtencent.chta.ChatActivity;
import com.tianxin.R;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Config;
import com.tianxin.activity.picenter.activity_picenter;
import com.tianxin.widget.Backtitle;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.opensource.model.Gamelist;
import com.tencent.opensource.model.Gametitle;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**游戏下单
 *
 */
public class Game_Activity extends BasActivity2 {
    private static final String TAG = Game_Activity.class.getSimpleName();
    @BindView(R.id.backtitle)
    Backtitle backtitle;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.ttitle)
    TextView ttitle;
    @BindView(R.id.msg_content)
    TextView msg_content;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.msg_content_ll)
    TextView msg_content_ll;
    private Gamelist gamelist;
    private com.tencent.opensource.model.member member;
    private Gametitle gametitle;
    private String json;

    @Override
    protected int getview() {
        return R.layout.activity_game;
    }

    @Override
    public void iniview() {
        json = getIntent().getStringExtra(Constants.JSON);
        gamelist = gson.fromJson(json, Gamelist.class);
        gametitle = gamelist.getGametitle();
        member = gamelist.getMember();
        backtitle.setconter(getString(R.string.tv_msg_tm6));

        name.setText(gamelist.getMember().getTruename());
        msg.setText(String.format("ID:%s", member.getId()));
        ttitle.setText(gamelist.getName());
        msg_content.setText(gamelist.getDescshow());
        money.setText(String.format(getString(R.string.tv_msg_tmhdate) + "", gamelist.getMoney()));
        msg_content_ll.setText(gamelist.getMsg().equals("") ? "" : gamelist.getMsg());
        if (!TextUtils.isEmpty(gametitle.getPic())) {
            Glide.with(context).load(gametitle.getPic()).into(image1);
        }
        if (!TextUtils.isEmpty(gamelist.getMember().getPicture())) {
            Glide.with(context).load(gamelist.getMember().getPicture()).into(image);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    @OnClick({R.id.onecon, R.id.buttion1, R.id.buttin2})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.onecon:
                tostartActivity(activity_picenter.class, String.valueOf(member.getId()));
                break;
            case R.id.buttion1:
                startChatActivity();
                break;
            case R.id.buttin2:
                if (!TextUtils.isEmpty(json)) {
                    if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
                        ToastUtil.toastShortMessage(getString(R.string.tm29));
                        return;
                    }
                    tostartActivity(Game_bin.class, userInfo.getUserId(), json);
                }
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

    /**
     * 这里需要用户上传头像才能聊天
     */
    private void startChatActivity() {
        if (member == null) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali3));
            return;
        }
        if (!Config.isNetworkAvailable()) {
            ToastUtil.toastLongMessage(getString(R.string.eorrfali2));
            return;
        }
        if (userInfo.getUserId().equals(String.valueOf(member.getId()))) {
            ToastUtil.toastShortMessage(getString(R.string.tv_msg132));
            return;
        }

        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_C2C);
        chatInfo.setId(String.valueOf(member.getId()));
        chatInfo.setChatName(member.getTruename());
        chatInfo.setIconUrlList(TextUtils.isEmpty(member.getPicture()) ? "" : member.getPicture());
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}