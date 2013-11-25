package me.loki2302.dal;

import com.google.inject.Singleton;

@Singleton
public class ApplicationState {
	private String sessionToken;
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}		
}