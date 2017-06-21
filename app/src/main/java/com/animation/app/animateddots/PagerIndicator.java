package com.animation.app.animateddots;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 21.06.2017
 */

public class PagerIndicator extends LinearLayout {

    private static final int DELAY_BETWEEN_DOTS = 500; //80
    private static final int APPEAR_DELAY_BETWEEN_DOTS = 200; //80
    private static final int BOUNCE_ANIMATION_DURATION = 500; //500
    private static final int COLOR_ANIMATION_DURATION = 250; //80

    ArrayList<DotView> dots;

    List<Animator> appearAnim = new ArrayList<>();
    List<Animator> loadingAnim = new ArrayList<>();

    AnimatorSet mAppearAnimatorSet;
    AnimatorSet mLoadingAnimationSet;
    AnimatorSet mGlobalSet;

    private int mActiveRad = 30;
    private int mActiveColor = 0;
    int mActivePos = 0;

    private int mDefaultRad = 20;
    int[] mDefColors;

    private int mDotsNumber = 1;


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
                mDefaultRad = a.getInteger(R.styleable.PagerIndicator_dot_radius_def, mDefaultRad);
                mActiveRad = a.getInteger(R.styleable.PagerIndicator_dot_radius_active, mActiveRad);
                mDotsNumber = a.getInteger(R.styleable.PagerIndicator_dots_number, mDotsNumber);

                int colorId = a.getResourceId(R.styleable.PagerIndicator_dot_active_color, 0);
                if(colorId != 0){
                    mActiveColor = ContextCompat.getColor(getContext(), colorId);
                }

                int colorsArrayId = a.getResourceId(R.styleable.PagerIndicator_dot_def_colors, 0);
                if(colorsArrayId != 0){
                    mDefColors = getResources().getIntArray(colorsArrayId);
                }
            } finally {
                a.recycle();
            }
        }


        initDots();
        initAnimation();
    }

    void initDots() {
        dots = new ArrayList<>();
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(LinearLayout.HORIZONTAL);

        if(mDotsNumber > 1 && mDefColors == null){
            throw new IllegalArgumentException("Colors for dots must not be empty");
        }

        for (int i = 0; i < mDotsNumber; i++) {

            DotView img = new DotView(getContext());

            if (i == mActivePos) {
                img.setDotImage(mActiveRad, mActiveColor);
                img.setDotType(DotView.DotType.STATIC);
            } else {
                img.setDotImage(mDefaultRad, mDefColors[0]);
                img.setDotType(DotView.DotType.ANIMATED);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            int rightMargin = i == 0 ? 25 : 32;
            params.setMargins(0, 0, rightMargin, 0);

            dots.add(img);
            addView(img, params);
        }
    }

    void initAnimation() {
        for (DotView dot : dots) {
            int index = dots.indexOf(dot);
            if (dot.getDotType() == DotView.DotType.ANIMATED) {

                AnimatorSet set = createAppearAnimatorForDot(dot);
                dot.setAppearAnimation(set);
                appearAnim.add(set);

                ObjectAnimator animatorLoad = createLoaderAnimatorForDot(dot);
                animatorLoad.setStartDelay((index - 1) * 500);
                dot.setTranslateAnimation(animatorLoad);
                loadingAnim.add(animatorLoad);

                ValueAnimator animatorColor = createColorAnimator(dot);
                animatorColor.setStartDelay((index - 1) * 500);
                dot.setColorAnimation(animatorColor);
                loadingAnim.add(animatorColor);
            }
        }
    }

    private ObjectAnimator createLoaderAnimatorForDot(final DotView dot) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(dot, TRANSLATION_Y, 0f, -15f);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ObjectAnimator.REVERSE);

       /* animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dot.setDotImage(mDefaultRad, (int) animation.getAnimatedValue());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/
        return animator;
    }

    private AnimatorSet createAppearAnimatorForDot(final View dot) {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.setDuration(500);
        set.playTogether(ObjectAnimator.ofFloat(dot, SCALE_X, 0f, 1f),
                ObjectAnimator.ofFloat(dot, SCALE_Y, 0f, 1f));
        return set;
    }

    private ValueAnimator createColorAnimator(final DotView dot) {
        ValueAnimator animator = ValueAnimator.ofInt(mDefColors);
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dot.setDotImage(mDefaultRad, (int) animation.getAnimatedValue());
            }
        });
        return animator;
    }

    void startAnimation() {
        if (appearAnim.size() != 0 && loadingAnim.size() != 0) {

            mAppearAnimatorSet = new AnimatorSet();
            mAppearAnimatorSet.playSequentially(appearAnim);

            mLoadingAnimationSet = new AnimatorSet();
            mLoadingAnimationSet.playTogether(loadingAnim);
            mLoadingAnimationSet.addListener(new AnimationRepeater(500));

            mGlobalSet = new AnimatorSet();
            mGlobalSet.playSequentially(mAppearAnimatorSet, mLoadingAnimationSet);
            mGlobalSet.start();
        }
    }

    public class AnimationRepeater implements Animator.AnimatorListener {
        private int mDelay = 500;

        public AnimationRepeater(int delay) {
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
                    resetColorDots();
                }
            }, 400);
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

    void resetColorDots() {
        for (DotView dot : dots) {
            int radius = dot.getDotType() == DotView.DotType.STATIC ? mActiveRad : mDefaultRad;
            int color = dot.getDotType() == DotView.DotType.STATIC ? mActiveColor : mDefColors[0];
            dot.setDotImage(radius, color);
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
