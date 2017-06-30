package com.animation.app.animateddots.Indicator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.animation.app.animateddots.Indicator.DotView;
import com.animation.app.animateddots.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 21.06.2017
 */

public class PagerIndicator extends LinearLayout {

    private static final int DELAY_BETWEEN_DOTS = 400;
    private static final int APPEAR_ANIMATION_DELAY = 50;
    private static final int LOADING_ANIMATION_DURATION = 400;
    private static final int DEFAULT_COLOR_POSITION = 0;

    ArrayList<DotView> dots;

    List<Animator> appearAnim = new ArrayList<>();
    List<Animator> loadingAnim = new ArrayList<>();

    AnimatorSet mAppearAnimatorSet;
    AnimatorSet mLoadingAnimationSet;
    AnimatorSet mGlobalSet;

    private float mActiveDiameter = 30;
    private int mActiveColor = 0;
    int mActivePos = 0;

    private float mDefaultDiameter = 20;
    int[] mDefColors;

    private float mPadding = 5;


    public PagerIndicator(Context context) {
        super(context);
        init(context, null);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PagerIndicator, 0, 0);

            try {
                mDefaultDiameter = a.getDimension(R.styleable.PagerIndicator_dot_diameter_def, mDefaultDiameter);
                mActiveDiameter = a.getDimension(R.styleable.PagerIndicator_dot_diameter_active, mActiveDiameter);
                mPadding = a.getDimension(R.styleable.PagerIndicator_distance_dot_centers, mPadding);

                int colorId = a.getResourceId(R.styleable.PagerIndicator_dot_active_color, 0);
                if (colorId != 0) {
                    mActiveColor = ContextCompat.getColor(getContext(), colorId);
                }

                int colorsArrayId = a.getResourceId(R.styleable.PagerIndicator_dot_def_colors, 0);
                if (colorsArrayId != 0) {
                    mDefColors = getResources().getIntArray(colorsArrayId);
                }
            } finally {
                a.recycle();
            }
        }

    }

    public void initDots(int count) {
        dots = new ArrayList<>();
        setOrientation(LinearLayout.HORIZONTAL);

        if (count > 1 && mDefColors == null) {
            throw new IllegalArgumentException("Colors for dots must not be empty");
        }

        for (int i = 0; i < count; i++) {

            DotView img = new DotView(getContext());

            if (i == mActivePos) {
                img.setDotImage(mActiveDiameter, mActiveColor);
                img.setDotType(DotView.DotType.STATIC);
            } else {
                img.setDotImage(mDefaultDiameter, mDefColors[DEFAULT_COLOR_POSITION]);
                img.setDotType(DotView.DotType.ANIMATED);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            int rightLeftMargin = (int) (mPadding / 2);
            if (img.getDotType() == DotView.DotType.STATIC) {
                rightLeftMargin = rightLeftMargin - (int) Math.abs((mActiveDiameter - mDefaultDiameter) / 2);
            }
            params.setMargins(rightLeftMargin, 0, rightLeftMargin, 0);

            dots.add(img);
            addView(img, params);
        }

        initAnimation();
    }

    void initAnimation() {
        for (DotView dot : dots) {
            int index = dots.indexOf(dot);
            if (dot.getDotType() == DotView.DotType.ANIMATED) {

                AnimatorSet set = createAppearAnimatorForDot(dot);
                set.setStartDelay((index - 1) * APPEAR_ANIMATION_DELAY);
                dot.setAppearAnimation(set);
                appearAnim.add(set);

                ObjectAnimator animatorLoad = createLoaderAnimatorForDot(dot);
                animatorLoad.setStartDelay((index - 1) * DELAY_BETWEEN_DOTS);
                dot.setTranslateAnimation(animatorLoad);
                loadingAnim.add(animatorLoad);
            }
        }
    }

    private ObjectAnimator createLoaderAnimatorForDot(final DotView dot) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(dot, TRANSLATION_Y, 0f, -mDefaultDiameter / 2);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(LOADING_ANIMATION_DURATION);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ObjectAnimator.REVERSE);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dot.setDotImage(mDefaultDiameter, incColor(dot));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return animator;
    }

    public int incColor(DotView dot) {
        int pos = 0;
        if (mDefColors != null && mDefColors.length > 0) {
            for (int i = 0; i < mDefColors.length; i++) {
                if (mDefColors[i] == dot.getColor()) {
                    pos = i;
                }
            }

            pos++;
            if (pos >= mDefColors.length) {
                return mDefColors[0];
            }
            return mDefColors[pos];
        }
        return 0;
    }

    private AnimatorSet createAppearAnimatorForDot(final View dot) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(APPEAR_ANIMATION_DELAY * dots.size());
        set.playTogether(ObjectAnimator.ofFloat(dot, SCALE_X, 0f, 1f),
                ObjectAnimator.ofFloat(dot, SCALE_Y, 0f, 1f));
        return set;
    }

    public void startAnimation() {
        if (appearAnim.size() != 0 && loadingAnim.size() != 0) {

            mAppearAnimatorSet = new AnimatorSet();
            mAppearAnimatorSet.playTogether(appearAnim);

            mLoadingAnimationSet = new AnimatorSet();
            mLoadingAnimationSet.playTogether(loadingAnim);
            mLoadingAnimationSet.addListener(new AnimationRepeater(LOADING_ANIMATION_DURATION));

            mGlobalSet = new AnimatorSet();
            mGlobalSet.playSequentially(mAppearAnimatorSet, mLoadingAnimationSet);
            mGlobalSet.start();
        }
    }

    public void stopAnimation() {
        mLoadingAnimationSet.removeAllListeners();
        mAppearAnimatorSet.cancel();
        mLoadingAnimationSet.cancel();
        mGlobalSet.cancel();

        for (DotView dot : dots) {
            dot.clearAnimation();
        }

        resetDots();
    }

    private class AnimationRepeater implements Animator.AnimatorListener {
        private int mDelay = 500;

        AnimationRepeater(int delay) {
            mDelay = delay;
        }

        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            animator.setStartDelay(mDelay);
            animator.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetDots();
                }
            }, mDelay - 100);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    void resetDots() {
        for (DotView dot : dots) {
            float radius = dot.getDotType() == DotView.DotType.STATIC ? mActiveDiameter : mDefaultDiameter;
            int color = dot.getDotType() == DotView.DotType.STATIC ? mActiveColor : mDefColors[DEFAULT_COLOR_POSITION];
            dot.setDotImage(radius, color);
            dot.setTranslationY(0);
            dot.setScaleY(1);
            dot.setScaleX(1);
        }
    }

    public void getNext() {
        View view = getChildAt(mActivePos);
        removeView(view);
        ++mActivePos;
        if (mActivePos > (dots.size() - 1)) {
            mActivePos = 0;
        }
        addView(view, mActivePos);
    }

    public void changeDots(int position) {
        View view = getChildAt(mActivePos);
        removeView(view);
        mActivePos = position;
        if (mActivePos > (dots.size() - 1)) {
            mActivePos = 0;
        } else if (mActivePos < 0) {
            mActivePos = dots.size() - 1;
        }
        addView(view, mActivePos);
    }

    public void getPrev() {
        View view = getChildAt(mActivePos);
        removeView(view);
        --mActivePos;
        if (mActivePos < 0) {
            mActivePos = dots.size() - 1;
        }
        addView(view, mActivePos);
    }

}
