package com.jinwang.subao.normal.guide;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jinwangmobile.ui.base.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by dreamy on 2015/6/23.
 */
public class HorizontalGuideActivity extends BaseActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ViewPagerAdapter vpAdapter;

    //定义一个ArrayList来存放View
    private ArrayList<View> views;
    //引导图片资源
//    private static final int[] pics = {R.drawable.guide_1,R.drawable.guide_2, R.drawable.guide_3};

    //底部小点的图片
    private ImageView[] points;

    //记录当前选中位置
    private int currentIndex;
    private ViewGroup main;

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
