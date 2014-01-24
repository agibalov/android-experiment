package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.service.ApplicationState;
import me.retask.dal.RetaskContentResolverUtils;
import me.retask.webapi.ApiCallProcessor;
import me.retask.webapi.apicalls.GetWorkspaceApiCall;
import me.retask.webapi.apicalls.SignInApiCall;
import me.retask.webapi.dto.SessionDto;
import me.retask.webapi.dto.WorkspaceDto;

public class SignInRequest implements ServiceRequest<Void> {
    private final String email;
    private final String password;
    private final boolean rememberMe;

    public SignInRequest(String email, String password, boolean rememberMe) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        // Authenticate
        SignInApiCall signInApiCall = new SignInApiCall(email, password);
        SessionDto sessionDto = apiCallProcessor.processApiCall(signInApiCall);
        String sessionToken = sessionDto.sessionToken;

        // Retrieve tasks
        GetWorkspaceApiCall getWorkspaceApiCall = new GetWorkspaceApiCall(sessionToken);
        WorkspaceDto workspaceDto = apiCallProcessor.processApiCall(getWorkspaceApiCall);

        // Update DB
        RetaskContentResolverUtils.reset(contentResolver);
        RetaskContentResolverUtils.addTasks(contentResolver, workspaceDto.tasks);

        // Done
        applicationState.setEmail(email);
        applicationState.setPassword(password);
        applicationState.setRememberMe(rememberMe);
        applicationState.setSessionToken(sessionToken);

        return null;
    }
}
