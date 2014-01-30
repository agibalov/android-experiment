package me.loki2302;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import me.loki2302.app.commands.CreateTaskApplicationCommand;
import me.loki2302.infrastructure.BaseActivity;
import me.loki2302.infrastructure.ContextAwareApplicationCommandResultListener;

public class CreateTaskActivity extends BaseActivity<CreateTaskActivity> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_layout);
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

            submit(new CreateTaskApplicationCommand(taskDescription), new ContextAwareApplicationCommandResultListener<CreateTaskActivity, Integer>() {
                @Override
                public void onResult(CreateTaskActivity createTaskActivity, Integer result) {
                    Intent viewTaskIntent = new Intent(createTaskActivity, ViewTaskActivity.class);
                    viewTaskIntent.putExtra("taskId", (int)result);
                    createTaskActivity.startActivity(viewTaskIntent);
                    createTaskActivity.finish();
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected String getActivityId() {
        return "createtask";
    }
}
