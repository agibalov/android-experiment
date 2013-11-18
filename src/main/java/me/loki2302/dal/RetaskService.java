package me.loki2302.dal;

import me.loki2302.dal.apicalls.GetWorkspaceApiCall;
import me.loki2302.dal.apicalls.SignInApiCall;
import me.loki2302.dal.apicalls.SignUpApiCall;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.WorkspaceDto;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RetaskService {
	@Inject
	private ApiCallProcessor apiCallProcessor;	
		
	public void signIn(LongOperationListener longOperationListener, String email, String password, ApiCallback<SessionDto> callback) {
		apiCallProcessor.process(longOperationListener, new SignInApiCall(email, password), callback);
	}
	
	public void signUp(LongOperationListener longOperationListener, String email, String password, ApiCallback<Object> callback) {
		apiCallProcessor.process(longOperationListener, new SignUpApiCall(email, password), callback);
	}
	
	public void getWorkspace(LongOperationListener longOperationListener, String sessionToken, ApiCallback<WorkspaceDto> callback) {
		apiCallProcessor.process(longOperationListener, new GetWorkspaceApiCall(sessionToken), callback);
	}
}