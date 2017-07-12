package com.niuyun.hire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Created by luo.xiao on 2016/5/17.
 */
public class MaxHeighListView extends ListView{
    int maxHeight=180;
    public MaxHeighListView(Context context) {
        super(context);
    }

    public MaxHeighListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeighListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (getMeasuredHeight() > UIUtil.dip2px(getContext(), maxHeight)){
//            int newHei = MeasureSpec.makeMeasureSpec(UIUtil.dip2px(getContext(), maxHeight), MeasureSpec.EXACTLY);
//            super.onMeasure(widthMeasureSpec, newHei);
//        }
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setMaxHeight(int max){
        this.maxHeight=max;
    }
}
