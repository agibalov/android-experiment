package me.retask.service.requests;

import android.content.Context;
import android.content.Intent;

import me.retask.service.RetaskService;

public class CreateTaskRetaskServiceRequest implements RetaskServiceRequest {
    public final static String COMMAND = "createTask";

    private final String sessionToken;
    private final String taskDescription;

    public CreateTaskRetaskServiceRequest(String sessionToken, String taskDescription) {
        this.sessionToken = sessionToken;
        this.taskDescription = taskDescription;
    }

    @Override
    public Intent getIntent(Context context) {
        Intent intent = new Intent(context, RetaskService.class);
        intent.putExtra(EXTRA_COMMAND, COMMAND);
        intent.putExtra(EXTRA_SESSION_TOKEN, sessionToken);
        intent.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription);
        return intent;
    }
}
