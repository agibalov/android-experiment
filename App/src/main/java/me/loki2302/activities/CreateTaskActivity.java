package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.inject.Inject;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.CreateTaskApiCall;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;

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
            run(new CreateTaskApiCall(applicationState.getSessionToken(), taskDescriptionDto), onTaskCreated, onFailedToCreateTask);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final DoneCallback<TaskDto> onTaskCreated = new DoneCallback<TaskDto>() {
        @Override
        public void onDone(TaskDto taskDto) {
            applicationState.getTaskRepository().add(Task.fromTaskDto(taskDto));

            Intent intent = new Intent(CreateTaskActivity.this, ViewTaskActivity.class);
            intent.putExtra("taskId", taskDto.taskId);
            startActivity(intent);

            finish();
        }
    };

    private final FailCallback onFailedToCreateTask = new DefaultFailCallback() {
        @Override
        protected void onValidationError(ServiceResultDto<?> serviceResult) {
            Toast.makeText(CreateTaskActivity.this, "Description should not be empty", Toast.LENGTH_SHORT).show();
        }
    };
}
