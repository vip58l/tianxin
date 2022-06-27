package com.tianxin.Util;

import java.util.Iterator;
import java.util.Map;

public class Comms {

    /**
     * 字符串拼接
     *
     * @param map
     * @param TYPE
     * @return
     */
    public static String getStringBuffer(Map<String, String> map, int TYPE) {
        StringBuffer sb = new StringBuffer();
        int is = 0;
        switch (TYPE) {
            case 1:
                //第一种：
                for (String key : map.keySet()) {
                    if (is > 0) {
                        sb.append("&" + key + "=" + map.get(key));
                    } else {
                        sb.append(key + "=" + map.get(key));
                    }
                    is++;
                }
                break;
            case 2:
                //第二种
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
            case 3:
                //第三种：
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (is > 0) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    } else {
                        sb.append(entry.getKey() + "=" + entry.getValue());
                    }
                    is++;
                }
                break;
        }

        return sb.toString();
    }

}
