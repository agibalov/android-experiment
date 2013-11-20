package me.loki2302.dal;

import org.jdeferred.Promise;

import me.loki2302.dal.apicalls.CreateTaskApiCall;
import me.loki2302.dal.apicalls.DeleteTaskApiCall;
import me.loki2302.dal.apicalls.GetWorkspaceApiCall;
import me.loki2302.dal.apicalls.ProgressTaskApiCall;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.apicalls.SignUpApiCall;
import me.loki2302.dal.apicalls.UnprogressTaskApiCall;
import me.loki2302.dal.apicalls.UpdateTaskApiCall;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.WorkspaceDto;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RetaskService {
	@Inject
	private ApiCallProcessor apiCallProcessor;	
		
	public Promise<SessionDto, Exception, Void> signIn(
			LongOperationListener longOperationListener, 
			String email, 
			String password) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new SignInApiCall(email, password));
	}
	
	public Promise<Object, Exception, Void> signUp(
			LongOperationListener longOperationListener, 
			String email, 
			String password) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new SignUpApiCall(email, password));
	}
	
	public Promise<WorkspaceDto, Exception, Void> getWorkspace(
			LongOperationListener longOperationListener, 
			String sessionToken) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new GetWorkspaceApiCall(sessionToken));
	}
	
	public Promise<TaskDto, Exception, Void> createTask(
			LongOperationListener longOperationListener, 
			String sessionToken, 
			TaskDescriptionDto taskDescriptionDto) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new CreateTaskApiCall(sessionToken, taskDescriptionDto));		
	}
	
	public Promise<TaskDto, Exception, Void> updateTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId,
			TaskDescriptionDto taskDescriptionDto) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new UpdateTaskApiCall(sessionToken, taskId, taskDescriptionDto));
	}
	
	public Promise<TaskDto, Exception, Void> progressTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new ProgressTaskApiCall(sessionToken, taskId));
	}
	
	public Promise<TaskDto, Exception, Void> unprogressTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new UnprogressTaskApiCall(sessionToken, taskId));
	}
	
	public Promise<Object, Exception, Void> deleteTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId) {
		
		return apiCallProcessor.process(
				longOperationListener, 
				new DeleteTaskApiCall(sessionToken, taskId));
	}
}