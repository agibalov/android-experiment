package me.retask.dal;

import me.retask.application.Repository;
import me.retask.application.Task;

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