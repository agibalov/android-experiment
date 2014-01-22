package me.retask.service.requests;

import android.content.ContentResolver;

import me.retask.dal.ApplicationState;
import me.retask.webapi.ApiCallProcessor;

public class SignUpRequest implements ServiceRequest<Void> {
    private final String email;
    private final String password;

    public SignUpRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Void run(ApiCallProcessor apiCallProcessor, ApplicationState applicationState, ContentResolver contentResolver) {
        // TODO
        return null;
    }
}
