package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.dal.ApplicationState;
import me.retask.dal.RetaskContentResolverUtils;
import me.retask.webapi.ApiCallProcessor;
import me.retask.webapi.apicalls.UpdateTaskApiCall;
import me.retask.webapi.dto.TaskDescriptionDto;
import me.retask.webapi.dto.TaskDto;

public class UpdateTaskRequest implements ServiceRequest<Void> {
    private final long taskId;
    private final String taskDescription;

    public UpdateTaskRequest(long taskId, String taskDescription) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        int taskRemoteId = RetaskContentResolverUtils.getTaskRemoteId(contentResolver, taskId);

        TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
        taskDescriptionDto.taskDescription = taskDescription;
        UpdateTaskApiCall updateTaskApiCall = new UpdateTaskApiCall(applicationState.getSessionToken(), taskRemoteId, taskDescriptionDto);
        TaskDto taskDto = apiCallProcessor.processApiCall(updateTaskApiCall);

        RetaskContentResolverUtils.updateTask(contentResolver, taskId, taskDto);

        return null;
    }
}
