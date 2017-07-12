package com.niuyun.hire.view.adviewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sun.luwei on 2016/3/4.
 */
public class AdAdapter extends PagerAdapter {

    View[] views;

    public AdAdapter(View[] views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        if(null == views) return 0;
        if (views.length > 1) {
            return Integer.MAX_VALUE;
        } else {
            return 1;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        try {
            container.addView(views[position % views.length], 0);
        } catch (Exception e) {
        }
        return views[position % views.length];
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }


}
