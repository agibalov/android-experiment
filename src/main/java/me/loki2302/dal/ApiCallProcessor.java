package me.loki2302.dal;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.web.client.RestTemplate;

import me.loki2302.dal.dto.ServiceResultDto;

import roboguice.util.Ln;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class ApiCallProcessor {	
	private final Executor executor = Executors.newSingleThreadExecutor();
	
	@Inject
	@Named("apiRootUrl")
	private String apiRootUrl;
	
	@Inject	
	private RestTemplate restTemplate;
	
	public <TResult> void process(
			final LongOperationListener longOperationListener, 
			final ApiCall<TResult> apiCall, 
			final ApiCallback<TResult> callback) {
		
		longOperationListener.onLongOperationStarted(apiCall.describe());
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					final ServiceResultDto<TResult> result = apiCall.performApiCall(apiRootUrl, restTemplate);
					
					Ln.i("api=%s, response ok=%b, error=%s", apiCall.describe(), result.ok, result.error);
					
					if(result.ok) {
						callback.onSuccess(result.payload);
					} else {						
						callback.onError(result, null);								
					}						
				} catch(final Exception e) {
					e.printStackTrace();
					callback.onError(null, e);								
				} finally {
					longOperationListener.onLongOperationFinished();
				}
			}				
		});
	}
}