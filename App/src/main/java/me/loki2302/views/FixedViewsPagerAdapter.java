package me.loki2302.views;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class FixedViewsPagerAdapter extends PagerAdapter {
    private final View[] views;

    public FixedViewsPagerAdapter(View[] views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = views[position];
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }
}
