package com.tianxin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tianxin.R;
import com.tianxin.Util.Toashow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论列表
 */
public class dialog_jibao extends Dialog {
    @BindView(R.id.listview)
    ListView listview;
    List<String> list = new ArrayList<>();
    public dialog_jibao(Context context) {
        super(context, R.style.fei_style_dialog);
        setContentView(R.layout.item_jibao);
        ButterKnife.bind(this);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        listview.setAdapter(new getsetAdapter());
    }

    @OnClick({R.id.colas})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.colas:
                dismiss();
                break;
        }
    }

    private class getsetAdapter extends BaseAdapter {
        public getsetAdapter() {
            list.add("垃圾广告");
            list.add("色情广告");
            list.add("衣着爆露");
            list.add("粗口骂人");
            list.add("虚假信息");
            list.add("系统出错了闪退");
            list.add("网络异常");
            list.add("视频播放卡顿");

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String title = list.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_jibao_text, null);
            }
            TextView tv = convertView.findViewById(R.id.tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toashow.show("提交成功,   感谢您的支持！");
                    dismiss();
                }
            });

            tv.setText(title);
            return convertView;
        }
    }


}
