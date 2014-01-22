package me.retask.webapi;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.springframework.web.client.RestTemplate;

import me.retask.webapi.ApiCall;
import me.retask.webapi.dto.ServiceResultDto;

@Singleton
public class ApiCallProcessor {
    @Inject
    @Named("apiRootUrl")
    private String apiRootUrl;

    @Inject
    private RestTemplate restTemplate;

    public <TResult> TResult processApiCall(ApiCall<TResult> apiCall) {
        ServiceResultDto<TResult> serviceResultDto = apiCall.performApiCall(apiRootUrl, restTemplate);
        if(serviceResultDto.ok) {
            return serviceResultDto.payload;
        }

        throw new RuntimeException("API call failed");
    }
}
