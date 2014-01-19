package me.retask.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SwimlanesAdapter extends FragmentPagerAdapter {
    private final Bundle[] bundles;

    public SwimlanesAdapter(FragmentManager fragmentManager, Bundle[] bundles) {
        super(fragmentManager);
        this.bundles = bundles;
    }

    @Override
    public Fragment getItem(int position) {
        if(position > getCount() - 1) {
            throw new IllegalArgumentException();
        }

        Bundle args = bundles[position];
        SwimlaneFragment swimlaneFragment = new SwimlaneFragment();
        swimlaneFragment.setArguments(args);
        return swimlaneFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
