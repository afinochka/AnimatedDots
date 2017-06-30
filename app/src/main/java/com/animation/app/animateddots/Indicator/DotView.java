package com.animation.app.animateddots.Indicator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Dasha on 21.06.2017
 */

public class DotView extends android.support.v7.widget.AppCompatImageView {

    public enum DotType {
        ANIMATED,
        STATIC
    }

    AnimatorSet appearAnimation;
    ObjectAnimator translateAnimation;

    private DotType mDotType = DotType.STATIC;

    private int mColor = 0;
    private float mRadius = 0;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDotImage(float radius, int color) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setSize((int) radius, (int) radius);
        shape.setColor(color);

        mRadius = radius;
        mColor = color;
        setImageDrawable(shape);
    }

    public float getRaduis() {
        return mRadius;
    }

    public void setDotType(DotType type) {
        mDotType = type;
        setScale(mDotType);

    }

    private void setScale(DotType type) {
        float scale = 1f;
        if (type == DotType.ANIMATED) {
            scale = 0f;
        }

        setScaleY(scale);
        setScaleX(scale);
    }

    public int getColor() {
        return mColor;
    }

    public void setAppearAnimation(AnimatorSet appearAnimation) {
        this.appearAnimation = appearAnimation;
    }

    public AnimatorSet getAppearAnimation() {
        return appearAnimation;
    }

    public void setTranslateAnimation(ObjectAnimator translateAnimation) {
        this.translateAnimation = translateAnimation;
    }

    public ObjectAnimator getTranslateAnimation() {
        return translateAnimation;
    }

    public DotType getDotType() {
        return mDotType;
    }

    @Override
    public void clearAnimation() {
        super.clearAnimation();
        if (appearAnimation != null) {
            appearAnimation.cancel();
        }
        if (translateAnimation != null) {
            translateAnimation.cancel();
        }
    }
}
