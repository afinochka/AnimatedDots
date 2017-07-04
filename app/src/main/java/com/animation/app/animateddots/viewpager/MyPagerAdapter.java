package com.animation.app.animateddots.viewpager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.animation.app.animateddots.R;
import com.animation.app.animateddots.custom.GradientTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 30.06.2017
 */

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mStrings = new ArrayList<>();
    Spannable wordtoSpan;
    private long startTime, currentTime, finishedTime = 0L;
    private int duration = 22000 / 4;// 1 character is equal to 1 second. if want to
    // reduce. can use as divide
    // by 2,4,8
    private int endTime = 0;
    private Handler handler;
    TextView mTxtLoading;

    public MyPagerAdapter(Context context) {
        mContext = context;
    }

    public void setStrings(List<String> strings) {
        mStrings = strings;
        notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.lyt_page, collection, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_discount);
        imageView.setImageResource(R.drawable.img);
        TextView text = (TextView) view.findViewById(R.id.txt_title);
        text.setText(mStrings.get(position) + position);

        mTxtLoading = (TextView) view.findViewById(R.id.txt_loading);
        wordtoSpan = new SpannableString("loading...");
       /* wordtoSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(view.getContext(), R.color.active)), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTxtLoading.setText(wordtoSpan);*/

        ValueAnimator valueAnimator = ValueAnimator.ofInt(ContextCompat.getColor(view.getContext(), R.color.active), Color.BLACK, Color.WHITE);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue();
                wordtoSpan.setSpan(new ForegroundColorSpan(color), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTxtLoading.setText(wordtoSpan);
            }
        });
        valueAnimator.start();

       /* mTxtLoading.startAnimation();*/

        collection.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mStrings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
