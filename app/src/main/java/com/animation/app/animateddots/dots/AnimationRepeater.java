package com.animation.app.animateddots.dots;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * Created by Dasha on 15.06.2017
 */

class AnimationRepeater implements Animator.AnimatorListener {
    private AnimatedDot mDot;
    private int mDelay = 1000;

    AnimationRepeater(AnimatedDot dot, int delay) {
        this.mDot = dot;
        mDelay = delay;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        mDot.incrementColorIndex();
        mDot.setColor(mDot.getCurrentColor());
        mDot.incrementColorIndex();
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        mDot.setColor(mDot.getCurrentColor());
        animator.setStartDelay(mDelay);
        animator.start();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {
    }
}
