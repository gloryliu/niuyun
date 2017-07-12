package com.niuyun.hire.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sun.luwei on 2017/3/2.
 */

public class NoScrollViewPager extends ViewPager {


    private boolean isCanScroll = false;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isCanScroll) {
            return true;
        }
        return super.onTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!isCanScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }


    public boolean isScrollble() {
        return isCanScroll;
    }

    public void setScrollble(boolean scrollble) {
        this.isCanScroll = scrollble;
    }
}
