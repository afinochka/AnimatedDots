package com.animation.app.animateddots.dots;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Dasha on 16.06.2017
 */

public class StaticDot extends IDot {

    private int mCurrentColor;

    public StaticDot(){
        super();
    }

    public StaticDot(float dotRadius, int position, int color) {
        super(dotRadius, position, color);
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }

    void draw(Canvas canvas) {
        super.draw(canvas);
    }


    @Override
    void initDot(float dotRadius, int position, int color) {
        this.position = position;
        mCurrentColor = color;
        mDotRadius = dotRadius;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mCurrentColor);
        mPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public int getCurrentColor() {
        return mCurrentColor;
    }

    @Override
    DotType getType() {
        return DotType.STATIC;
    }

}
