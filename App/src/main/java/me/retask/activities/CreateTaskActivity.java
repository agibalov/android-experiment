package me.retask.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import me.retask.R;
import me.retask.service.requests.CreateTaskRequest;

public class CreateTaskActivity extends RetaskActivity {
    private EditText taskDescriptionEditText;

    @Override
	protected void onCreate(Bundle savedInstanceState) {			
		super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_view);
        taskDescriptionEditText = (EditText)findViewById(R.id.taskDescriptionEditText);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.create_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.createTaskMenuItem) {
            String taskDescription = taskDescriptionEditText.getText().toString();
            if(taskDescription == null || taskDescription.equals("")) {
                return true;
            }

            run(new CreateTaskRequest(taskDescription));
            finish(); // TODO: only finish when request is completed

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
