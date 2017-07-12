package com.niuyun.hire.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ScrollView;

/**
 * Created by liu.zhenrong on 2017/1/5.
 */

public class ChangelessHeightScrollView extends ScrollView {
    private Context mContext;
    public ChangelessHeightScrollView(Context context) {
        super(context);
        init(context,null);
    }

    public ChangelessHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ChangelessHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        //暂时定位屏幕的三分之一 以后有需要扩展为活的
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels / 3, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
