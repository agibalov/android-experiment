package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.application.Credentials;
import me.retask.application.PreferencesService;
import me.retask.dal.ApplicationState;
import me.retask.service.requests.SignInRetaskServiceRequest;
import me.retask.service.requests.SignUpRetaskServiceRequest;
import me.retask.views.FixedViewsPagerAdapter;

public class WelcomeActivity extends RetaskActivity implements SignInUi.SignInUiListener, SignUpUi.SignUpUiListener, ActionBar.TabListener {
    @Inject
    private PreferencesService preferencesService;

    @Inject
    private ApplicationState applicationState;

    private ViewPager viewPager;
    private SignInUi signInUi;
    private SignUpUi signUpUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_view);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        LayoutInflater layoutInflater = getLayoutInflater();
        View signInView = layoutInflater.inflate(R.layout.sign_in_view, null);
        View signUpView = layoutInflater.inflate(R.layout.sign_up_view, null);
        FixedViewsPagerAdapter pagerAdapter = new FixedViewsPagerAdapter(new View[] {
                signInView,
                signUpView
        });

        signInUi = new SignInUi(signInView);
        signInUi.setListener(this);
        Credentials credentials = preferencesService.getCredentials();
        if(credentials != null) {
            signInUi.setEmail(credentials.getEmail());
            signInUi.setPassword(credentials.getPassword());
            signInUi.setRememberMeChecked(true);
        }

        signUpUi = new SignUpUi(signUpView);
        signUpUi.setListener(this);

        viewPager.setAdapter(pagerAdapter);
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
    public void onSignInClicked() {
        String email = signInUi.getEmail();
        String password = signInUi.getPassword();
        run(new SignInRetaskServiceRequest(email, password));

        Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSignUpClicked() {
        String email = signUpUi.getEmail();
        String password = signUpUi.getPassword();
        run(new SignUpRetaskServiceRequest(email, password));
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
}
