package me.retask.webapi;

import org.springframework.web.client.RestTemplate;

import me.retask.webapi.dto.ServiceResultDto;

public interface ApiCall<TResult> {
	ServiceResultDto<TResult> performApiCall(String apiRootUrl, RestTemplate restTemplate);
}