package com.animation.app.animateddots.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

/**
 * Created by Dasha on 04.07.2017
 */

public class GradientTextView extends AppCompatTextView {

    public GradientTextView(Context context) {
        super(context, null, -1);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs) {
        super(context, attrs, -1);
    }

    public GradientTextView(Context context,
                            AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed,
                            int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /*if (changed) {
            getPaint().setShader(new LinearGradient(
                    0, 0, 0, getHeight(),
                    Color.WHITE, Color.BLACK,
                    Shader.TileMode.CLAMP));
            startAnimation();
        }*/
    }

    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, getHeight());
        valueAnimator.setDuration(10000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int end = (int) animation.getAnimatedValue();
                getPaint().setShader(new LinearGradient(
                        0, 0, 0, end,
                        Color.WHITE, Color.BLACK,
                        Shader.TileMode.CLAMP));
            }
        });
        valueAnimator.start();
    }
}
