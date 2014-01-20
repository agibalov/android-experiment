package me.retask.service.requests;

import android.content.Context;
import android.content.Intent;

import me.retask.service.RetaskService;

public class SignUpRetaskServiceRequest implements RetaskServiceRequest {
    public final static String COMMAND = "signUp";

    private final String email;
    private final String password;

    public SignUpRetaskServiceRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public Intent getIntent(Context context) {
        Intent intent = new Intent(context, RetaskService.class);
        intent.putExtra(EXTRA_COMMAND, COMMAND);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PASSWORD, password);
        return intent;
    }
}
