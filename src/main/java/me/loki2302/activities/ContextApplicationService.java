package me.loki2302.activities;

import java.util.List;

import org.jdeferred.Promise;
import me.loki2302.application.Task;
import me.loki2302.dal.ApplicationService;
import me.loki2302.dal.dto.TaskStatus;
import roboguice.inject.ContextSingleton;

import android.content.Context;

import com.google.inject.Inject;

@ContextSingleton
public class ContextApplicationService {
	@Inject
	private Context context;
	
	@Inject
	private ProgressDialogLongOperationListener longOperationListener;
	
	@Inject
	private ApplicationService applicationService;
	
	//
	// TODO: can I get rid of this class if inject Provider<Activity> to real application service?
	//
	
	public Promise<String, Exception, Void> signIn(String email, String password) {
		return applicationService.signIn(
				longOperationListener, 
				email, 
				password);
	}
	
	public Promise<List<Task>, Exception, Void> getTasksByStatus(TaskStatus status) {
		return applicationService.getTasksByStatus(
				longOperationListener, 
				status);
	}
	
	public Promise<Task, Exception, Void> getTask(int taskId) {
		return applicationService.getTask(
				longOperationListener, 
				taskId);
	}
	
	public Promise<Task, Exception, Void> createTask(String taskDescription) {
		return applicationService.createTask(
				longOperationListener, 
				taskDescription);
	}
	
	public Promise<Task, Exception, Void> updateTask(int taskId, String taskDescription) {
		return applicationService.updateTask(
				longOperationListener,
				taskId,
				taskDescription);
	}
	
	public Promise<Task, Exception, Void> progressTask(int taskId) {
		return applicationService.progressTask(
				longOperationListener,
				taskId);
	}
	
	public Promise<Task, Exception, Void> unprogressTask(int taskId) {
		return applicationService.unprogressTask(
				longOperationListener,
				taskId);
	}
	
	public Promise<Object, Exception, Void> deleteTask(int taskId) {
		return applicationService.deleteTask(
				longOperationListener,
				taskId);
	}
}