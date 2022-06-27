/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/1/25 0025
 */


package com.tencent.qcloud.costransferpractice.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class JsonUitl {
    private static final Gson mGson = new Gson();

    /**
     * 将json字符串转化成实体对象
     * @param json
     * @param classOfT
     * @return
     */
    public static Object stringToObject( String json , Class classOfT){
        return  mGson.fromJson( json , classOfT ) ;
    }

    /**
     * 将对象准换为json字符串 或者 把list 转化成json
     * @param object
     * @param <T>
     * @return
     */
    public static <T> String objectToString(T object) {
        return mGson.toJson(object);
    }

    /**
     * 把json 字符串转化成list
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> stringToList(String json , Class<T> cls  ){
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(mGson.fromJson(elem, cls));
        }
        return list ;
    }


}
