package com.animation.app.animateddots.dots;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Dasha on 15.06.2017
 */

class Dot {
    private Paint mPaint;
    int mCurrentColorIndex;
    private int mDotRadius;
    private Integer[] mColors;
    float cx;
    float cy;
    int position;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private ValueAnimator mPositionAnimator;
    private ValueAnimator mColorAnimator;

    Dot(Integer[] colors, int dotRadius, int position) {
        this.position = position;
        mColors = colors;
        mCurrentColorIndex = 0;
        mDotRadius = dotRadius;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColors[mCurrentColorIndex]);
        mPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
    }

    void setColorIndex(int index) {
        mCurrentColorIndex = index;
        mPaint.setColor(mColors[index]);
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }

    private int getCurrentColor() {
        return mColors[mCurrentColorIndex];
    }

    public int incrementAndGetColor() {
        incrementColorIndex();
        return getCurrentColor();
    }

    public void startAnimation(){
        mAnimatorSet.playTogether(mPositionAnimator, mColorAnimator);
        mAnimatorSet.start();
    }

    public void stopAnimation(){
        mAnimatorSet.end();
    }

    void setPositionAnimator(ValueAnimator posAnimator){
        mPositionAnimator = posAnimator;
    }

    void setColorAnimator(ValueAnimator colorAnimator){
        mColorAnimator = colorAnimator;
    }

    public void setDelayPositionAnim(long delay){
        mPositionAnimator.setStartDelay(delay);
    }

    void applyNextColor() {
        mCurrentColorIndex++;
        if (mCurrentColorIndex >= mColors.length)
            mCurrentColorIndex = 0;
        mPaint.setColor(mColors[mCurrentColorIndex]);
    }

    int incrementColorIndex() {
        mCurrentColorIndex++;
        if (mCurrentColorIndex >= mColors.length)
            mCurrentColorIndex = 0;
        return mCurrentColorIndex;
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, mDotRadius, mPaint);
    }
}
