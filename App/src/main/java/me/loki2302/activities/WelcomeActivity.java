package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import me.loki2302.R;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.dto.SessionDto;
import roboguice.util.Ln;

public class WelcomeActivity extends RetaskActivity implements SignInUi.SignInUiListener, SignUpUi.SignUpUiListener, ActionBar.TabListener {
    @Inject
    private PreferencesService preferencesService;

    @Inject
    private ConnectivityService connectivityService;

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
        WelcomePagerAdapter pagerAdapter = new WelcomePagerAdapter(new View[] {
                signInView,
                signUpView
        });

        signInUi = new SignInUi(signInView);
        signInUi.setListener(this);

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
        final String email = signInUi.getEmail();
        final String password = signInUi.getPassword();
        run(new SignInApiCall(email, password), new DoneCallback<SessionDto>() {
            @Override
            public void onDone(SessionDto result) {
                Ln.i("Authenticated: %s", result.sessionToken);

                applicationState.setSessionToken(result.sessionToken);

                if(signInUi.isRememberMeChecked()) {
                    Credentials credentials = new Credentials(email, password);
                    preferencesService.setCredentials(credentials);
                }

                Intent intent = new Intent(WelcomeActivity.this, WorkspaceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onSignUpClicked() {
        // TODO
        Ln.i("Sign up clicked");
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

    private static class WelcomePagerAdapter extends PagerAdapter {
        private final View[] views;

        public WelcomePagerAdapter(View[] views) {
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
}
