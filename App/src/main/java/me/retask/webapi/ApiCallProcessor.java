package me.retask.webapi;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import org.springframework.web.client.RestTemplate;

import me.retask.webapi.dto.ServiceResultDto;

@Singleton
public class ApiCallProcessor {
    @Inject
    @Named("apiRootUrl")
    private String apiRootUrl;

    @Inject
    private ConnectivityManager connectivityManager;

    @Inject
    private RestTemplate restTemplate;

    public <TResult> TResult processApiCall(ApiCall<TResult> apiCall) {
        throwIfNoConnectivity();

        ServiceResultDto<TResult> serviceResultDto;

        try {
            serviceResultDto = apiCall.performApiCall(apiRootUrl, restTemplate);
        } catch(RuntimeException e) {
            throw new NetworkException();
        }

        if(!serviceResultDto.ok) {
            throw new RetaskServiceException(serviceResultDto.error, serviceResultDto.fieldsInError);
        }

        return serviceResultDto.payload;
    }

    private void throwIfNoConnectivity() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null) {
            throw new ConnectivityException();
        }

        if(!networkInfo.isAvailable()) {
            throw new ConnectivityException();
        }

        if(!networkInfo.isConnected()) {
            throw new ConnectivityException();
        }
    }
}
