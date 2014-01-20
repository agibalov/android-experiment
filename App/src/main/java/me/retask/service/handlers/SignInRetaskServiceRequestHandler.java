package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;

import org.springframework.web.client.RestTemplate;

import me.retask.dal.RetaskContract;
import me.retask.webapi.apicalls.GetWorkspaceApiCall;
import me.retask.webapi.apicalls.SignInApiCall;
import me.retask.webapi.dto.ServiceResultDto;
import me.retask.webapi.dto.SessionDto;
import me.retask.webapi.dto.TaskDto;
import me.retask.webapi.dto.WorkspaceDto;

public class SignInRetaskServiceRequestHandler extends RetaskServiceRequestHandler {
    @Override
    public void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate) {
        String email = getEmail(input);
        String password = getPassword(input);

        SignInApiCall signInApiCall = new SignInApiCall(email, password);
        ServiceResultDto<SessionDto> sessionDtoResult = signInApiCall.performApiCall(apiRootUrl, restTemplate);
        if(!sessionDtoResult.ok) {
            // TODO
            return;
        }

        SessionDto sessionDto = sessionDtoResult.payload;
        String sessionToken = sessionDto.sessionToken;

        GetWorkspaceApiCall getWorkspaceApiCall = new GetWorkspaceApiCall(sessionToken);
        ServiceResultDto<WorkspaceDto> workspaceDtoResult = getWorkspaceApiCall.performApiCall(apiRootUrl, restTemplate);
        if(!workspaceDtoResult.ok) {
            // TODO
            return;
        }

        WorkspaceDto workspaceDto = workspaceDtoResult.payload;

        contentResolver.delete(RetaskContract.Task.CONTENT_URI, null, null);

        for(TaskDto taskDto : workspaceDto.tasks) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RetaskContract.Task.REMOTE_ID, taskDto.taskId);
            contentValues.put(RetaskContract.Task.STATUS, taskDto.taskStatus); // TODO: not very fair
            contentValues.put(RetaskContract.Task.DESCRIPTION, taskDto.taskDescription);

            contentResolver.insert(RetaskContract.Task.CONTENT_URI, contentValues);
        }
    }
}
