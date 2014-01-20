package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.Intent;

import org.springframework.web.client.RestTemplate;

import me.retask.service.requests.DeleteTaskRetaskServiceRequest;
import me.retask.service.requests.RetaskServiceRequest;

public abstract class RetaskServiceRequestHandler {
    public abstract void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate);

    protected static String getEmail(Intent input) {
        String email = input.getStringExtra(RetaskServiceRequest.EXTRA_EMAIL);
        if(email == null || email.equals("")) {
            throw new IllegalArgumentException("Email has not been provided");
        }

        return email;
    }

    protected static String getPassword(Intent input) {
        String password = input.getStringExtra(RetaskServiceRequest.EXTRA_PASSWORD);
        if(password == null || password.equals("")) {
            throw new IllegalArgumentException("Password has not been provided");
        }

        return password;
    }

    protected static String getSessionToken(Intent input) {
        String sessionToken = input.getStringExtra(RetaskServiceRequest.EXTRA_SESSION_TOKEN);
        if(sessionToken == null || sessionToken.equals("")) {
            throw new IllegalArgumentException("SessionToken has not been provided");
        }

        return sessionToken;
    }

    protected static long getTaskId(Intent input) {
        long taskId = input.getLongExtra(DeleteTaskRetaskServiceRequest.EXTRA_TASK_ID, -1);
        if(taskId == -1) {
            throw new IllegalArgumentException("TaskId has not been provided");
        }

        return taskId;
    }

    protected static String getTaskDescription(Intent input) {
        String sessionToken = input.getStringExtra(RetaskServiceRequest.EXTRA_TASK_DESCRIPTION);
        if(sessionToken == null || sessionToken.equals("")) {
            throw new IllegalArgumentException("TaskDescription has not been provided");
        }

        return sessionToken;
    }
}
