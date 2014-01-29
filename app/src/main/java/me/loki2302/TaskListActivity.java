package me.loki2302;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.inject.Inject;

import java.util.List;

import me.loki2302.app.App;
import me.loki2302.app.data.Task;
import me.loki2302.app.locators.AllTasksListener;
import me.loki2302.app.locators.AllTasksResourceLocator;

public class TaskListActivity extends RoboActionBarActivity implements AllTasksListener {
    @Inject
    private App app;
    private String subscriptionToken;
    private TaskListAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_layout);

        taskListAdapter = new TaskListAdapter(this);
        ListView taskListView = (ListView)findViewById(R.id.taskListView);
        taskListView.setAdapter(taskListAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent viewTaskIntent = new Intent(TaskListActivity.this, ViewTaskActivity.class);
                viewTaskIntent.putExtra("taskId", (int)id);
                startActivity(viewTaskIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.unsubscribe(subscriptionToken);
        subscriptionToken = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscriptionToken = app.subscribe(new AllTasksResourceLocator(), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.createTaskMenuItem) {
            Intent createTaskIntent = new Intent(TaskListActivity.this, CreateTaskActivity.class);
            startActivity(createTaskIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSetTaskList(final List<Task> tasks) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                taskListAdapter.setTasks(tasks);
                taskListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onTaskListChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                taskListAdapter.notifyDataSetChanged();
            }
        });
    }
}
