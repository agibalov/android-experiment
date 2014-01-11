package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.inject.Inject;

import me.loki2302.R;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.apicalls.SignUpApiCall;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.views.FixedViewsPagerAdapter;
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
        FixedViewsPagerAdapter pagerAdapter = new FixedViewsPagerAdapter(new View[] {
                signInView,
                signUpView
        });

        signInUi = new SignInUi(signInView);
        signInUi.setListener(this);
        Credentials credentials = preferencesService.getCredentials();
        if(credentials != null) {
            signInUi.setEmail(credentials.email);
            signInUi.setPassword(credentials.password);
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
        final String email = signInUi.getEmail();
        final String password = signInUi.getPassword();
        run(new SignInApiCall(email, password), new OnSignInDoneCallback(email, password), new OnSignInFailedCallback());
    }

    private class OnSignInDoneCallback implements DoneCallback<SessionDto> {
        private final String email;
        private final String password;

        public OnSignInDoneCallback(String email, String password) {
            this.email = email;
            this.password = password;
        }

        @Override
        public void onDone(SessionDto sessionDto) {
            Ln.i("Authenticated: %s", sessionDto.sessionToken);

            applicationState.setSessionToken(sessionDto.sessionToken);

            if(signInUi.isRememberMeChecked()) {
                Credentials credentials = new Credentials(email, password);
                preferencesService.setCredentials(credentials);
            }

            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class OnSignInFailedCallback extends DefaultFailCallback {
        @Override
        protected void onNoSuchUser(ServiceResultDto<?> serviceResult) {
            Toast.makeText(WelcomeActivity.this, "No such user", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onInvalidPassword(ServiceResultDto<?> serviceResult) {
            Toast.makeText(WelcomeActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onValidationError(ServiceResultDto<?> serviceResult) {
            Toast.makeText(WelcomeActivity.this, "Validation error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSignUpClicked() {
        final String email = signUpUi.getEmail();
        final String password = signUpUi.getPassword();

        if(false) {
            run(new SignUpApiCall(email, password), new DoneCallback<Object>() {
                @Override
                public void onDone(Object o) {
                    Intent intent = new Intent(WelcomeActivity.this, SignedUpActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                }
            });
        }

        Intent intent = new Intent(WelcomeActivity.this, SignedUpActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
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
