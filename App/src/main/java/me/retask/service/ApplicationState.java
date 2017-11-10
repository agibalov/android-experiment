package me.retask.service;

import android.content.SharedPreferences;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationState {
    private final long MAX_TOKEN_AGE = 60000;

    private final String PREFERENCE_EMAIL = "email";
    private final String PREFERENCE_PASSWORD = "password";
    private final String PREFERENCE_REMEMBER_ME = "rememberMe";
    private final String PREFERENCE_SESSION_TOKEN = "sessionToken";
    private final String PREFERENCE_SESSION_TOKEN_TIME = "sessionTokenTime";

    @Inject
    private SharedPreferences sharedPreferences;

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCE_EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(PREFERENCE_EMAIL, null);
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREFERENCE_PASSWORD, password);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString(PREFERENCE_PASSWORD, null);
    }

    public void setRememberMe(boolean rememberMe) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCE_REMEMBER_ME, rememberMe);
        editor.commit();
    }

    public boolean getRememberMe() {
        return sharedPreferences.getBoolean(PREFERENCE_REMEMBER_ME, false);
    }

    public void setSessionToken(String sessionToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(sessionToken == null || sessionToken.equals("")) {
            editor.remove(PREFERENCE_SESSION_TOKEN);
            editor.remove(PREFERENCE_SESSION_TOKEN_TIME);
        } else {
            editor.putString(PREFERENCE_SESSION_TOKEN, sessionToken);
            editor.putLong(PREFERENCE_SESSION_TOKEN_TIME, System.currentTimeMillis());
        }

        editor.commit();
    }

    public String getAndUpdateSessionToken() {
        if(sharedPreferences.contains(PREFERENCE_SESSION_TOKEN_TIME)) {
            long lastTokenUpdateTime = sharedPreferences.getLong(PREFERENCE_SESSION_TOKEN_TIME, 0);
            long tokenAge = System.currentTimeMillis() - lastTokenUpdateTime;
            boolean canReuseToken = tokenAge <= MAX_TOKEN_AGE;
            if(canReuseToken) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong(PREFERENCE_SESSION_TOKEN_TIME, System.currentTimeMillis());
                editor.commit();
                return sharedPreferences.getString(PREFERENCE_SESSION_TOKEN, null);
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(PREFERENCE_SESSION_TOKEN);
            editor.remove(PREFERENCE_SESSION_TOKEN_TIME);
            editor.commit();
        }

        return null;
    }
}
