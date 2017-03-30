package com.xiaoka.xksupportui.imageview.imagebrown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.xiaoka.xksupportui.R;
import com.xiaoka.xksupportui.viewpager.HackyViewPager;

import java.util.ArrayList;

public class ImageBrownActivity extends FragmentActivity {

    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "IMAGE_INDEX";
    public static final String EXTRA_IMAGE_URLS_LIST = "IMAGE_LIST_URLS";
    public static final String EXTRA_IMAGE_URL_ARRAY = "IMAGE_ARRAY_URLS";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;

    public static void newInstance(Context mContext, ArrayList<String> urls, int position) {
        Intent intent = new Intent(mContext, ImageBrownActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImageBrownActivity.EXTRA_IMAGE_URLS_LIST, urls);
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    public static void newInstance(Context mContext, ArrayList<String> urls) {
        Intent intent = new Intent(mContext, ImageBrownActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putStringArrayListExtra(ImageBrownActivity.EXTRA_IMAGE_URLS_LIST, urls);
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_INDEX, 0);
        mContext.startActivity(intent);
    }

    public static void newInstance(Context mContext, String[] urls, int position) {
        Intent intent = new Intent(mContext, ImageBrownActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_URL_ARRAY, urls);
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    public static void newInstance(Context mContext, String[] urls) {
        Intent intent = new Intent(mContext, ImageBrownActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_URL_ARRAY, urls);
        intent.putExtra(ImageBrownActivity.EXTRA_IMAGE_INDEX, 0);
        mContext.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);
        String[] urls = new String[]{};
        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        String[] urlsArray = getIntent().getStringArrayExtra(EXTRA_IMAGE_URL_ARRAY);
        ArrayList<String> urlsList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS_LIST);
        if (urlsArray != null && urlsArray.length != 0) {
            urls = urlsArray;
        } else if (urlsList != null && urlsList.size() != 0) {
            urls =urlsList.toArray(new String[urlsList.size()]);
        }


        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }
        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return ImageDetailFragment.newInstance(url);
        }
    }
}