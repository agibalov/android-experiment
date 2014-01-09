package me.loki2302.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.inject.Inject;

import java.util.Date;

import me.loki2302.R;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationState;
import me.loki2302.dal.apicalls.UpdateTaskApiCall;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;
import roboguice.util.Ln;

public class EditTaskActivity extends RetaskActivity {
    @Inject
    private ApplicationState applicationState;

    private Task task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task_view);

        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", -1);
        if(taskId == -1) {
            throw new IllegalStateException("Looks like taskId is missing in Intent");
        }

        task = applicationState.getTaskRepository().getOne(taskId);
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
        if(itemId == R.id.updateTaskMenuItem) {
            TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
            taskDescriptionDto.taskDescription = "Dummy " + new Date().toString();
            run(new UpdateTaskApiCall(applicationState.getSessionToken(), task.id, taskDescriptionDto), onTaskUpdated);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private final DoneCallback<TaskDto> onTaskUpdated = new DoneCallback<TaskDto>() {
        @Override
        public void onDone(TaskDto taskDto) {
            Ln.i("Task %d updated", taskDto.taskId);
        }
    };
}
