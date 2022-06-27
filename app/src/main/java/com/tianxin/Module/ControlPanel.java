package com.tianxin.Module;

import android.content.Context;
import android.content.Intent;

import com.tianxin.activity.activity_music_play;
import com.tencent.liteav.TUIVoiceRoomLogin;
import com.tencent.liteav.trtcvoiceroom.model.TRTCVoiceRoom;
import com.tencent.opensource.model.LiveRoom;

/**
 * 音频聊天室控制面板
 */
public class ControlPanel {
    private Context context;
    private TUIVoiceRoomLogin tuiVoiceRoomLogin;
    private TRTCVoiceRoom mTRTCVoiceRoom;
    private com.tencent.liteav.trtcvoiceroom.ui.widget.AudioEffectPanel audioEffectPanel1;
    private com.tencent.liteav.audiosettingkit.AudioEffectPanel audioEffectPanel2;

    public ControlPanel(Context context) {
        this.context = context;
        mTRTCVoiceRoom = TRTCVoiceRoom.sharedInstance(context);
        tuiVoiceRoomLogin = new TUIVoiceRoomLogin(context);

        audioEffectPanel1 = new com.tencent.liteav.trtcvoiceroom.ui.widget.AudioEffectPanel(context);
        audioEffectPanel1.setAudioEffectManager(mTRTCVoiceRoom.getAudioEffectManager());
        audioEffectPanel1.setTRTCVoiceRoom(mTRTCVoiceRoom);

        audioEffectPanel2 = new com.tencent.liteav.audiosettingkit.AudioEffectPanel(context);
        audioEffectPanel2.setAudioEffectManager(mTRTCVoiceRoom.getAudioEffectManager());
    }

    public TUIVoiceRoomLogin getTuiVoiceRoomLogin() {
        return tuiVoiceRoomLogin;
    }

    /**
     * 聊天室音频音乐
     */
    public void audioEffectPanel1() {
        if (audioEffectPanel1 != null) {
            audioEffectPanel1.show();
        }
    }

    /**
     * 直播视频音乐
     */
    public void audioEffectPanel2() {
        if (audioEffectPanel2 != null) {
            audioEffectPanel2.show();
        }

    }

    /**
     * 创建音频聊天室
     */
    public void createRoom() {
        tuiVoiceRoomLogin.createRoom();
    }

    /**
     * 进入音频聊天室
     *
     * @param roomIdStr
     */
    public void enterRoom(String roomIdStr, LiveRoom liveRoom) {
        tuiVoiceRoomLogin.enterRoom(roomIdStr, liveRoom);
    }

    /**
     * 音乐播器打开
     */
    public void musicstartActivity() {
        Intent intent = new Intent(context, activity_music_play.class);
        context.startActivity(intent);
    }


}
