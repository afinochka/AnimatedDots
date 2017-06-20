package com.animation.app.animateddots.scroll;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.animation.app.animateddots.R;
import com.animation.app.animateddots.dots.AnimatedDot;
import com.animation.app.animateddots.dots.AnimationRepeater;
import com.animation.app.animateddots.dots.DotLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 20.06.2017
 */

public class CustomScroll extends LinearLayout {

    private static final int DELAY_BETWEEN_DOTS = 500; //80
    private static final int APPEAR_DELAY_BETWEEN_DOTS = 200; //80
    private static final int BOUNCE_ANIMATION_DURATION = 500; //500
    private static final int COLOR_ANIMATION_DURATION = 250; //80

    private int mPadding = 0;

    private View[] mDots;
    private int dotsCount = 0;

    int mDefBg;
    int mActiveBg;

    int mActivePosDot = -1;

    /*private Rect mClipBounds;*/
    private float mFromY;
    private float mToY;

    AnimatorSet mAppearAnimatorSet;
    AnimatorSet mLoadingAnimationSet;
    AnimatorSet mGlobalSet;

    List<Animator> appearAnim = new ArrayList<>();
    List<Animator> loadingAnim = new ArrayList<>();

    public CustomScroll(Context context) {
        super(context);
        init(context, null);
    }

    public CustomScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public CustomScroll(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(Context c, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomScroll, 0, 0);

        try {

            mDefBg = a.getResourceId(R.styleable.CustomScroll_dot_def_bg, 0);
            mActiveBg = a.getResourceId(R.styleable.CustomScroll_dot_active_bg, 0);
            dotsCount = a.getInteger(R.styleable.CustomScroll_count, 1);
            mPadding = a.getInteger(R.styleable.CustomScroll_padding_between_dots, 0);

            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);

            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.scroll_layout, this, true);

            init(mActivePosDot);
        }finally {
            a.recycle();
        }
    }

    private void init(int activePos) {
       /* mClipBounds = new Rect(0, 0, 0, 0);*/
        mDots = new View[dotsCount];
        Drawable drawable;
        for (int i = 0; i < dotsCount; i++) {
            mDots[i] = new View(getContext());
            if (activePos != -1 && i == mActivePosDot) {
                drawable = getShape(R.color.colorPrimary, 20);
            } else {
                drawable = getShape(R.color.active, 30);
            }

            mDots[i].setBackground(drawable);
            addView(mDots[i]);
        }

        //noinspection deprecation
       /* startAnimation();*/
    }

    private Drawable getShape(@ColorRes int color, int radius){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setSize(radius, radius);
        shape.setColor(ContextCompat.getColor(getContext(), color));
        return shape;
    }

    private void startAnimation() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                /*resetColors();*/
                _startAnimation();
            }
        }, 10);


    }

    private void _startAnimation() {
        initAnimation();

        mGlobalSet = new AnimatorSet();
        mAppearAnimatorSet = new AnimatorSet();
        mAppearAnimatorSet.playSequentially(appearAnim);
        mLoadingAnimationSet = new AnimatorSet();
        mLoadingAnimationSet.playTogether(loadingAnim);
        mGlobalSet.playSequentially(mAppearAnimatorSet, mLoadingAnimationSet);
        mGlobalSet.start();
    }

    public void initAnimation() {
        for (int i = 0, size = mDots.length; i < size; i++) {
            if (i != mActivePosDot) {

                appearAnim.add(createLoaderAnimatorForDot(mDots[i]));
                loadingAnim.add(createAppearAnimatorForDot(mDots[i]));
            }
        }
    }

    private ValueAnimator createLoaderAnimatorForDot(final View dot) {

        final ObjectAnimator animator = ObjectAnimator.ofFloat(dot, TRANSLATION_Y, mFromY, mToY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(BOUNCE_ANIMATION_DURATION);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        /*animator.addUpdateListener(new DotYUpdater(dot, this));*/
        animator.addListener(new AnimationRepeater((mDots.length - 1) * DELAY_BETWEEN_DOTS));
        return animator;
    }

    /*private static class DotYUpdater implements ValueAnimator.AnimatorUpdateListener {
        private View mDot;
        private WeakReference<DotLoader> mDotLoaderRef;

        private DotYUpdater(View dot, DotLoader dotLoader) {
            mDot = dot;
            mDotLoaderRef = new WeakReference<>(dotLoader);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mDot.cy = (float) valueAnimator.getAnimatedValue();
            DotLoader dotLoader = mDotLoaderRef.get();
            if (dotLoader != null) {
                dotLoader.invalidateOnlyRectIfPossible();
            }
        }
    }*/

   /* private void invalidateOnlyRectIfPossible() {
        if (mClipBounds != null && mClipBounds.left != 0 &&
                mClipBounds.top != 0 && mClipBounds.right != 0 && mClipBounds.bottom != 0)
            invalidate(mClipBounds);
        else
            invalidate();
    }*/

    private ValueAnimator createAppearAnimatorForDot(final View dot) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(dot, SCALE_X, 0, 1);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(300);
       /* animator.addUpdateListener(new DotLoader.DotRUpdater(dot, this));*/
        return animator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        // desired height is 3 times the diameter of a dot.

        int desiredHeight = (int) (mPadding + getPaddingTop() + getPaddingBottom());

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        int desiredWidth = mPadding * mDots.length;
        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }
        for (int i = 0, size = mDots.length; i < size; i++) {
            mDots[i].setPadding(0, 0, mPadding, 0);
        }

        int dotWidth;
        int dotHeight;
        mDots[0].measure(MeasureSpec.EXACTLY, MeasureSpec.EXACTLY);

        dotWidth = mDots[0].getMeasuredWidth();
        dotHeight = mDots[0].getMeasuredHeight();

        mFromY = height - dotHeight / 2;
        mToY = dotHeight / 2;

        setMeasuredDimension(width, height);
    }

    public class AnimationRepeater implements Animator.AnimatorListener {
        private int mDelay = 1000;

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
        }

        @Override
        public void onAnimationCancel(Animator animator) {

        }

        @Override
        public void onAnimationRepeat(Animator animator) {
        }
    }

}
