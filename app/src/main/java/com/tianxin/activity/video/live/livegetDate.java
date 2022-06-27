package com.tianxin.activity.video.live;

import android.content.Context;
import android.view.View;

import com.tencent.opensource.model.item;
import com.tianxin.widget.zbVideo;

import java.util.ArrayList;
import java.util.List;

public class livegetDate {

    /**
     * 创建布文件文件
     *
     * @return
     */
    public List<View> CreateViewList(Context context, List<item> list) {
        List<View> viewList = new ArrayList<>();
        for (item item : list) {
            zbVideo azty = new zbVideo(context);
            azty.setinit(item);
            viewList.add(azty);
        }
        return viewList;
    }
}
