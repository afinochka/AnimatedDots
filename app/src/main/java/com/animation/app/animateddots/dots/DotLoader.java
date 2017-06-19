package com.animation.app.animateddots.dots;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.animation.app.animateddots.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 15.06.2017
 */

public class DotLoader extends View  {
    private static final int DELAY_BETWEEN_DOTS = 500; //80
    private static final int APPEAR_DELAY_BETWEEN_DOTS = 200; //80
    private static final int BOUNCE_ANIMATION_DURATION = 500; //500
    private static final int COLOR_ANIMATION_DURATION = 250; //80

    private IDot[] mDots;
    private int dotsCount = 0;

    Integer[] mDefColors;
    float mDefRadius;

    int mActiveColor;
    int mActivePosDot = -1;
    float mActiveRadius;

    private Rect mClipBounds;
    private float mCalculatedGapBetweenDotCenters;
    private float mFromY;
    private float mToY;

    AnimatorSet mAppearAnimatorSet;
    AnimatorSet mLoadingAnimationSet;
    AnimatorSet mGlobalSet;

    List<Animator> appearAnim = new ArrayList<>();
    List<Animator> loadingAnim = new ArrayList<>();

    private Interpolator bounceAnimationInterpolator =
            new CubicBezierInterpolator(0.62f, 0.28f, 0.23f, 0.99f); //0.62f, 0.28f, 0.23f, 0.99f


    public DotLoader(Context context) {
        super(context);
        init(context, null);
    }

    public DotLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DotLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(21)
    public DotLoader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context c, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.DotLoader, 0, 0);
        try {
            mDefRadius = a.getDimension(R.styleable.DotLoader_dot_def_radius, 0);
            mActiveRadius = a.getDimension(R.styleable.DotLoader_dot_active_radius, 0);
            dotsCount = a.getInteger(R.styleable.DotLoader_number_of_dots, 1);
            int colorArrayId = a.getResourceId(R.styleable.DotLoader_color_array_def, 0);
            int colorActiveId = a.getResourceId(R.styleable.DotLoader_color_active, 0);
            if(colorActiveId != 0){
                mActiveColor = ContextCompat.getColor(getContext(), colorActiveId);
                mActivePosDot = 0;
            }

            if (colorArrayId == 0) {
                mDefColors = new Integer[dotsCount];
                for (int i = 1; i < dotsCount; i++) {
                    mDefColors[i] = 0x0;
                }
            } else {
                int[] temp = getResources().getIntArray(colorArrayId);
                mDefColors = new Integer[temp.length];
                for (int i = 0; i < temp.length; i++) {
                    mDefColors[i] = temp[i];
                }
            }

            init(mActivePosDot);
        } finally {
            a.recycle();
        }
    }

    private void _stopAnimations() {
        mAppearAnimatorSet.cancel();
        mLoadingAnimationSet.cancel();
        mGlobalSet.cancel();
    }

    private void init(int activePos) {
        mClipBounds = new Rect(0, 0, 0, 0);
        mDots = new IDot[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            if(activePos != -1 && i == mActivePosDot) {
                mDots[i] = IDot.getDot(IDot.DotType.STATIC);
                mDots[i].initDot(mActiveRadius, i, mActiveColor);
            }else {
                mDots[i] = IDot.getDot(IDot.DotType.ANIMATED);
                ((AnimatedDot) mDots[i]).setColors(mDefColors);
                mDots[i].initDot(0, i, mDefColors[0]);
            }
        }

        //noinspection deprecation
        startAnimation();
    }

    public void initAnimation() {
        for (int i = 0, size = mDots.length; i < size; i++) {
            if(i != mActivePosDot) {

                ((AnimatedDot) mDots[i]).setLoaderAnimator(createLoaderAnimatorForDot((AnimatedDot) mDots[i]), DELAY_BETWEEN_DOTS * i);
                loadingAnim.add(((AnimatedDot) mDots[i]).getLoaderAnimator());

             /*   ((AnimatedDot) mDots[i]).setColorAnimator(createColorAnimatorForDot((AnimatedDot) mDots[i]));*/

                ((AnimatedDot) mDots[i]).setAppearAnimator(createAppearAnimatorForDot((AnimatedDot) mDots[i]), APPEAR_DELAY_BETWEEN_DOTS);
                appearAnim.add(((AnimatedDot) mDots[i]).getAppearAnimator());
            }
        }
    }

    private ValueAnimator createLoaderAnimatorForDot(final AnimatedDot dot) {
        final ValueAnimator animator = ValueAnimator.ofFloat(mFromY, mToY);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(BOUNCE_ANIMATION_DURATION);
        animator.setRepeatCount(1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.addUpdateListener(new DotYUpdater(dot, this));
        animator.addListener(new AnimationRepeater(dot, (mDots.length - 1) * DELAY_BETWEEN_DOTS));
        return animator;
    }

    private ValueAnimator createAppearAnimatorForDot(final AnimatedDot dot) {
        final ValueAnimator animator = ValueAnimator.ofFloat(0, mDefRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(new DotRUpdater(dot, this));
        return animator;
    }

    /**
     * Won't support starting and stopping animations for now, until I figure out how to sync animation
     * delays.
     *
     * @deprecated
     */
    private void startAnimation() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                /*resetColors();*/
                _startAnimation();
            }
        }, 10);


    }

    /**
     * Won't support starting and stopping animations for now, until I figure out how to sync animation
     * delays.
     *
     * @deprecated
     */
    @SuppressWarnings("unused")
    private void stopAnimations() {
        post(new Runnable() {
            @Override
            public void run() {
                _stopAnimations();
            }
        });
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

    public void repeatAnimation(){
        //noinspection deprecation
        stopAnimations();

        //noinspection deprecation
        init(mActivePosDot);
    }

    private ValueAnimator createColorAnimatorForDot(AnimatedDot dot) {
        ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), mDefColors[dot.getCurrentColorIndex()],
                mDefColors[dot.incrementColorIndex()]);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(COLOR_ANIMATION_DURATION);
        animator.addUpdateListener(new DotColorUpdater(dot, this));

        return animator;
    }

    private static class DotColorUpdater implements ValueAnimator.AnimatorUpdateListener {
        private AnimatedDot mDot;
        private WeakReference<DotLoader> mDotLoaderRef;

        private DotColorUpdater(AnimatedDot dot, DotLoader dotLoader) {
            mDot = dot;
            mDotLoaderRef = new WeakReference<>(dotLoader);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mDot.setColor((Integer) valueAnimator.getAnimatedValue());
            DotLoader dotLoader = mDotLoaderRef.get();
            if (dotLoader != null) {
                dotLoader.invalidateOnlyRectIfPossible();
            }
        }
    }

    private static class DotYUpdater implements ValueAnimator.AnimatorUpdateListener {
        private AnimatedDot mDot;
        private WeakReference<DotLoader> mDotLoaderRef;

        private DotYUpdater(AnimatedDot dot, DotLoader dotLoader) {
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
    }

    private static class DotRUpdater implements ValueAnimator.AnimatorUpdateListener {
        private AnimatedDot mDot;
        private WeakReference<DotLoader> mDotLoaderRef;

        private DotRUpdater(AnimatedDot dot, DotLoader dotLoader) {
            mDot = dot;
            mDotLoaderRef = new WeakReference<>(dotLoader);
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            mDot.setRadius((float) valueAnimator.getAnimatedValue());
            DotLoader dotLoader = mDotLoaderRef.get();
            if (dotLoader != null) {
                dotLoader.invalidateOnlyRectIfPossible();
            }
        }
    }


    private void invalidateOnlyRectIfPossible() {
        if (mClipBounds != null && mClipBounds.left != 0 &&
                mClipBounds.top != 0 && mClipBounds.right != 0 && mClipBounds.bottom != 0)
            invalidate(mClipBounds);
        else
            invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(mClipBounds);
        for (IDot mDot : mDots) {
            mDot.draw(canvas);
        }
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
        float radius = mActiveRadius == 0 ? mDefRadius : mActiveRadius;

        int desiredHeight = (int) ((radius * 3) + getPaddingTop() + getPaddingBottom());

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
        mCalculatedGapBetweenDotCenters = calculateGapBetweenDotCenters(radius);
        int desiredWidth = (int) (mCalculatedGapBetweenDotCenters * mDots.length);
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
            mDots[i].cx = (radius + i * mCalculatedGapBetweenDotCenters);
            mDots[i].cy = height - radius;
        }
        mFromY = height - radius;
        mToY = radius;
        setMeasuredDimension(width, height);
    }

    private float calculateGapBetweenDotCenters(float radius) {
        return (radius * 2) + radius;
    }

    public int getNext(){
        int pos = 0;
        if(mActivePosDot < (dotsCount - 1)) {
            pos = mActivePosDot;
            return ++pos;
        }
        return pos;
    }

    public int getPrev(){
        int prev = --mActivePosDot;
        if(prev < 0) {
            return (dotsCount - 1);
        }
        return prev;
    }

    public void changeDots(int position) {
        int prev = getPrev();
        for (int i = 0; i < dotsCount; i++) {
            if(prev == i) {
                setDefDot(i);
            }
        }

        setActiveDot(position);
        mActivePosDot = position;
    }

    private void setActiveDot(int pos){
        mDots[pos] = new StaticDot(mActiveRadius, pos, mActiveColor);
    }

    private void setDefDot(int pos){
        mDots[pos] = new AnimatedDot(mDefRadius, pos, mDefColors[0]);
       /* ((AnimatedDot) mDots[pos]).startAnimation((mDots.length - 1) * APPEAR_DELAY_BETWEEN_DOTS);*/
    }
}
