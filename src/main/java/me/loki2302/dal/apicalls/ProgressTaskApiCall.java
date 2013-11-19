package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDto;

public class ProgressTaskApiCall implements ApiCall<TaskDto> {
	private final String sessionToken;
	private final int taskId;		
	
	public ProgressTaskApiCall(String sessionToken, int taskId) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
	}

	@Override
	public String describe() {
		return "Progressing task...";
	}

	@Override
	public ServiceResultDto<TaskDto> performApiCall(RetaskApi retaskApi) {
		return retaskApi.progressTask(sessionToken, taskId);
	}		
}