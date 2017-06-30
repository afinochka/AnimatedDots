package com.animation.app.animateddots;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.animation.app.animateddots.Indicator.PagerIndicator;
import com.animation.app.animateddots.viewpager.MyPageTransformer;
import com.animation.app.animateddots.viewpager.MyPagerAdapter;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final int PAGE_COUNT = 10;
    Button btnStart;
    Button btnStop;

    TextView mTxtDots;

    PagerIndicator mPagerIndicator;

    ViewPager mViewPager;
    PagerAdapter pagerAdapter;

    String[] mStrings = new String[]{"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtDots = (TextView) findViewById(R.id.txt_dots);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.BLACK, Color.TRANSPARENT);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mTxtDots.setTextColor((Integer) animator.getAnimatedValue());
            }

        });

        mViewPager = (ViewPager) findViewById(R.id.vp);
        pagerAdapter = new MyPagerAdapter(this, Arrays.asList(mStrings));
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setPageTransformer(true, new MyPageTransformer(this));
        mViewPager.setClipToPadding(false);
        mViewPager.setPageMargin(40);   //56
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mPagerIndicator.changeDots(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoopPagerActivity.class);
                startActivity(intent);
            }
        });


        colorAnimation.setDuration(5000);
        colorAnimation.start();

        /*btnStart = (Button) findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerIndicator.startAnimation();
            }
        });

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerIndicator.stopAnimation();
            }
        });*/

        mPagerIndicator = (PagerIndicator) findViewById(R.id.lyt_indicator);
        mPagerIndicator.initDots(mStrings.length);
        mPagerIndicator.startAnimation();

    }

}
