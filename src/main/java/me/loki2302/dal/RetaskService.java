package me.loki2302.dal;

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
		
	public void signIn(
			LongOperationListener longOperationListener, 
			String email, 
			String password, 
			ApiCallback<SessionDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new SignInApiCall(email, password), 
				callback);
	}
	
	public void signUp(
			LongOperationListener longOperationListener, 
			String email, 
			String password, 
			ApiCallback<Object> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new SignUpApiCall(email, password), 
				callback);
	}
	
	public void getWorkspace(
			LongOperationListener longOperationListener, 
			String sessionToken, 
			ApiCallback<WorkspaceDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new GetWorkspaceApiCall(sessionToken),
				callback);
	}
	
	public void createTask(
			LongOperationListener longOperationListener, 
			String sessionToken, 
			TaskDescriptionDto taskDescriptionDto, 
			ApiCallback<TaskDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new CreateTaskApiCall(sessionToken, taskDescriptionDto), 
				callback);		
	}
	
	public void updateTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId,
			TaskDescriptionDto taskDescriptionDto, 
			ApiCallback<TaskDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new UpdateTaskApiCall(sessionToken, taskId, taskDescriptionDto), 
				callback);
	}
	
	public void progressTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId, 
			ApiCallback<TaskDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new ProgressTaskApiCall(sessionToken, taskId), 
				callback);
	}
	
	public void unprogressTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId, 
			ApiCallback<TaskDto> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new UnprogressTaskApiCall(sessionToken, taskId), 
				callback);
	}
	
	public void deleteTask(
			LongOperationListener longOperationListener, 
			String sessionToken,
			int taskId, 
			ApiCallback<Object> callback) {
		
		apiCallProcessor.process(
				longOperationListener, 
				new DeleteTaskApiCall(sessionToken, taskId), 
				callback);
	}
}