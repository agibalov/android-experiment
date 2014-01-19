package me.retask.dal;

import android.content.ContentResolver;

import org.springframework.web.client.RestTemplate;

import me.retask.dal.dto.ServiceResultDto;

public interface ApiCall<TResult> {
	ServiceResultDto<TResult> performApiCall(String apiRootUrl, RestTemplate restTemplate);
}