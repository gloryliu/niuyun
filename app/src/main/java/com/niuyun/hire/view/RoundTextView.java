package com.niuyun.hire.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.niuyun.hire.R;


/**
 * 圆角的
 */
public class RoundTextView extends android.support.v7.widget.AppCompatTextView {
    private int inColor;
    private float round;
    private int textColor;

    public RoundTextView(Context context) {
        super(context);
    }

    public RoundTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        inColor = array.getColor(R.styleable.RoundTextView_inColor, 0);
        round = array.getDimension(R.styleable.RoundTextView_round, 0);
        textColor = array.getColor(R.styleable.RoundTextView_textColor1, 0xffffff);
        array.recycle();
        initPaint();
    }


    public void setInColor(int color) {
        this.inColor = color;
        invalidate();
    }

    Paint mTextPaint;
    float mTextSize;
    Paint mPaint;
    RectF mRectF;

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        if (inColor != 0) {
            mPaint.setColor(inColor);
        }

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFilterBitmap(true);
        mTextSize = getTextSize();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);//设置文字水平居中
        mTextPaint.setColor(textColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //在空的画布上画一个矩形
        mRectF = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(mRectF, round, round, mPaint);
        String text = getText().toString();
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        float xPos = getWidth() / 2;
        int yPos = (getHeight() - fontMetricsInt.ascent - fontMetricsInt.descent) / 2;
        canvas.drawText(text, xPos, yPos, mTextPaint);

    }

}
