package me.loki2302.dal;

import org.springframework.web.client.RestTemplate;

import me.loki2302.dal.dto.ServiceResultDto;

public interface ApiCall<TResult> {
	ServiceResultDto<TResult> performApiCall(String apiRootUrl, RestTemplate restTemplate);
}