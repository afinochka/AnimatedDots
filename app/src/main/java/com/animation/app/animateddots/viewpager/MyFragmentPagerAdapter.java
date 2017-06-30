package com.animation.app.animateddots.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 28.06.2017
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

    static final int PAGE_COUNT = 10;
    private List<Fragment> mFragments = new ArrayList<>();

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        int index = position % mFragments.size();
        return mFragments.get(index);
//        return PageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
//        return PAGE_COUNT;
    }

    /*@Override
    public float getPageWidth(int position) {
        return 0.93f;
    }*/

}
