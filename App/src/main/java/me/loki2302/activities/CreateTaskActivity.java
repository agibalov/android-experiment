package me.loki2302.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.google.inject.Inject;

import me.loki2302.R;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.CreateTaskApiCall;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;
import roboguice.util.Ln;

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
            TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
            taskDescriptionDto.taskDescription = taskDescription;
            run(new CreateTaskApiCall(applicationState.getSessionToken(), taskDescriptionDto), onTaskCreated);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final DoneCallback<TaskDto> onTaskCreated = new DoneCallback<TaskDto>() {
        @Override
        public void onDone(TaskDto taskDto) {
            Ln.i("Created task with id %d", taskDto.taskId);
            finish();
        }
    };
}
