package com.animation.app.animateddots.dots;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Dasha on 19.06.2017
 */

public abstract class IDot {

    enum DotType {
        ANIMATED,
        STATIC
    }

    protected Paint mPaint;
    protected float mDotRadius;

    protected float cx;
    protected float cy;

    protected int position;

    public IDot() {
    }

    public IDot(float dotRadius, int position, int color) {
        initDot(dotRadius, position, color);
    }

    public static IDot getDot(DotType type) {
        if (type == DotType.ANIMATED) {
            return new AnimatedDot();
        } else if (type == DotType.STATIC) {
            return new StaticDot();
        }

        return null;
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, mDotRadius, mPaint);
    }

    abstract void initDot(float dotRadius, int position, int color);

    public abstract int getCurrentColor();

    abstract DotType getType();

    void setRadius(float radius){
        mDotRadius = radius;
    }

    float getRadius(){
        return mDotRadius;
    }
}
