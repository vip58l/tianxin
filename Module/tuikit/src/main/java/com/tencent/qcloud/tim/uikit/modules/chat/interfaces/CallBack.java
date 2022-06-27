package com.tencent.qcloud.tim.uikit.modules.chat.interfaces;

public interface CallBack {
    /**
     * 禁止无权限
     */
    void onsuccess();

    /**
     * 金币不足
     */
    void onmoney();

    /**
     * 发送失败
     */
    void onfall();

    /**
     * 是否有上传头像
     */
    void avatar();

    /**
     * 打音视频电话
     * @param object
     * @param TYPE
     */
    void videoCall(Object object, int TYPE);

    /**
     * 封禁
     */
    void fengjin();

    /**
     * 限制聊天数次
     */
    void chta();

    /**
     * 消息成功发送
     */
    void sendMessage();

    /**
     * 扣除金币操作 返回对方和ID号
     */
    void reducemoney();

    /**
     * 首次弹出需要扣除金币
     */
    void moneyshow();

    /**
     * 补全资料
     */
    void Completedata();

    /**
     * 发送图片
     */
    void Sendpictures();

    /**
     * 发送视频
     */
    void Sendvideo();

    /**
     * 发送语音
     */
    void Sendvoice();

    /**
     * 发送位置
     */
    void Sendlocation();

    /**
     * 发送礼物
     */
    void Sendgifts();
}
