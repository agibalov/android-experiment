package me.retask.dal.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.retask.dal.ApiCall;
import me.retask.dal.dto.ServiceResultDto;
import me.retask.dal.dto.TaskDto;
import me.retask.dal.dto.TaskServiceResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class ProgressTaskApiCall implements ApiCall<TaskDto> {
	private final String sessionToken;
	private final int taskId;		
	
	public ProgressTaskApiCall(String sessionToken, int taskId) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
	}

	@Override
	public ServiceResultDto<TaskDto> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("sessionToken", sessionToken);
		uriVariables.put("taskId", taskId);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/ProgressTask")
				.query("sessionToken={sessionToken}&taskId={taskId}")					
				.buildAndExpand(uriVariables)
				.toUri();
		
		ResponseEntity<TaskServiceResult> result = restTemplate
				.exchange(
						uri, 
						HttpMethod.POST, 
						null, 
						TaskServiceResult.class);
		
		return result.getBody();
	}		
}