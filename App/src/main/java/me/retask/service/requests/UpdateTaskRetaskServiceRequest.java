package me.retask.service.requests;

import android.content.Context;
import android.content.Intent;

import me.retask.service.RetaskService;

public class UpdateTaskRetaskServiceRequest implements RetaskServiceRequest {
    public final static String COMMAND = "updateTask";

    private final String sessionToken;
    private final long taskId;
    private final String taskDescription;

    public UpdateTaskRetaskServiceRequest(String sessionToken, long taskId, String taskDescription) {
        this.sessionToken = sessionToken;
        this.taskId = taskId;
        this.taskDescription = taskDescription;
    }

    @Override
    public Intent getIntent(Context context) {
        Intent intent = new Intent(context, RetaskService.class);
        intent.putExtra(EXTRA_COMMAND, COMMAND);
        intent.putExtra(EXTRA_SESSION_TOKEN, sessionToken);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        intent.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
        return intent;
    }
}
