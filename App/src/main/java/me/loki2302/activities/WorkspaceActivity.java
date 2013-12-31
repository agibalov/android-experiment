package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import me.loki2302.views.ModelListAdapter;
import me.loki2302.views.TaskThumbnailView;
import me.loki2302.views.ViewFactory;
import roboguice.util.Ln;

public class WorkspaceActivity extends RetaskActivity implements ActionBar.TabListener {
    @Inject
    private PreferencesService preferencesService;

    @Inject
    private ApplicationState applicationState;

    private ViewPager swimlanesViewPager;

    private SwimlaneFragment toDoSwimlaneFragment;
    private SwimlaneFragment inProgressSwimlaneFragment;
    private SwimlaneFragment doneSwimlaneFragment;
    private SwimlanesPagesAdapter swimlanesPagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_view);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        toDoSwimlaneFragment = SwimlaneFragment.newInstance("TO DO");
        inProgressSwimlaneFragment = SwimlaneFragment.newInstance("DOING");
        doneSwimlaneFragment = SwimlaneFragment.newInstance("DONE");
        SwimlaneFragment[] swimlaneFragments = new SwimlaneFragment[] {
                toDoSwimlaneFragment,
                inProgressSwimlaneFragment,
                doneSwimlaneFragment
        };

        swimlanesPagesAdapter = new SwimlanesPagesAdapter(
                getSupportFragmentManager(),
                swimlaneFragments);
        swimlanesViewPager = (ViewPager)findViewById(R.id.swimlanesViewPager);
        swimlanesViewPager.setAdapter(swimlanesPagesAdapter);

        swimlanesViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < swimlanesPagesAdapter.getCount(); i++) {
            actionBar.addTab(actionBar
                    .newTab()
                    .setText(swimlanesPagesAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        run(new GetWorkspaceApiCall(applicationState.getSessionToken()), onWorkspaceDataAvailable);
    }

    private final DoneCallback<WorkspaceDto> onWorkspaceDataAvailable = new DoneCallback<WorkspaceDto>() {
        @Override
        public void onDone(WorkspaceDto result) {
            Repository<Task> taskRepository = applicationState.getTaskRepository();

            for (TaskDto taskDto : result.tasks) {
                Task task = taskFromTaskDto(taskDto);
                taskRepository.add(task);
            }

            Ln.i("Got %d tasks", result.tasks.size());

            List<Task> toDoTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.NotStarted));
            Ln.i("Setting model for %s", toDoSwimlaneFragment);
            toDoSwimlaneFragment.setModel(toDoTasks);

            List<Task> inProgressTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.InProgress));
            inProgressSwimlaneFragment.setModel(inProgressTasks);

            List<Task> doneTasks = taskRepository.getWhere(new TaskStatusIsQuery(TaskStatus.Done));
            doneSwimlaneFragment.setModel(doneTasks);
        }

        private Task taskFromTaskDto(TaskDto taskDto) {
            Task task = new Task();
            task.id = taskDto.taskId;
            task.description = taskDto.taskDescription;
            task.status = taskDto.taskStatus;
            return task;
        }
    };

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
            Intent intent = new Intent(WorkspaceActivity.this, CreateTaskActivity.class);
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

    public class SwimlanesPagesAdapter extends FragmentPagerAdapter {
        // TODO: this adapter should be responsible for holding references to fragments - it's how this should be designed
        private SwimlaneFragment[] swimlaneFragments;

        public SwimlanesPagesAdapter(
                FragmentManager fragmentManager,
                SwimlaneFragment[] swimlaneFragments) {

            super(fragmentManager);
            this.swimlaneFragments = swimlaneFragments;
        }

        @Override
        public Fragment getItem(int position) {
            return swimlaneFragments[position];
        }

        @Override
        public int getCount() {
            return swimlaneFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return swimlaneFragments[position].getSwimlaneName();
        }
    }

    public static class SwimlaneFragment extends Fragment {
        private static final String ARG_SWIMLANE_NAME = "swimlane_name";
        private ListView taskThumbnailsListView;
        private ListAdapter listAdapter;

        public static SwimlaneFragment newInstance(String swimlaneName) {
            SwimlaneFragment swimlaneFragment = new SwimlaneFragment();
            swimlaneFragment.setRetainInstance(false);
            Bundle args = new Bundle();
            args.putString(ARG_SWIMLANE_NAME, swimlaneName);
            swimlaneFragment.setArguments(args);

            Ln.i("[%s] CREATED INSTANCE: %s", swimlaneFragment, swimlaneName);

            return swimlaneFragment;
        }

        public SwimlaneFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.swimlane_view, container, false);
            TextView textView = (TextView)rootView.findViewById(R.id.dummy);
            textView.setText(getArguments().getString(ARG_SWIMLANE_NAME));

            taskThumbnailsListView = (ListView)rootView.findViewById(R.id.taskThumbailsListView);

            Ln.i("[%s] ONCREATEVIEW: %s (%s,%d)", this, getArguments().getString(ARG_SWIMLANE_NAME), getTag(), getId());

            if(listAdapter != null) {
                taskThumbnailsListView.setAdapter(listAdapter);
            }

            return rootView;
        }

        public String getSwimlaneName() {
            return getArguments().getString(ARG_SWIMLANE_NAME);
        }

        public void setModel(List<Task> tasks) {
            Ln.i("[%s] - %d tasks", this, tasks.size());
            listAdapter = new ModelListAdapter<Task>(tasks, new ViewFactory<Task>() {
                @Override
                public View makeView(Task model) {
                    TaskThumbnailView taskThumbnailView = new TaskThumbnailView(getActivity());
                    taskThumbnailView.setModel(model);
                    return taskThumbnailView;
                }
            });

            if(taskThumbnailsListView != null) {
                Ln.i("[%s] - view is not null", this);
                taskThumbnailsListView.setAdapter(listAdapter);
            } else {
                Ln.i("[%s] - view is null", this);
            }
        }
    }
}
