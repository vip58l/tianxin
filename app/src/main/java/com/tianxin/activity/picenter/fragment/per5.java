package com.tianxin.activity.picenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;
import com.tianxin.Util.Glideload;

import butterknife.BindView;

/**
 * 图片浏览器
 */
public class per5 extends BasFragment {
    @BindView(R.id.photo_view)
    ImageView photo_view;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private static String puturl = "url";
    private String getPuturl;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        if (arguments != null) {
            getPuturl = arguments.getString(puturl);
        }
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return inflater.inflate(R.layout.show_item_image, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        progressBar.setVisibility(View.GONE);
        Glideload.loadImage(photo_view, getPuturl);
    }

    @Override
    public void initData() {

    }

    @Override
    public void OnEorr() {

    }

    @Override
    public void onRefresh() {

    }

    public static Fragment seturl(String url) {
        Bundle args = new Bundle();
        args.putString(puturl, url);
        per5 image = new per5();
        image.setArguments(args);
        return image;
    }


}
