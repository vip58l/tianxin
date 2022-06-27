package com.tianxin.Fragment.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.tianxin.BasActivity.BasFragment;
import com.tianxin.R;

import butterknife.BindView;

/**
 * 图片浏览类
 */
public class photos extends BasFragment {
    @BindView(R.id.imageView)
    ImageView imageview;
    @BindView(R.id.loading)
    ProgressBar loading;
    View rooview;
    final static String urlkey = "url";
    String url;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle arguments = getArguments();
        url = arguments.getString(urlkey);
    }

    @Override
    public View getview(LayoutInflater inflater) {
        return rooview = inflater.inflate(R.layout.gallery_detail, null);
    }

    @Override
    public void OnClick(View v) {

    }

    @Override
    public void iniview() {
        Glide.with(getContext()).load(url).listener(new RequestListeners(loading)).into(imageview);
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

    public static photos pasturl(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        photos photo = new photos();
        photo.setArguments(bundle);
        return photo;
    }

}
