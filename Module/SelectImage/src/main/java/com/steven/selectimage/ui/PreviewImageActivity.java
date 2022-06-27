package com.steven.selectimage.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.steven.selectimage.R;
import com.steven.selectimage.model.Image;
import com.steven.selectimage.utils.StatusBarUtil;
import com.steven.selectimage.widget.PreViewImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewImageActivity extends BaseActivity {
    private ViewPager mViewPager;
    private TextView mCount;
    private List<Image> mSelectedImages;
    private int currentItem;

    /**
     * 打开
     * @param context
     * @param mSelectImages
     * @param position
     */
    public static void  setAction(Context context,List<Image> mSelectImages,int position){

        ArrayList<Image> mSelect = new ArrayList<>();
        for (Image selectImage : mSelectImages) {
            if (TextUtils.isEmpty(selectImage.getPath())) {
                continue;
            }
            mSelect.add(selectImage);
        }
        Collections.swap(mSelect, 0, mSelect.size() - 1);

        Intent intent = new Intent(context, PreviewImageActivity.class);
        intent.putParcelableArrayListExtra("preview_images", mSelect);
        intent.putExtra("currentItem", position);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void init() {
        mViewPager = findViewById(R.id.viewPager);
        mCount = findViewById(R.id.tv_count);
        StatusBarUtil.statusBarTranslucent(activity);
        mSelectedImages = getIntent().getParcelableArrayListExtra("preview_images"); //接收外页传的参数
        currentItem = getIntent().getIntExtra("currentItem", 0);         //接收外页传的参数
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mSelectedImages.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                PreViewImageView imageView = new PreViewImageView(container.getContext());
                Glide.with(imageView.getContext()).load(mSelectedImages.get(position).getPath()).into(imageView);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCount.setText(String.format("%s/%s", (position + 1), mSelectedImages.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(currentItem);
        mCount.setText(String.format("%s/%s", 1, mSelectedImages.size()));
    }

}
