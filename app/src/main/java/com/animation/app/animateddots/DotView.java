package com.animation.app.animateddots;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

/**
 * Created by Dasha on 21.06.2017
 */

public class DotView extends android.support.v7.widget.AppCompatImageView {

    enum DotType {
        ANIMATED,
        STATIC
    }

    AnimatorSet appearAnimation;
    ValueAnimator colorAnimation;
    ObjectAnimator translateAnimation;

    private DotType mDotType = DotType.STATIC;

    public DotView(Context context) {
        super(context);
    }

    public DotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    void setDotImage(int radius, int color){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setSize(radius, radius);
        shape.setColor(color);

        setImageDrawable(shape);
    }

    void setDotType(DotType type){
        mDotType = type;
        setScale(mDotType);

    }

    private void setScale(DotType type){
        float scale = 1f;
        if(type == DotType.ANIMATED){
            scale = 0f;
        }

        setScaleY(scale);
        setScaleX(scale);
    }

    public void setAppearAnimation(AnimatorSet appearAnimation) {
        this.appearAnimation = appearAnimation;
    }

    public AnimatorSet getAppearAnimation() {
        return appearAnimation;
    }

    public void setColorAnimation(ValueAnimator colorAnimation) {
        this.colorAnimation = colorAnimation;
    }

    public ValueAnimator getColorAnimation() {
        return colorAnimation;
    }

    public void setTranslateAnimation(ObjectAnimator translateAnimation) {
        this.translateAnimation = translateAnimation;
    }

    public ObjectAnimator getTranslateAnimation() {
        return translateAnimation;
    }

    public DotType getDotType(){
        return mDotType;
    }
}
