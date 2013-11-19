package me.loki2302.dal;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


import me.loki2302.dal.dto.ObjectServiceResult;
import me.loki2302.dal.dto.ServiceResultDto;
import me.loki2302.dal.dto.SessionDto;
import me.loki2302.dal.dto.SessionServiceResult;
import me.loki2302.dal.dto.TaskDescriptionDto;
import me.loki2302.dal.dto.TaskDto;
import me.loki2302.dal.dto.WorkspaceDto;
import me.loki2302.dal.dto.WorkspaceServiceResult;

import org.springframework.http.HttpEntity;
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
	
	public ServiceResultDto<TaskDto> createTask(String sessionToken, TaskDescriptionDto taskDescriptionDto) {
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("sessionToken", sessionToken);
		
		URI uri = UriComponentsBuilder
				.fromUriString(apiRootUrl)
				.path("/CreateTask")
				.query("sessionToken={sessionToken}")					
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
	
	public ServiceResultDto<TaskDto> updateTask(String sessionToken, int taskId, TaskDescriptionDto taskDescriptionDto) {
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
	
	public ServiceResultDto<TaskDto> progressTask(String sessionToken, int taskId) {
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
	
	public ServiceResultDto<TaskDto> unprogressTask(String sessionToken, int taskId) {
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
	
	public ServiceResultDto<Object> deleteTask(String sessionToken, int taskId) {
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
