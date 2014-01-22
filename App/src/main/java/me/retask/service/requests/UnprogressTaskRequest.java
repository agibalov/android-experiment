package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.service.ApplicationState;
import me.retask.dal.RetaskContentResolverUtils;
import me.retask.webapi.ApiCallProcessor;
import me.retask.webapi.apicalls.UnprogressTaskApiCall;
import me.retask.webapi.dto.TaskDto;

public class UnprogressTaskRequest implements ServiceRequest<Void> {
    private final long taskId;

    public UnprogressTaskRequest(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        int taskRemoteId = RetaskContentResolverUtils.getTaskRemoteId(contentResolver, taskId);
        UnprogressTaskApiCall unprogressTaskApiCall = new UnprogressTaskApiCall(applicationState.getAndUpdateSessionToken(), taskRemoteId);
        TaskDto taskDto = apiCallProcessor.processApiCall(unprogressTaskApiCall);
        RetaskContentResolverUtils.updateTask(contentResolver, taskId, taskDto);
        return null;
    }
}
