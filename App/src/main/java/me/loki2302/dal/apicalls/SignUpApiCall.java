package me.loki2302.dal.apicalls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import me.loki2302.dal.ApiCall;
import me.loki2302.dal.dto.ObjectServiceResult;
import me.loki2302.dal.dto.ServiceResultDto;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

public class SignUpApiCall implements ApiCall<Object> {
	private final String email;
	private final String password;
	
	public SignUpApiCall(String email, String password) {
		this.email = email;
		this.password = password;
	}

	@Override
	public ServiceResultDto<Object> performApiCall(String apiRootUrl, RestTemplate restTemplate) {
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
	
	@Override
	public String describe() {
		return "Signing up...";
	}
}