package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.Intent;

import org.springframework.web.client.RestTemplate;

public class UpdateTaskRetaskServiceRequestHandler extends RetaskServiceRequestHandler {
    @Override
    public void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate) {
        String sessionToken = getSessionToken(input);
        long taskId = getTaskId(input);
        String taskDescription = getTaskDescription(input);
        throw new RuntimeException("Not implemented");
    }
}
