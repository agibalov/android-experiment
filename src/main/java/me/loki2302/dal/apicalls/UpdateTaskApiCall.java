package me.loki2302.dal.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.TaskServiceResult;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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
	public ServiceResultDto<TaskDto> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("sessionToken", sessionToken);
		uriVariables.put("taskId", taskId);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/UpdateTask")
				.query("sessionToken={sessionToken}&taskId={taskId}")					
				.buildAndExpand(uriVariables)
				.toUri();
		
		ResponseEntity<TaskServiceResult> result = restTemplate
				.exchange(
						uri, 
						HttpMethod.POST, 
						new HttpEntity<TaskDescriptionDto>(taskDescriptionDto), 
						TaskServiceResult.class);
		
		return result.getBody();
	}		
}