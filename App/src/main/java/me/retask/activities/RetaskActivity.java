package me.retask.activities;

import android.content.Intent;

import me.retask.service.requests.RetaskServiceRequest;

public abstract class RetaskActivity extends RoboActionBarActivity {
    protected void run(RetaskServiceRequest retaskServiceRequest) {
        Intent intent = retaskServiceRequest.getIntent(this);
        startService(intent);
    }
}
