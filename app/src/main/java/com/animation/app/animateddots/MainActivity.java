package com.animation.app.animateddots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.animation.app.animateddots.dots.DotLoader;

public class MainActivity extends AppCompatActivity {

    DotLoader dotLoader;
    DotLoader textDotLoader;
    Button relaunch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dotLoader = (DotLoader) findViewById(R.id.dot_loader);
        relaunch = (Button) findViewById(R.id.btn);
        relaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        });
       /* textDotLoader = (DotLoader) findViewById(R.id.text_dot_loader);*/
        // testing set number of dots after some delay
        /*dotLoader.postDelayed(new Runnable() {
            @Override
            public void run() {
                dotLoader.setNumberOfDots(5);
            }
        }, 3000);

        dotLoader.postDelayed(new Runnable() {
            @Override
            public void run() {
                dotLoader.setNumberOfDots(6);
            }
        }, 6000);*/

        /*for (int i = 1; i < 4; i++) {
            textDotLoader.postDelayed(new DotIncrementRunnable(3 + i, textDotLoader), i * 3000);
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        dotLoader.resetColors();
        textDotLoader.resetColors();
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
            mDotLoader.setNumberOfDots(mNumberOfDots);
        }
    }
}
