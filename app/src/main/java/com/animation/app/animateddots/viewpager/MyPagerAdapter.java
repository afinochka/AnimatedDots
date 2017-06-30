package com.animation.app.animateddots.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.animation.app.animateddots.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dasha on 30.06.2017
 */

public class MyPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mStrings = new ArrayList<>();

    public MyPagerAdapter(Context context, List<String> strings) {
        mContext = context;
        mStrings = strings;
    }

    public void setStrings(ArrayList<String> strings){
        mStrings = strings;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.lyt_page, collection, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_discount);
        imageView.setImageResource(R.drawable.img);
        TextView text = (TextView) view.findViewById(R.id.txt_title);
        text.setText(mStrings.get(position) + position);
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
