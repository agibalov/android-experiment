package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;

public class DeleteTaskApiCall implements ApiCall<Object> {
	private final String sessionToken;
	private final int taskId;		
	
	public DeleteTaskApiCall(String sessionToken, int taskId) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
	}

	@Override
	public String describe() {
		return "Deleting task...";
	}

	@Override
	public ServiceResultDto<Object> performApiCall(RetaskApi retaskApi) {
		return retaskApi.deleteTask(sessionToken, taskId);
	}		
}