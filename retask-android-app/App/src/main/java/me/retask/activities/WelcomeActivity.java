package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

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

    @Override
    protected void handleRetaskValidationError(ServiceRequest<?> request, Map<String, List<String>> fieldsInError) {
        StringBuilder sb = new StringBuilder();
        for(String key : fieldsInError.keySet()) {
            sb.append(key);
            sb.append(": ");
            sb.append(fieldsInError.get(key).get(0));
            sb.append("\n");
        }
        String message = sb.toString().trim();

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleRetaskNoSuchUserError(ServiceRequest<?> request) {
        Toast.makeText(this, "No such user", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleRetaskInvalidPasswordError(ServiceRequest<?> request) {
        Toast.makeText(this, "Bad password", Toast.LENGTH_SHORT).show();
    }
}
