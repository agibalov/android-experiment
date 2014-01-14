package me.retask.dal.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.retask.dal.ApiCall;
import me.retask.dal.dto.ObjectServiceResult;
import me.retask.dal.dto.ServiceResultDto;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class DeleteTaskApiCall implements ApiCall<Object> {
	private final String sessionToken;
	private final int taskId;		
	
	public DeleteTaskApiCall(String sessionToken, int taskId) {
		this.sessionToken = sessionToken;
		this.taskId = taskId;
	}

	@Override
	public ServiceResultDto<Object> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
		Map<String, Object> uriVariables = new HashMap<String, Object>();
		uriVariables.put("sessionToken", sessionToken);
		uriVariables.put("taskId", taskId);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/DeleteTask")
				.query("sessionToken={sessionToken}&taskId={taskId}")					
				.buildAndExpand(uriVariables)
				.toUri();
		
		ResponseEntity<ObjectServiceResult> result = restTemplate
				.exchange(
						uri, 
						HttpMethod.POST, 
						null, 
						ObjectServiceResult.class);
		
		return result.getBody();
	}		
}