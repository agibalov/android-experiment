package me.loki2302;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;

import me.loki2302.app.App;
import me.loki2302.app.commands.UpdateTaskApplicationCommand;
import me.loki2302.app.data.Task;
import me.loki2302.app.locators.SingleTaskListener;
import me.loki2302.app.locators.SingleTaskResourceLocator;
import me.loki2302.infrastructure.BaseActivity;
import me.loki2302.infrastructure.ContextAwareApplicationCommandResultListener;

public class EditTaskActivity extends BaseActivity<EditTaskActivity> implements SingleTaskListener {
    @Inject
    private App app;
    private String subscriptionToken;
    private int taskId;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task_layout);

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
        menuInflater.inflate(R.menu.edit_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if(itemId == R.id.saveTaskMenuItem) {
            String taskDescription = ((EditText)findViewById(R.id.taskDescriptionEditText)).getText().toString();
            if(taskDescription == null || taskDescription.equals("")) {
                Toast.makeText(this, "Description should not be empty", Toast.LENGTH_SHORT).show();
                return true;
            }

            submit(new UpdateTaskApplicationCommand(taskId, taskDescription), new ContextAwareApplicationCommandResultListener<EditTaskActivity, Void>() {
                @Override
                public void onResult(EditTaskActivity editTaskActivity, Void result) {
                    editTaskActivity.finish();
                }
            });

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
                ((EditText)findViewById(R.id.taskDescriptionEditText)).setText(task.description);
            }
        });
    }

    @Override
    public void onTaskChanged() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((EditText)findViewById(R.id.taskDescriptionEditText)).setText(task.description);
            }
        });
    }

    @Override
    protected String getActivityId() {
        return String.format("edittask-%d", getIntent().getIntExtra("taskId", -1));
    }
}
