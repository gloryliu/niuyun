package com.niuyun.hire.view.adviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;


public class AdViewPagerAdapter extends PagerAdapter {
	private ScaleImageView[] advImgs;
	private int mCount;

	
	public AdViewPagerAdapter(ScaleImageView[] advImgs) {
		this.advImgs = advImgs;
		mCount = advImgs.length;
	}

	@Override
	public int getCount() {
		if (mCount > 1) {
			return Integer.MAX_VALUE;
		} else {
			return advImgs.length;
		}
	}

	@Override
	public boolean isViewFromObject(View v, Object o) {
		return v == o;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		try {
			if(null!=advImgs[position % mCount].getParent()){
				((ViewPager)advImgs[position % mCount].getParent()).removeView(advImgs[position % mCount]);
			}
			container.addView(advImgs[position % mCount], 0);
		} catch (Exception e) {
		}
		return advImgs[position % mCount];
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// container.removeView(advImgs[position % mCount]);
	}

}
