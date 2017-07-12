package com.niuyun.hire.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.niuyun.hire.utils.UIUtil;


public class ProgressBarCircularIndeterminate extends CustomView {


    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    int backgroundColor = Color.parseColor("#1E88E5");


    public ProgressBarCircularIndeterminate(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(attrs);

    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs) {

        setMinimumHeight(UIUtil.dpToPx(32, getResources()));
        setMinimumWidth(UIUtil.dpToPx(32, getResources()));

        //Set background Color
        // Color by resource
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            // Color by hexadecimal
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
            else
                setBackgroundColor(Color.parseColor("#1E88E5"));
        }

        setMinimumHeight(UIUtil.dpToPx(3, getResources()));


    }

    /**
     * Make a dark color to ripple effect
     *
     * @return
     */
    protected int makePressColor() {
        int r = (this.backgroundColor >> 16) & 0xFF;
        int g = (this.backgroundColor >> 8) & 0xFF;
        int b = (this.backgroundColor >> 0) & 0xFF;
//		r = (r+90 > 245) ? 245 : r+90;
//		g = (g+90 > 245) ? 245 : g+90;
//		b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128, r, g, b);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //暂时隐藏了ribble效果
//        if (firstAnimationOver == false)
//            drawFirstAnimation(canvas);
//        if (cont > 0)
            drawSecondAnimation(canvas);
        invalidate();

    }

    float radius1 = 0;
    float radius2 = 0;
    int cont = 0;
    boolean firstAnimationOver = false;

    /**
     * Draw first animation of view
     *
     * @param canvas
     */
    private void drawFirstAnimation(Canvas canvas) {
        if (radius1 < getWidth() / 2) {
            //第一步 循环画一个逐渐变大的圆，形成放大动画
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius1 = (radius1 >= getWidth() / 2) ? (float) getWidth() / 2 : radius1 + 1;
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius1, paint);
        } else {
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            //画一个蓝色的背景圆
            Canvas temp = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
            //第二步  画圆中间的透明部分，形成缩小动画ripple
            Paint transparentPaint = new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            if (cont >= 50) {
                radius2 = (radius2 >= getWidth() / 2) ? (float) getWidth() / 2 : radius2 + 1;
            } else {
                radius2 = (radius2 >= getWidth() / 2 - UIUtil.dpToPx(4, getResources())) ? (float) getWidth() / 2 - UIUtil.dpToPx(4, getResources()) : radius2 + 1;
            }
            temp.drawCircle(getWidth() / 2, getHeight() / 2, radius2, transparentPaint);
            //将整个动画view画到该画布上
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if (radius2 >= getWidth() / 2 - UIUtil.dpToPx(4, getResources()))
                cont++;
            if (radius2 >= getWidth() / 2)
                firstAnimationOver = true;
        }
    }

    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limite = 0;

    /**
     * Draw second animation of view
     *
     * @param canvas
     */
    private void drawSecondAnimation(Canvas canvas) {
        if (arcO == limite)
            //圆弧头部逐渐增大
            arcD += 6;
        if (arcD >= 290 || arcO > limite) {
            //到了最大圆弧，圆弧结束位置减少，开始位置收缩
            arcO += 6;
            arcD -= 6;
        }
        if (arcO > limite + 290) {
            //一圈结束，又开开始下一轮
            limite = arcO;
            arcO = limite;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle, getWidth() / 2, getHeight() / 2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        //绘制旋转圆弧部分
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
//		temp.drawARGB(0, 0, 0, 255);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, paint);
        //绘制空心圆部分，清除圆弧靠里边的颜色，造成一种现象：只有一个圆弧线条在旋转
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() / 2) - UIUtil.dpToPx(4, getResources()), transparentPaint);
//将整个动画view画到该画布上
        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    // Set color of background
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        if (isEnabled())
            beforeBackground = backgroundColor;
        this.backgroundColor = color;
    }

}
