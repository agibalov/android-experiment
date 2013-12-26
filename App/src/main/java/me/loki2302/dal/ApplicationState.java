package me.loki2302.dal;

import me.loki2302.application.Repository;
import me.loki2302.application.Task;

import com.google.inject.Singleton;

@Singleton
public class ApplicationState {
	private String sessionToken;
	private Repository<Task> taskRepository = new Repository<Task>();
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}		
	
	public Repository<Task> getTaskRepository() {
		return taskRepository;
	}
}