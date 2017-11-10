package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.service.ApplicationState;
import me.retask.dal.RetaskContentResolverUtils;
import me.retask.webapi.ApiCallProcessor;
import me.retask.webapi.apicalls.CreateTaskApiCall;
import me.retask.webapi.dto.TaskDescriptionDto;
import me.retask.webapi.dto.TaskDto;

public class CreateTaskRequest implements ServiceRequest<Void> {
    private final String taskDescription;

    public CreateTaskRequest(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        TaskDescriptionDto taskDescriptionDto = new TaskDescriptionDto();
        taskDescriptionDto.taskDescription = taskDescription;
        CreateTaskApiCall createTaskApiCall = new CreateTaskApiCall(applicationState.getAndUpdateSessionToken(), taskDescriptionDto);
        TaskDto taskDto = apiCallProcessor.processApiCall(createTaskApiCall);
        RetaskContentResolverUtils.addTask(contentResolver, taskDto);

        return null;
    }
}
