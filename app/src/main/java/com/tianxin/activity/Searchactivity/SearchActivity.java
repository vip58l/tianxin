package com.tianxin.activity.Searchactivity;

import android.content.Context;
import android.content.Intent;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.tianxin.Util.Constants;
import com.tianxin.adapter.setAdapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.R;
import com.tencent.opensource.model.item;
import com.tianxin.Util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//搜索界面
public class SearchActivity extends BasActivity2 {
    @BindView(R.id.se_gridview_one)
    GridView se_gridview_one;
    @BindView(R.id.se_gridview_two)
    GridView se_gridview_two;
    @BindView(R.id.se_edittext_top)
    EditText se_edittext_top;
    List<item> list1 = new ArrayList<>();
    List<item> list2 = new ArrayList<>();

    private String[] title1;
    private String[] title2;

    public static void starsetAction(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(this, getResources().getColor(R.color.c_main));
        return R.layout.activity_search;
    }

    @Override
    public void iniview() {
        title1 = getResources().getStringArray(R.array.arrayitem12);
        for (String s : title1) {
            item item = new item();
            item.type = setAdapter.Search1;
            item.object = s;
            list1.add(item);
        }
        title2 = getResources().getStringArray(R.array.arrayitem13);
        for (String s : title2) {
            item item = new item();
            item.type = setAdapter.Search2;
            item.object = s;
            list2.add(item);
        }

        se_gridview_one.setAdapter(new setAdapter(context, list1));
        se_gridview_two.setAdapter(new setAdapter(context, list2));
        se_gridview_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(Constants.TITLE, title1);
                intent.putExtra(Constants.POSITION, position);
                intent.setClass(context, SearchDeActivity.class);
                startActivity(intent);

            }
        });
        se_gridview_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, SearchDeActivity.class);
                intent.putExtra(Constants.TITLE, title2);
                intent.putExtra(Constants.POSITION, position);
                startActivity(intent);
            }
        });
        se_edittext_top.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String[] title = {se_edittext_top.getText().toString().trim()};
                    Intent intent = new Intent();
                    intent.putExtra(Constants.TITLE, title);
                    intent.putExtra(Constants.POSITION, 0);
                    intent.setClass(SearchActivity.this, SearchDeActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.se_img_go})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.se_img_go:
                finish();
                break;
        }

    }

    @Override
    public void OnEorr() {

    }

}
