package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.service.ApplicationState;
import me.retask.dal.RetaskContentResolverUtils;
import me.retask.webapi.ApiCallProcessor;
import me.retask.webapi.apicalls.DeleteTaskApiCall;

public class DeleteTaskRequest implements ServiceRequest<Void> {
    private final long taskId;

    public DeleteTaskRequest(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        int taskRemoteId = RetaskContentResolverUtils.getTaskRemoteId(contentResolver, taskId);
        DeleteTaskApiCall progressTaskApiCall = new DeleteTaskApiCall(applicationState.getAndUpdateSessionToken(), taskRemoteId);
        apiCallProcessor.processApiCall(progressTaskApiCall);
        RetaskContentResolverUtils.deleteTask(contentResolver, taskId);

        return null;
    }
}
