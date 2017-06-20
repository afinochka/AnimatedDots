package com.animation.app.animateddots;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.animation.app.animateddots.dots.DotLoader;

public class MainActivity extends AppCompatActivity {

    DotLoader dotLoader;
    Button btnRepeat;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*    dotLoader = (DotLoader) findViewById(R.id.dot_loader);
        btnRepeat = (Button) findViewById(R.id.btn_repeat);
        btnNext = (Button) findViewById(R.id.btn_move);
        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotLoader.repeatAnimation();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dotLoader.changeDots(dotLoader.getNext());
            }
        });*/
    }

    private static class DotIncrementRunnable implements Runnable {
        private int mNumberOfDots;
        private DotLoader mDotLoader;

        private DotIncrementRunnable(int numberOfDots, DotLoader dotLoader) {
            mNumberOfDots = numberOfDots;
            mDotLoader = dotLoader;
        }

        @Override
        public void run() {
          /*  mDotLoader.setNumberOfDots(mNumberOfDots);*/
        }
    }
}
