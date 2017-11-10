package me.retask.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class WelcomeScreenPagerAdapter extends FragmentPagerAdapter {
    public WelcomeScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return new SignInFragment();
        }

        if(position == 1) {
            return new SignUpFragment();
        }

        throw new IllegalArgumentException();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
