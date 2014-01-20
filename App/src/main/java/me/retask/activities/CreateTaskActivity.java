package me.retask.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.inject.Inject;

import me.retask.R;
import me.retask.dal.ApplicationState;
import me.retask.service.requests.CreateTaskRetaskServiceRequest;

public class CreateTaskActivity extends RetaskActivity {
	@Inject
	private ApplicationState applicationState;

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
            run(new CreateTaskRetaskServiceRequest(applicationState.getSessionToken(), taskDescription));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
