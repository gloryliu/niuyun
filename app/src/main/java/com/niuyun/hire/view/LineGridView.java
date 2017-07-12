package com.niuyun.hire.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;


import com.niuyun.hire.R;

import java.lang.reflect.Field;

/**
 * Created by zhao.wenchao on 2016/3/1.
 * 网格gridview
 */
public class LineGridView extends GridView {
    Context mContext;
    public LineGridView(Context context)
    {
        super(context);
        this.mContext=context;
    }

    public LineGridView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext=context;
    }

    public LineGridView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.mContext=context;
    }
    //该自定义控件只是重写了GridView的onMeasure方法，使其不会出现滚动条，ScrollView嵌套ListView也是同样的道理，不再赘述。
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    @Override
    protected void dispatchDraw(Canvas canvas)
    {
        super.dispatchDraw(canvas);

        int column = 1;
        try
        {
            //通过反射拿到列数
            Field field = GridView.class.getDeclaredField("mNumColumns");
            field.setAccessible(true);
            column = field.getInt(this);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        int childCount = getChildCount();
        Paint paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getContext().getResources().getColor(R.color.color_eeeeee));
        for (int i = 0; i < childCount; i++)
        {
            View cellView = getChildAt(i);
            if (cellView.getTop()!=0){
                //顶部线，坐标+1是为了画在cellView上
                canvas.drawLine(cellView.getLeft(), cellView.getTop() +1, cellView.getRight(), cellView.getTop() + 1, paint);
            }
            //左边线
            canvas.drawLine(cellView.getLeft() + 1, cellView.getTop(), cellView.getLeft() + 1, cellView.getBottom(), paint);


            if ((i + 1) % column == 0)      //最右边一列单元格画上右边线
            {
                canvas.drawLine(cellView.getRight(), cellView.getTop() + 1, cellView.getRight(), cellView.getBottom() + 1, paint);
            }
            if ((i + column) >= childCount)  //最后column个单元格画上底边线
            {
                canvas.drawLine(cellView.getLeft(), cellView.getBottom() + 1, cellView.getRight(), cellView.getBottom() + 1, paint);
            }

            if (i<column)  //第一行单元格画上上边线
            {
//                canvas.drawLine(cellView.getLeft(), cellView.getTop() +1, cellView.getRight(), cellView.getTop() + 1, paint);
            }
            if (childCount % column != 0 && i == childCount - 1)   //如果最后一个单元格不在最右一列，单独为它画上右边线
            {
                canvas.drawLine(cellView.getRight() +1, cellView.getTop() + 1, cellView.getRight() + 1, cellView.getBottom() + 1, paint);
            }
        }
    }
}
