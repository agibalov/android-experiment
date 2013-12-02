package me.loki2302.dal.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.TaskServiceResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class UnprogressTaskApiCall implements ApiCall<TaskDto> {
	private final String sessionToken;
	private final int taskId;		
	
	public UnprogressTaskApiCall(String sessionToken, int taskId) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
	}

	@Override
	public String describe() {
		return "Unprogressing task...";
	}

	@Override
	public ServiceResultDto<TaskDto> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("sessionToken", sessionToken);
		uriVariables.put("taskId", taskId);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/UnprogressTask")
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