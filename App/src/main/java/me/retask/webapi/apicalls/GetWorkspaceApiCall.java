package me.retask.webapi.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.retask.webapi.ApiCall;
import me.retask.webapi.dto.ServiceResultDto;
import me.retask.webapi.dto.WorkspaceDto;
import me.retask.webapi.dto.WorkspaceServiceResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class GetWorkspaceApiCall implements ApiCall<WorkspaceDto> {
	private final String sessionToken;
	
	public GetWorkspaceApiCall(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	@Override
	public ServiceResultDto<WorkspaceDto> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("sessionToken", sessionToken);		
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/GetWorkspace")
				.query("sessionToken={sessionToken}")					
				.buildAndExpand(uriVariables)
				.toUri();
		
		ResponseEntity<WorkspaceServiceResult> result = restTemplate
				.exchange(
						uri, 
						HttpMethod.GET, 
						null, 
						WorkspaceServiceResult.class);
		
		return result.getBody();
	}
}