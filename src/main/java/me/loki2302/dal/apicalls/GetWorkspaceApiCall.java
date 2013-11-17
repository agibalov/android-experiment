package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.WorkspaceDto;

public class GetWorkspaceApiCall implements ApiCall<WorkspaceDto> {
	private final String sessionToken;
	
	public GetWorkspaceApiCall(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Override
	public ServiceResultDto<WorkspaceDto> performApiCall(RetaskApi retaskApi) {
		return retaskApi.getWorkspace(sessionToken);
	}		
	
	@Override
	public String describe() {
		return "Retrieving tasks...";
	}
}