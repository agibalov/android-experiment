package me.loki2302.dal;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


import me.loki2302.dal.dto.ObjectServiceResult;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.SessionDto.SessionServiceResult;
import me.loki2302.dal.dto.WorkspaceDto;
import me.loki2302.dal.dto.WorkspaceDto.WorkspaceServiceResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class RetaskApi {
	private final RestTemplate restTemplate;
	private final String apiRootUrl;
	
	public RetaskApi(RestTemplate restTemplate, String apiRootUrl) {
		this.restTemplate = restTemplate;
		this.apiRootUrl = apiRootUrl;
	}
	
	public ServiceResultDto<SessionDto> signIn(String email, String password) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("email", email);
		uriVariables.put("password", password);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/SignIn")
				.query("email={email}&password={password}")					
				.buildAndExpand(uriVariables)
				.toUri();
		
		ResponseEntity<SessionServiceResult> result = restTemplate
				.exchange(
						uri, 
						HttpMethod.POST, 
						null, 
						SessionServiceResult.class);
		
		return result.getBody();
	}
	
	public ServiceResultDto<Object> signUp(String email, String password) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("email", email);
		uriVariables.put("password", password);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/SignUp")
				.query("email={email}&password={password}")					
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
	
	public ServiceResultDto<WorkspaceDto> getWorkspace(String sessionToken) {
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