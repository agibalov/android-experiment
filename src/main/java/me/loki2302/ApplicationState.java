package me.loki2302;

import me.loki2302.dal.Repository;
import me.loki2302.dal.Task;

import com.google.inject.Singleton;

@Singleton
public class ApplicationState {
	private String sessionToken;
	
	private volatile boolean isTaskRepositoryInitialized;
	private final Repository<Task> taskRepository = new Repository<Task>();
	
	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
	public String getSessionToken() {
		return sessionToken;
	}
	
	public boolean isTaskRepositoryInitialized() {
		return isTaskRepositoryInitialized;
	}
	
	public void setTaskRepositoryInitialized(boolean isTaskRepositoryInitialized) {
		this.isTaskRepositoryInitialized = isTaskRepositoryInitialized;
	}
	
	public Repository<Task> getTaskRepository() {
		return taskRepository;
	}
}