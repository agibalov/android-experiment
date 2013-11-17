package me.loki2302.dal;

import me.loki2302.dal.apicalls.GetWorkspaceApiCall;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.apicalls.SignUpApiCall;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.WorkspaceDto;
import roboguice.inject.ContextSingleton;

import com.google.inject.Inject;

@ContextSingleton
public class RetaskService {
	private final ApiCallProcessor apiCallProcessor;	
	
	@Inject
	public RetaskService(ApiCallProcessor apiCallProcessor) {
		this.apiCallProcessor = apiCallProcessor;
	}
	
	public void signIn(String email, String password, ApiCallback<SessionDto> callback) {
		apiCallProcessor.process(new SignInApiCall(email, password), callback);
	}
	
	public void signUp(String email, String password, ApiCallback<Object> callback) {
		apiCallProcessor.process(new SignUpApiCall(email, password), callback);
	}
	
	public void getWorkspace(String sessionToken, ApiCallback<WorkspaceDto> callback) {
		apiCallProcessor.process(new GetWorkspaceApiCall(sessionToken), callback);
	}
}