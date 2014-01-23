package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import me.retask.R;
import me.retask.service.requests.ServiceRequest;
import me.retask.service.requests.SignInRequest;
import me.retask.service.requests.SignUpRequest;
import roboguice.util.Ln;

public class WelcomeActivity extends RetaskActivity implements ActionBar.TabListener {
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_view);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        WelcomeScreenPagerAdapter welcomeScreenPagerAdapter = new WelcomeScreenPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(welcomeScreenPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText("Sign In").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Sign Up").setTabListener(this));
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onSuccess(String requestToken, ServiceRequest<?> request, Object result) {
        super.onSuccess(requestToken, request, result);

        if(request instanceof SignInRequest) {
            Ln.i("Signed in");
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        if(request instanceof SignUpRequest) {
            Ln.i("Signed up");
            return;
        }

        throw new IllegalStateException("Didn't expect this request here");
    }
}
