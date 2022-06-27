package com.tianxin.Fragment.fragment;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;



public class RequestListeners implements RequestListener {
    ProgressBar loading;

    public RequestListeners(ProgressBar loading) {
        this.loading = loading;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
        loading.setVisibility(View.GONE);
        return false;
    }

}
