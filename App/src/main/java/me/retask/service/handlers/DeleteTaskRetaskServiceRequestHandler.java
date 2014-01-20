package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.Intent;

import org.springframework.web.client.RestTemplate;

public class DeleteTaskRetaskServiceRequestHandler extends RetaskServiceRequestHandler {
    @Override
    public void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate) {
        long taskId = getTaskId(input);
        throw new RuntimeException("Not implemented");
    }
}
