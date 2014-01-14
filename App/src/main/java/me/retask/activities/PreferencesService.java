package me.retask.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PreferencesService {
	private final static String REMEMBER_ME_PREFERENCE_NAME = "remember";
	private final static String EMAIL_PREFERENCE_NAME = "email";
	private final static String PASSWORD_PREFERENCE_NAME = "password";
	
	@Inject
	private SharedPreferences sharedPreferences;
	
	public Credentials getCredentials() {
		boolean rememberMe = sharedPreferences.getBoolean(REMEMBER_ME_PREFERENCE_NAME, false);
		if(!rememberMe) {
			return null;
		}
		
		String email = sharedPreferences.getString(EMAIL_PREFERENCE_NAME, null);
		String password = sharedPreferences.getString(PASSWORD_PREFERENCE_NAME, null);
		return new Credentials(email, password);
	}
	
	public void setCredentials(Credentials credentials) {
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(REMEMBER_ME_PREFERENCE_NAME, true);
		editor.putString(EMAIL_PREFERENCE_NAME, credentials.email);
		editor.putString(PASSWORD_PREFERENCE_NAME, credentials.password);
		editor.commit();						
	}
	
	public void unsetCredentials() {
		Editor editor = sharedPreferences.edit();
		editor.remove(REMEMBER_ME_PREFERENCE_NAME);			
		editor.remove(EMAIL_PREFERENCE_NAME);
		editor.remove(PASSWORD_PREFERENCE_NAME);
		editor.commit();
	}
}