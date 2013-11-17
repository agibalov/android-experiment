package me.loki2302;

import com.google.inject.Singleton;

@Singleton
public class ApplicationState {
	private String sessionToken;
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public String getSessionToken() {
		return sessionToken;
	}
}