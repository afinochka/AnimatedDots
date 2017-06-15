package com.animation.app.animateddots.dots;

import android.animation.Animator;

/**
 * Created by Dasha on 15.06.2017
 */

class AnimationRepeater implements Animator.AnimatorListener {
    private boolean alternate = true;
    private Dot mDot;
    private Integer[] mColors;

    AnimationRepeater(Dot dot, Integer[] colors) {
        this.mDot = dot;
        mColors = colors;
    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {
        /*if (alternate) {
            mDot.colorAnimator.setObjectValues(
                    mColors[mDot.mCurrentColorIndex],
                    mColors[mDot.incrementColorIndex()]
            );
            mDot.colorAnimator.start();
            alternate = false;
        } else {
            alternate = true;
        }*/
    }
}
