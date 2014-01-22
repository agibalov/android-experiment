package me.retask.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.application.PreferencesService;
import me.retask.dal.RetaskContract;
import roboguice.util.Ln;

public class HomeActivity extends RetaskActivity implements ActionBar.TabListener, SwimlaneFragment.OnTaskThumbnailClickListener {
    @Inject
    private PreferencesService preferencesService;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        Bundle todoBundle = new Bundle();
        todoBundle.putInt(SwimlaneFragment.ARG_STATUS, RetaskContract.Task.TASK_STATUS_TODO);

        Bundle inProgressBundle = new Bundle();
        inProgressBundle.putInt(SwimlaneFragment.ARG_STATUS, RetaskContract.Task.TASK_STATUS_IN_PROGRESS);

        Bundle doneBundle = new Bundle();
        doneBundle.putInt(SwimlaneFragment.ARG_STATUS, RetaskContract.Task.TASK_STATUS_DONE);

        Bundle[] bundles = new Bundle[] { todoBundle, inProgressBundle, doneBundle };
        HomeScreenPagerAdapter homeScreenPagerAdapter = new HomeScreenPagerAdapter(getSupportFragmentManager(), bundles);

        viewPager = (ViewPager)findViewById(R.id.swimlanesViewPager);
        viewPager.setAdapter(homeScreenPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText("TO DO").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("DOING").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("DONE").setTabListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.createTaskMenuItem) {
            Intent intent = new Intent(HomeActivity.this, CreateTaskActivity.class);
            startActivity(intent);
            return true;
        }

        if(itemId == R.id.signOutMenuItem) {
            preferencesService.unsetCredentials();
            Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void onTaskThumbnailClick(long taskId) {
        Ln.i("Task clicked: %d", taskId);
        Intent intent = new Intent(this, ViewTaskActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }
}
