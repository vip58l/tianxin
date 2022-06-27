package com.tianxin.listener;

import android.view.View;

public interface OnItemChildClickListener {
    void onItemChildClick(Object object, View view, int position);

    void OnClickListener(Object object, View view, int position);

    default void onItemdefaultListener(Object object, View view, int position) {
    }


}
