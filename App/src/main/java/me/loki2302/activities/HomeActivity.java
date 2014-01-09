package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.inject.Inject;

import java.util.List;

import me.loki2302.R;
import me.loki2302.application.Repository;
import me.loki2302.application.Task;
import me.loki2302.application.TaskStatusIsQuery;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.GetWorkspaceApiCall;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskStatus;
import me.loki2302.dal.dto.WorkspaceDto;
import me.loki2302.views.OnTaskThumbnailClickedListener;
import me.loki2302.views.SwimlaneView;

public class HomeActivity extends RetaskActivity implements ActionBar.TabListener, OnTaskThumbnailClickedListener {
    @Inject
    private PreferencesService preferencesService;

    @Inject
    private ApplicationState applicationState;

    private ViewPager swimlanesViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        SwimlaneView[] swimlaneViews = new SwimlaneView[] {
                new SwimlaneView(this),
                new SwimlaneView(this),
                new SwimlaneView(this)
        };

        SwimlanePagerAdapter swimlanePagerAdapter = new SwimlanePagerAdapter(swimlaneViews);
        swimlanesViewPager = (ViewPager)findViewById(R.id.swimlanesViewPager);
        swimlanesViewPager.setAdapter(swimlanePagerAdapter);

        swimlanesViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        actionBar.addTab(actionBar.newTab().setText("TO DO").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("DOING").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("DONE").setTabListener(this));

        run(new GetWorkspaceApiCall(applicationState.getSessionToken()), new OnWorkspaceDataAvailable(applicationState, swimlaneViews));
    }

    @Override
    public void onTaskThumbnailClicked(Task model) {
        Intent intent = new Intent(HomeActivity.this, ViewTaskActivity.class);
        intent.putExtra("taskId", model.id);
        startActivity(intent);
    }

    private class OnWorkspaceDataAvailable implements DoneCallback<WorkspaceDto> {
        private final ApplicationState applicationState;
        private final SwimlaneView[] swimlaneViews;

        public OnWorkspaceDataAvailable(ApplicationState applicationState, SwimlaneView[] swimlaneViews) {
            this.applicationState = applicationState;
            this.swimlaneViews = swimlaneViews;
        }

        @Override
        public void onDone(WorkspaceDto workspaceDto) {
            Repository<Task> taskRepository = applicationState.getTaskRepository();
            for (TaskDto taskDto : workspaceDto.tasks) {
                Task task = taskFromTaskDto(taskDto);
                taskRepository.add(task);
            }

            List<Task> toDoTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.NotStarted));
            swimlaneViews[0].setModel(toDoTasks, HomeActivity.this);

            List<Task> inProgressTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.InProgress));
            swimlaneViews[1].setModel(inProgressTasks, HomeActivity.this);

            List<Task> doneTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.Done));
            swimlaneViews[2].setModel(doneTasks, HomeActivity.this);
        }

        private Task taskFromTaskDto(TaskDto taskDto) {
            Task task = new Task();
            task.id = taskDto.taskId;
            task.description = taskDto.taskDescription;
            task.status = taskDto.taskStatus;
            return task;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.workspace_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.createTaskMenuItem) {
            Intent intent = new Intent(HomeActivity.this, CreateTaskActivity.class);
            startActivity(intent);
            return true;
        } else if(itemId == R.id.resetMenuItem) {
            preferencesService.unsetCredentials();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        swimlanesViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public static class SwimlanePagerAdapter extends PagerAdapter {
        private final SwimlaneView[] swimlaneViews;

        public SwimlanePagerAdapter(SwimlaneView[] swimlaneViews) {
            this.swimlaneViews = swimlaneViews;
        }

        @Override
        public int getCount() {
            return swimlaneViews.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SwimlaneView swimlaneView = swimlaneViews[position];
            container.addView(swimlaneView);
            return swimlaneView;
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
