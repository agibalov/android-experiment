package me.retask;

import android.app.Application;

import com.google.inject.Injector;

import me.retask.service.ApplicationState;
import me.retask.service.RetaskService;
import me.retask.service.requests.SignInRequest;
import roboguice.RoboGuice;

public class RetaskApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Injector injector = RoboGuice.getBaseApplicationInjector(this);
        ApplicationState applicationState = injector.getInstance(ApplicationState.class);
        if(applicationState.getRememberMe()) {
            RetaskService retaskService = injector.getInstance(RetaskService.class);
            String email = applicationState.getEmail();
            String password = applicationState.getPassword();
            retaskService.submit(new SignInRequest(email, password, true));
        }
    }
}
