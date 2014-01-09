package me.loki2302;

import android.app.Application;

import com.testflightapp.lib.TestFlight;

public class RetaskApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TestFlight.takeOff(this, "<PUT API TOKEN HERE>");
    }
}
