package com.niuyun.hire.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutoListView extends ListView {

    /**
     * listview高度
     */
    private int listViewHeight = -1;

    public int getListViewHeight() {
        return listViewHeight;
    }

    public void setListViewHeight(int listViewHeight) {
        this.listViewHeight = listViewHeight;
    }

    public AutoListView(Context context) {
        super(context);
    }

    public AutoListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (listViewHeight == -1) {// 动态的修改listview的高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        } else {// 设置listview的最大高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(listViewHeight, MeasureSpec.AT_MOST);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
