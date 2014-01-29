package me.loki2302;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.inject.Inject;

import me.loki2302.app.App;
import me.loki2302.app.commands.DeleteTaskApplicationCommand;
import me.loki2302.app.data.Task;
import me.loki2302.app.locators.SingleTaskListener;
import me.loki2302.app.locators.SingleTaskResourceLocator;

public class ViewTaskActivity extends RoboActionBarActivity implements SingleTaskListener {
    @Inject
    private App app;
    private String subscriptionToken;
    private int taskId;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task_layout);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("taskId", -1);
        if(taskId == -1) {
            throw new IllegalArgumentException();
        }
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
        subscriptionToken = app.subscribe(new SingleTaskResourceLocator(taskId), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.editTaskMenuItem) {
            Intent editTaskIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
            editTaskIntent.putExtra("taskId", taskId);
            startActivity(editTaskIntent);
            return true;
        }

        if(itemId == R.id.deleteTaskMenuItem) {
            app.submit(new DeleteTaskApplicationCommand(taskId));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSetTask(final Task task) {
        this.task = task;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.taskDescriptionTextView)).setText(task.description);
            }
        });
    }

    @Override
    public void onTaskChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.taskDescriptionTextView)).setText(task.description);
            }
        });
    }
}
