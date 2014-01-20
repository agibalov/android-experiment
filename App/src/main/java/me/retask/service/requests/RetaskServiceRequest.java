package me.retask.service.requests;

import android.content.Context;
import android.content.Intent;

public interface RetaskServiceRequest {
    public final static String EXTRA_COMMAND = "command";
    public final static String EXTRA_EMAIL = "email";
    public final static String EXTRA_PASSWORD = "password";
    public final static String EXTRA_SESSION_TOKEN = "sessionToken";
    public final static String EXTRA_TASK_ID = "taskId";
    public final static String EXTRA_TASK_DESCRIPTION = "taskDescription";

    Intent getIntent(Context context);
}
