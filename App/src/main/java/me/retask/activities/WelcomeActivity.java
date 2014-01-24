package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import me.retask.R;
import me.retask.service.requests.ServiceRequest;
import me.retask.service.requests.SignInRequest;
import me.retask.service.requests.SignUpRequest;
import me.retask.webapi.RetaskServiceException;
import me.retask.webapi.dto.ServiceResultDto;
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

    protected void handleSuccessOnUiThread(ServiceRequest<?> request, Object result) {
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

    protected void handleErrorOnUiThread(ServiceRequest<?> request, RuntimeException exception) {
        if (request instanceof SignInRequest) {
            if (exception instanceof RetaskServiceException) {
                RetaskServiceException e = (RetaskServiceException) exception;
                if (e.errorCode == ServiceResultDto.RETASK_RESULT_VALIDATION_ERROR) {
                    Toast.makeText(WelcomeActivity.this, "Validation error", Toast.LENGTH_SHORT).show();
                } else if (e.errorCode == ServiceResultDto.RETASK_RESULT_NO_SUCH_USER) {
                    Toast.makeText(WelcomeActivity.this, "No such user", Toast.LENGTH_SHORT).show();
                } else if (e.errorCode == ServiceResultDto.RETASK_RESULT_INVALID_PASSWORD) {
                    Toast.makeText(WelcomeActivity.this, "Bad password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
