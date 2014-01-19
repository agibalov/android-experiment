package me.retask.dal.apicalls;

import android.content.ContentResolver;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.retask.dal.ApiCall;
import me.retask.dal.dto.ServiceResultDto;
import me.retask.dal.dto.SessionDto;
import me.retask.dal.dto.SessionServiceResult;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SignInApiCall implements ApiCall<SessionDto> {
	private final String email;
	private final String password;
	
	public SignInApiCall(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public ServiceResultDto<SessionDto> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
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
}