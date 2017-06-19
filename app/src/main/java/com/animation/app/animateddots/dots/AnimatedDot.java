package com.animation.app.animateddots.dots;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Dasha on 16.06.2017
 */

public class AnimatedDot extends IDot {

    private int mCurrentColorIndex;
    private Integer[] mColors;
    private AnimatorSet mAnimSetSeq = new AnimatorSet();
    private AnimatorSet mAnimSetTogether = new AnimatorSet();
    private ValueAnimator mAppearAnimator;
    private ValueAnimator mLoaderAnimator;
    private ValueAnimator mColorAnimator;

    public AnimatedDot(float dotRadius, int position, int defColor) {
        super(dotRadius, position, defColor);
    }

    public AnimatedDot() {
        super();
    }

    public void setColors(Integer[] colors) {
        mCurrentColorIndex = 0;
        mColors = colors;
    }

    void setColorIndex(int index) {
        mCurrentColorIndex = index;
        mPaint.setColor(mColors[index]);
    }

    void setColor(int color) {
        mPaint.setColor(color);
    }


    @Override
    public int getCurrentColor() {
        return mColors[mCurrentColorIndex];
    }

    public int getCurrentColorIndex() {
        return mCurrentColorIndex;
    }

    public int incrementAndGetColor() {
        incrementColorIndex();
        return getCurrentColor();
    }

    public void startAnimation(long delay) {
        if (mAppearAnimator != null && mLoaderAnimator != null/* && mColorAnimator != null*/) {
            mAnimSetTogether.playTogether(mLoaderAnimator/*, mColorAnimator*/);
            mAnimSetTogether.setStartDelay(delay);
            mAnimSetSeq.playSequentially(mAppearAnimator, mAnimSetTogether);
            mAnimSetSeq.start();
        }
    }

    public void stopAnimation() {
        mAnimSetSeq.cancel();

    }

    void setLoaderAnimator(ValueAnimator loadAnimator, long delay) {
        mLoaderAnimator = loadAnimator;
        mLoaderAnimator.setStartDelay(delay);
    }

    public ValueAnimator getLoaderAnimator() {
        return mLoaderAnimator;
    }

    public ValueAnimator getAppearAnimator() {
        return mAppearAnimator;
    }

    void setColorAnimator(ValueAnimator colorAnimator) {
        mColorAnimator = colorAnimator;
    }

    void setAppearAnimator(ValueAnimator appearAnimator, long delay) {
        mAppearAnimator = appearAnimator;
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
        super.draw(canvas);
    }

    @Override
    void initDot(float dotRadius, int position, int color) {
        this.position = position;
        mDotRadius = dotRadius;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setShadowLayer(5.5f, 6.0f, 6.0f, Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    DotType getType() {
        return DotType.ANIMATED;
    }
}
