package com.niuyun.hire.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页viewpager适配器
 */

public class IndexFragmentPagerAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragmentList =new ArrayList<>();
    public IndexFragmentPagerAdapter(FragmentManager fm , List<BaseFragment> fragmentList ) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
