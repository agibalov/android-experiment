package me.retask.service.handlers;

import android.content.ContentResolver;
import android.content.Intent;

import org.springframework.web.client.RestTemplate;

public class SignUpRetaskServiceRequestHandler extends RetaskServiceRequestHandler {
    @Override
    public void process(Intent input, ContentResolver contentResolver, String apiRootUrl, RestTemplate restTemplate) {
        String email = getEmail(input);
        String password = getPassword(input);
        throw new RuntimeException("Not implemented");
    }
}
