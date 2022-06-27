package com.steven.selectimage.widget.recyclerview;

/**
 * Created by zhiwenyan on 5/25/17.
 * <p>
 * 条目的点击事件
 */

public interface OnItemClickListener {

    /**
     * 点击浏览图片
     * @param position
     */
    void onItemClick(int position);

    /**
     *
     * 添加更多图片
     */
    void onAdd();

    /**
     *删除图片
     * @param position
     */
    void delonItemClick(int position);
}
