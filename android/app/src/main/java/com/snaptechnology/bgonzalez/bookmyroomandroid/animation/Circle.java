package com.snaptechnology.bgonzalez.bookmyroomandroid.animation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;

/**
 * Created by bgonzalez on 30/08/2016.
 */
public class Circle extends View {

    private static final int START_ANGLE_POINT = 0;

    private final Paint paint;
    private static RectF rect;
    final int strokeWidth = 40;


    private float angle;



    int size= 243;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);


        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(getResources().getColor(R.color.colorAccent));


        this.getMeasuredWidth();

        rect = new RectF(strokeWidth, size+strokeWidth,  strokeWidth,  size+strokeWidth);

        //Initial Angle (optional, it can be zero)
        angle = 90;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rect, START_ANGLE_POINT - 90, angle, false, paint);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
