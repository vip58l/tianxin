package com.tianxin.Module.api;

import com.tencent.opensource.model.item;
import com.tianxin.Test.MyOpenhelper;
import com.tianxin.getHandler.JsonUitl;
import com.tianxin.getlist.HotRooms;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class appmes {

    /**
     * 联网处理
     *
     * @param msg
     * @param type
     * @param myOpenhelper
     * @return
     */
    public static List<item> getList(String msg, int type,List<item> list, MyOpenhelper myOpenhelper) {
        try {
            JSONArray jsonArray = new JSONObject(msg).getJSONObject("data").getJSONArray("datas");
            List<HotRooms> list1 = JsonUitl.stringToList(jsonArray.toString(), HotRooms.class);
            for (int i = 0; i < list1.size(); i++) {
                HotRooms hotRooms = list1.get(i);
                item item = new item();
                item.type = type;
                item.object = hotRooms;
                list.add(item);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
