package me.loki2302.dal.apicalls;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.RetaskApi;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;

public class UpdateTaskApiCall implements ApiCall<TaskDto> {
	private final String sessionToken;
	private final int taskId;
	private final TaskDescriptionDto taskDescriptionDto;
	
	public UpdateTaskApiCall(String sessionToken, int taskId, TaskDescriptionDto taskDescriptionDto) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
		this.taskDescriptionDto = taskDescriptionDto;
	}

	@Override
	public String describe() {
		return "Creating task...";
	}

	@Override
	public ServiceResultDto<TaskDto> performApiCall(RetaskApi retaskApi) {
		return retaskApi.updateTask(sessionToken, taskId, taskDescriptionDto);
	}		
}