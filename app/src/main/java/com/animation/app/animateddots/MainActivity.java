package com.animation.app.animateddots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final int DELAY_BETWEEN_DOTS = 500; //80
    private static final int APPEAR_DELAY_BETWEEN_DOTS = 200; //80
    private static final int BOUNCE_ANIMATION_DURATION = 500; //500
    private static final int COLOR_ANIMATION_DURATION = 250; //80

    Button btnNext;
    Button btnPrev;

    PagerIndicator mPagerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerIndicator.getNext();
            }
        });

        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPagerIndicator.getPrev();
            }
        });

        mPagerIndicator = (PagerIndicator) findViewById(R.id.lyt_indicator);
        mPagerIndicator.startAnimation();
    }


}
