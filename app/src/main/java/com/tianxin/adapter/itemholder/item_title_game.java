package com.tianxin.adapter.itemholder;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianxin.BasActivity.BaseHolder;
import com.tianxin.R;
import com.tianxin.Util.Glideload;
import com.tianxin.Util.glide.ImageLoadHelper;
import com.tianxin.app.DemoApplication;
import com.tianxin.listener.Callback;
import com.tencent.opensource.model.Gamefinish;
import com.tencent.opensource.model.Gamelist;
import com.tencent.opensource.model.Gametitle;
import com.tencent.opensource.model.Party;
import com.tencent.opensource.model.member;
import com.tencent.opensource.model.partyname2;

import butterknife.BindView;

public class item_title_game extends BaseHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.msg)
    TextView msg;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.money2)
    TextView money2;
    @BindView(R.id.update)
    TextView update;
    @BindView(R.id.delete1)
    TextView delete1;
    @BindView(R.id.svip)
    TextView svip;
    private boolean is = false;

    public item_title_game(Context content, ViewGroup parent) {
        super(LayoutInflater.from(content).inflate(R.layout.item_text_title_game, parent, false));
    }

    @Override
    public void bind(Object object, int position, Callback callback) {
        Gamelist gamelist = (Gamelist) object;
        member member = gamelist.getMember();
        Gametitle gametitle = gamelist.getGametitle();
        if (!TextUtils.isEmpty(member.getPicture())) {
            Glideload.loadImage(image, member.getPicture(), 6);
        }
        if (!TextUtils.isEmpty(gametitle.getPic())) {
            Glideload.loadImage(image2, gametitle.getPic());
        }
        update.setVisibility(View.GONE);
        name.setText(member.getTruename());
        if (member.getCity() != null) {
            title.setText(String.format("%s %s.%s", gamelist.getName(), member.getCity(), member.getDistrict()));
        } else {
            title.setText(gamelist.getName());
        }
        msg.setText(gamelist.getDescshow());
        money.setText(String.format(DemoApplication.instance().getString(R.string.tv_msg_tmhdate), String.valueOf(gamelist.getMoney())));
        if (callback != null) {
            money2.setOnClickListener(v -> {
                callback.LongClickListener(position);
                is = true;
            });

            if (!is) {
                itemView.setOnClickListener(v -> callback.OnClickListener(position));
            }

        }
    }

    @Override
    public void bind(Context context, Object object, int position, Callback callback) {
        delete1.setVisibility(View.GONE);
        money2.setVisibility(View.GONE);
        Gamefinish gamefinish = (Gamefinish) object;
        member member2 = gamefinish.getMember2();
        Gametitle gametitle = gamefinish.getGametitle();
        if (!TextUtils.isEmpty(member2.getPicture())) {
            Glideload.loadImage(image, member2.getPicture(), 6);
        }
        if (!TextUtils.isEmpty(gametitle.getPic())) {
            Glideload.loadImage(image2, gametitle.getPic());
        }
        name.setText(gamefinish.getName());
        title.setText(context.getString(R.string.tm52) + " " + gamefinish.getMsg());
        money.setText(String.format(context.getString(R.string.tm51) + "", gamefinish.getTotal(), gamefinish.getQuantity()));
        msg.setText(gamefinish.getName());
        switch (gamefinish.getOk()) {
            case 0:
                update.setText(R.string.tm53);
                update.setBackground(DemoApplication.instance().getDrawable(R.drawable.activity011));
                break;
            case 1:
                update.setText(R.string.tm54);
                update.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity017));
                break;
            default:
                update.setVisibility(View.GONE);
                break;


        }

        if (callback != null) {
            itemView.setOnClickListener(v -> callback.OnClickListener(position));
        }

    }

    public void bind(Context context, Object object, int position, boolean delshow, Callback callback) {
        Gamelist gamelist = (Gamelist) object;
        member member = gamelist.getMember();
        Gametitle gametitle = gamelist.getGametitle();
        if (member == null) {
            return;
        }
        if (member.getPicture() != null) {
            Glideload.loadImage(image, member.getPicture(), 6);
        }
        if (!TextUtils.isEmpty(gametitle.getPic())) {
            Glideload.loadImage(image2, gametitle.getPic());
        }
        name.setText(member.getTruename());
        if (member.getCity() != null) {
            title.setText(String.format("%s %s.%s", gamelist.getName(), member.getCity(), member.getDistrict()));
        } else {
            title.setText(gamelist.getName());
        }
        msg.setText(gamelist.getDescshow());
        money.setText(String.format(DemoApplication.instance().getString(R.string.tv_msg_tmhdate), String.valueOf(gamelist.getMoney())));
        switch (gamelist.getStatus()) {
            case 0:
                update.setText(R.string.tm31);
                update.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity017));
                break;
            case 1:
                update.setText(R.string.tm32);
                update.setBackground(DemoApplication.instance().getDrawable(R.drawable.activity011));
                break;
            case 2:
                update.setText(R.string.tm33);
                update.setBackground(DemoApplication.instance().getDrawable(R.drawable.activity011));
                msg.setText(gamelist.getAlias());
                msg.setTextColor(Color.RED);
                break;
            default:
                break;


        }
        delete1.setVisibility(delshow ? View.VISIBLE : View.GONE);
        money2.setVisibility(delshow ? View.GONE : View.VISIBLE);
        if (callback != null) {
            money2.setOnClickListener(v -> {
                callback.LongClickListener(position);
                is = true;
            });
            delete1.setOnClickListener(v -> callback.OndeleteListener(position));
            if (!is) {
                itemView.setOnClickListener(v -> callback.OnClickListener(position));
            }

        }
    }

    /**
     * 我的聚会列表
     *
     * @param obj
     * @param position
     * @param callback
     * @param type
     */
    public void bind(Object obj, int position, Callback callback, int type) {
        Party party = (Party) obj;
        member member = party.getMember();
        name.setText(String.format("%s(限%s人)", party.getTitle(), party.getPartynumbe()));
        title.setText(party.getTdesc());
        msg.setText(party.getAddress());
        money.setVisibility(View.GONE);
        money2.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        svip.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(member.getPicture())) {
            if (TextUtils.isEmpty(member.getAvatar())) {
                Glideload.loadImage(image2, member.getAvatar());
            }
        } else {
            Glideload.loadImage(image2, member.getPicture());
        }
        if (!TextUtils.isEmpty(party.getCover())) {
            ImageLoadHelper.glideShowCornerImageWithUrl(context, party.getCover(), image);
        }

        if (party.getFinish() == 0) {
            switch (party.getStatus()) {
                case 0:
                    svip.setText(R.string.tm32);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.activity011));
                    break;
                case 1:
                    svip.setText(R.string.tm31);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity017));
                    break;
                case 2:
                    svip.setText(R.string.tm33);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity04));
                    break;
                default:
                    svip.setVisibility(View.GONE);
                    break;


            }
        } else {
            svip.setText(R.string.resp);
            svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity04));
        }

        if (callback != null) {
            this.position = position;
            this.callback = callback;
            itemView.setOnClickListener(this::OnClick);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    callback.LongClickListener(position);
                    return true;
                }
            });
        }


    }

    /**
     * 我的聚会列表
     *
     * @param obj
     * @param position
     * @param callback
     * @param type
     */
    public void bind(Object obj, int position, Callback callback, boolean type) {
        partyname2 p = (partyname2) obj;
        Party party = p.getParty();
        member member = p.getMember();
        name.setText(String.format("%s(限%s人)", party.getTitle(), party.getPartynumbe()));
        title.setText(party.getTdesc());
        msg.setText(party.getAddress());
        money.setVisibility(View.GONE);
        money2.setVisibility(View.GONE);
        update.setVisibility(View.GONE);
        svip.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(member.getPicture())) {
            if (TextUtils.isEmpty(member.getAvatar())) {
                Glideload.loadImage(image2, member.getAvatar());
            }
        } else {
            Glideload.loadImage(image2, member.getPicture());
        }
        if (!TextUtils.isEmpty(party.getCover())) {
            ImageLoadHelper.glideShowCornerImageWithUrl(context, party.getCover(), image);
        }
        if (party.getFinish() == 0) {
            switch (party.getStatus()) {
                case 0:
                    svip.setText(R.string.tm32);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.activity011));
                    break;
                case 1:
                    svip.setText(R.string.tm31);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity017));
                    break;
                case 2:
                    svip.setText(R.string.tm33);
                    svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity04));
                    break;
                default:
                    svip.setVisibility(View.GONE);
                    break;


            }
        } else {
            svip.setText(R.string.resp);
            svip.setBackground(DemoApplication.instance().getDrawable(R.drawable.acitvity04));
        }

        if (callback != null) {
            this.position = position;
            this.callback = callback;
            itemView.setOnClickListener(this::OnClick);
        }


    }

    @Override
    public void OnClick(View v) {
        callback.OnClickListener(position);

    }
}
