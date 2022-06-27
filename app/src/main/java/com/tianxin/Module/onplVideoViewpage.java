/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/14 0014
 */


package com.tianxin.Module;

import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnImageCapturedListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnSeekCompleteListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;

public abstract class onplVideoViewpage implements PLOnPreparedListener, PLOnCompletionListener, PLOnErrorListener, PLOnInfoListener, PLOnVideoSizeChangedListener, PLOnSeekCompleteListener, PLOnImageCapturedListener {
}
