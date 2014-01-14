package me.retask;

import android.app.Application;

public class RetaskApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TestFlight.takeOff(this, "<PUT API TOKEN HERE>");
    }
}
